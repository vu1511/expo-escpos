package expo.modules.escpos

import expo.modules.escpos.Image.toRaster
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Main class for generating ESC/POS commands for thermal printers
 */
@OptIn(ExperimentalUnsignedTypes::class)
class EscPosEncoder(private val options: Options = Options()) {
    data class Options(
        val width: Int = 0,
        val embedded: Boolean = false,
        val wordWrap: Boolean = true,
        val imageMode: String = "column",
        val codepageMapping: PrinterType = PrinterType.EPSON,
        val codepageCandidates: List<String> = listOf(
            "cp437", "cp858", "cp860", "cp861", "cp863",
            "cp865", "cp852", "cp857", "cp855", "cp866", "cp869"
        )
    )

    data class State(
        var codepage: Int = 0,
        var align: String = "left",
        var bold: Boolean = false,
        var italic: Boolean = false,
        var underline: Boolean = false,
        var invert: Boolean = false,
        var width: Int = 1,
        var height: Int = 1
    )

    private var embedded: Boolean = false
    private var buffer = UByteArray(1024)  // Initial buffer size
    private var bufferSize = 0
    private var queued = UByteArray(256)   // Initial queue size
    private var queuedSize = 0
    private var cursor: Int = 0
    private var codepage: String = "ascii"
    private var state = State()

    init {
        reset(options)
    }

    /**
     * Reset the encoder state with new options
     */
    private fun reset(options: Options) {
        this.embedded = options.embedded
        this.buffer = UByteArray(1024)
        this.bufferSize = 0
        this.queued = UByteArray(256)
        this.queuedSize = 0
        this.cursor = 0
        this.codepage = "ascii"
        this.state = State()
    }

    /**
     * Ensure buffer has enough capacity for additional data
     */
    private fun ensureBufferCapacity(additionalSize: Int) {
        if (bufferSize + additionalSize > buffer.size) {
            val newSize = maxOf(buffer.size * 2, bufferSize + additionalSize)
            val newBuffer = UByteArray(newSize)
            buffer.copyInto(newBuffer, 0, 0, bufferSize)
            buffer = newBuffer
        }
    }

    /**
     * Ensure queue has enough capacity for additional data
     */
    private fun ensureQueuedCapacity(additionalSize: Int) {
        if (queuedSize + additionalSize > queued.size) {
            val newSize = maxOf(queued.size * 2, queuedSize + additionalSize)
            val newQueued = UByteArray(newSize)
            queued.copyInto(newQueued, 0, 0, queuedSize)
            queued = newQueued
        }
    }

    /**
     * Encode text based on current codepage settings
     */
    private fun encode(value: String): UByteArray {
        if (codepage != "auto") {
            return CodepageEncoder.encode(value, codepage)
        }

        val codepages = codepageMappings[options.codepageMapping.toString()] ?: emptyMap()
        val fragments = CodepageEncoder.autoEncode(value, options.codepageCandidates)

        var length = 0
        for (fragment in fragments) {
            length += 3 + fragment.bytes.size
        }

        val result = UByteArray(length)
        var index = 0

        for (fragment in fragments) {
            result[index] = 0x1Bu
            result[index + 1] = 0x74u
            result[index + 2] = codepages[fragment.codepage]?.toUByte() ?: 0u
            fragment.bytes.copyInto(result, index + 3)
            index += 3 + fragment.bytes.size
            state.codepage = codepages[fragment.codepage] ?: 0
        }

        return result
    }

    /**
     * Add data to the command queue
     */
    private fun queue(data: UByteArray) {
        ensureQueuedCapacity(data.size)
        data.copyInto(queued, queuedSize)
        queuedSize += data.size
    }

    /**
     * Flush queued commands to the buffer
     */
    private fun flush() {
        if (embedded) {
            val indent = options.width - cursor

            when (state.align) {
                "left" -> {
                    val spaces = UByteArray(indent) { 0x20u }
                    queue(spaces)
                }
                "center" -> {
                    val remainder = indent % 2
                    val halfIndent = indent / 2

                    if (halfIndent > 0) {
                        val spaces = UByteArray(halfIndent) { 0x20u }
                        queue(spaces)
                    }

                    if (halfIndent + remainder > 0) {
                        val spaces = UByteArray(halfIndent + remainder) { 0x20u }
                        val temp = UByteArray(queuedSize + spaces.size)
                        spaces.copyInto(temp)
                        queued.copyInto(temp, spaces.size, 0, queuedSize)
                        queued = temp
                        queuedSize += spaces.size
                    }
                }
                "right" -> {
                    val spaces = UByteArray(indent) { 0x20u }
                    val temp = UByteArray(queuedSize + spaces.size)
                    spaces.copyInto(temp)
                    queued.copyInto(temp, spaces.size, 0, queuedSize)
                    queued = temp
                    queuedSize += spaces.size
                }
            }
        }

        ensureBufferCapacity(queuedSize)
        queued.copyInto(buffer, bufferSize, 0, queuedSize)
        bufferSize += queuedSize
        queuedSize = 0
        cursor = 0
    }

    /**
     * Word wrap text to fit within printer width
     */
    private fun wrap(value: String, position: Int? = null): List<String> {
        if (position != null || options.wordWrap) {
            val width = position ?: options.width
            val lines = mutableListOf<String>()
            val indent = " ".repeat(cursor)
            val text = indent + value

            // Simple wrap algorithm
            var currentLine = ""
            var currentLineLength = 0
            val words = text.split(" ")

            for (word in words) {
                if (currentLineLength + word.length + 1 <= width) {
                    // Word fits on current line
                    if (currentLine.isNotEmpty()) {
                        currentLine += " "
                        currentLineLength += 1
                    }
                    currentLine += word
                    currentLineLength += word.length
                } else {
                    // Word doesn't fit, start new line
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                    }
                    currentLine = word
                    currentLineLength = word.length
                }
            }

            // Add last line
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine)
            }

            // Remove indent from first line
            if (lines.isNotEmpty() && cursor > 0) {
                lines[0] = lines[0].substring(cursor)
            }

            return lines
        }

        return listOf(value)
    }

    /**
     * Restore state after embedded content
     */
    private fun restoreState() {
        bold(state.bold)
        italic(state.italic)
        underline(state.underline)
        invert(state.invert)
        queue(ubyteArrayOf(0x1Bu, 0x74u, state.codepage.toUByte()))
    }

    /**
     * Initialize the printer
     */
    fun initialize(): EscPosEncoder {
        queue(ubyteArrayOf(0x1Bu, 0x40u))
        flush()
        return this
    }

    /**
     * Set the codepage for subsequent text
     */
    fun codepage(codepage: String): EscPosEncoder {
        if (codepage == "auto") {
            this.codepage = codepage
            return this
        }

        if (!CodepageEncoder.supports(codepage)) {
            throw IllegalArgumentException("Unknown codepage: $codepage")
        }

        val codepages = codepageMappings[options.codepageMapping.toString()] ?: emptyMap()

        if (codepages.containsKey(codepage)) {
            this.codepage = codepage
            state.codepage = codepages[codepage]!!
            queue(ubyteArrayOf(0x1Bu, 0x74u, codepages[codepage]!!.toUByte()))
        } else {
            throw IllegalArgumentException("Codepage not supported by printer: $codepage")
        }

        return this
    }

    /**
     * Print text
     */
    fun text(value: String, wrap: Int? = null): EscPosEncoder {
        val lines = wrap(value, wrap)

        for (i in lines.indices) {
            val bytes = encode(lines[i])
            if (options.width == 46 && state.align != "center") {
                queue(ubyteArrayOf(0x20u, 0x20u))  // K80 margin 2 spaces
            }

            queue(bytes)

            cursor += lines[i].length * state.width

            if (!embedded) {
                cursor %= options.width
            }

            if (i < lines.size - 1) {
                newline()
            }
        }

        return this
    }

    /**
     * Print a newline
     */
    fun newline(): EscPosEncoder {
        flush()
        queue(ubyteArrayOf(0x0Au, 0x0Du))

        if (embedded) {
            restoreState()
        }

        return this
    }

    /**
     * Print a line of text followed by a newline
     */
    fun line(value: String, wrap: Int? = null): EscPosEncoder {
        text(value, wrap)
        newline()
        return this
    }

    /**
     * Set underline mode
     */
    fun underline(value: Boolean? = null): EscPosEncoder {
        val newValue = value ?: !state.underline
        state.underline = newValue
        queue(ubyteArrayOf(0x1Bu, 0x2Du, if (newValue) 1u else 0u))
        return this
    }

    /**
     * Set italic mode
     */
    fun italic(value: Boolean? = null): EscPosEncoder {
        val newValue = value ?: !state.italic
        state.italic = newValue
        queue(ubyteArrayOf(0x1Bu, 0x34u, if (newValue) 1u else 0u))
        return this
    }

    /**
     * Set bold mode
     */
    fun bold(value: Boolean? = null): EscPosEncoder {
        val newValue = value ?: !state.bold
        state.bold = newValue
        queue(ubyteArrayOf(0x1Bu, 0x45u, if (newValue) 1u else 0u))
        return this
    }

    /**
     * Set text width multiplier
     */
    fun width(width: Int = 1): EscPosEncoder {
        if (width !in 1..8) {
            throw IllegalArgumentException("Width must be between 1 and 8")
        }

        state.width = width
        queue(ubyteArrayOf(
            0x1Du, 0x21u,
            ((state.height - 1) or ((state.width - 1) shl 4)).toUByte()
        ))
        return this
    }

    /**
     * Set text height multiplier
     */
    fun height(height: Int = 1): EscPosEncoder {
        if (height !in 1..8) {
            throw IllegalArgumentException("Height must be between 1 and 8")
        }

        state.height = height
        queue(ubyteArrayOf(
            0x1Du, 0x21u,
            ((state.height - 1) or ((state.width - 1) shl 4)).toUByte()
        ))
        return this
    }

    /**
     * Set inverted mode (white on black)
     */
    fun invert(value: Boolean? = null): EscPosEncoder {
        val newValue = value ?: !state.invert
        state.invert = newValue
        queue(ubyteArrayOf(0x1Du, 0x42u, if (newValue) 1u else 0u))
        return this
    }

    /**
     * Set text size
     */
    fun size(value: String): EscPosEncoder {
        val sizeValue: UByte = if (value == "small") 0x01u else 0x00u
        queue(ubyteArrayOf(0x1Bu, 0x4Du, sizeValue))
        return this
    }

    /**
     * Set text alignment
     */
    fun align(value: String): EscPosEncoder {
        val alignments: Map<String, UByte> = mapOf(
            "left" to 0x00u,
            "center" to 0x01u,
            "right" to 0x02u
        )

        if (value in alignments) {
            state.align = value
            if (!embedded) {
                queue(ubyteArrayOf(0x1Bu, 0x61u, alignments[value]!!))
            }
        } else {
            throw IllegalArgumentException("Unknown alignment: $value")
        }

        return this
    }

    /**
     * Print an image from base64 encoded data
     */
    fun image(base64: String): EscPosEncoder {
        // Determine max width based on printer model
        val maxWidthDots = when (options.width) {
            46 -> 576  // 80mm printer: 576 dots
            32 -> 464  // 58mm printer: 464 dots
            else -> 576
        }

        // Load and scale image optimized for thermal printer
        val bitmap = Image.loadForThermalPrinter(base64, maxWidthDots)
        val raster = bitmap.toRaster()

        // The raster data already has the correct bytes per line calculation
        val bytesPerLine = ceil(raster.width / 8.0).toInt()

        // Command header size (8 bytes) + data size
        val imageData = UByteArray(8 + raster.data.size)

        // GS v 0 0 command - standard ESC/POS raster image format
        imageData[0] = 0x1Du  // GS
        imageData[1] = 0x76u  // v
        imageData[2] = 0x30u  // 0
        imageData[3] = 0u     // 0

        // Write width in bytes (not dots) - little endian (LSB first)
        imageData[4] = bytesPerLine.toUByte()
        imageData[5] = 0u  // Most printers only support width < 256 bytes

        // Write height in dots - little endian (LSB first)
        imageData[6] = (raster.height and 0xFF).toUByte()
        imageData[7] = (raster.height shr 8).toUByte()

        // Write raster data
        raster.data.copyInto(imageData, 8)

        queue(imageData)
        flush()
        return this
    }

    /**
     * Cut the paper
     */
    fun cut(value: String? = null): EscPosEncoder {
        if (embedded) {
            throw IllegalStateException("Cut is not supported in table cells or boxes")
        }

        val data: UByte = if (value == "partial") 0x01u else 0x00u
        queue(ubyteArrayOf(0x1Du, 0x56u, data))
        return this
    }

    /**
     * Activate the cash drawer
     */
    fun pulse(device: Int = 0, on: Int = 100, off: Int = 500): EscPosEncoder {
        if (embedded) {
            throw IllegalStateException("Pulse is not supported in table cells or boxes")
        }

        val onTime = min(500, (on / 2.0).roundToInt())
        val offTime = min(500, (off / 2.0).roundToInt())

        queue(ubyteArrayOf(
            0x1Bu, 0x70u,
            if (device != 0) 1u else 0u,
            (onTime and 0xFF).toUByte(),
            (offTime and 0xFF).toUByte()
        ))

        return this
    }

    /**
     * Add raw binary data
     */
    fun raw(data: UByteArray): EscPosEncoder {
        queue(data)
        return this
    }

    /**
     * Get the encoded command data
     */
    fun encode(): ByteArray {
        flush()
        val result = ByteArray(bufferSize)
        for (i in 0 until bufferSize) {
            result[i] = buffer[i].toByte()
        }
        return result
    }

    companion object {
        private val codepageMappings = mapOf(
            PrinterType.EPSON.toString() to mapOf(
                "cp437" to 0x00,
                "shiftjis" to 0x01,
                "cp850" to 0x02,
                "cp860" to 0x03,
                "cp863" to 0x04,
                "cp865" to 0x05,
                "cp851" to 0x0b,
                "cp853" to 0x0c,
                "cp857" to 0x0d,
                "cp737" to 0x0e,
                "iso88597" to 0x0f,
                "windows1252" to 0x10,
                "cp866" to 0x11,
                "cp852" to 0x12,
                "cp858" to 0x13,
                "cp720" to 0x20,
                "cp775" to 0x21,
                "cp855" to 0x22,
                "cp861" to 0x23,
                "cp862" to 0x24,
                "cp864" to 0x25,
                "cp869" to 0x26,
                "iso88592" to 0x27,
                "iso885915" to 0x28,
                "cp1098" to 0x29,
                "cp1118" to 0x2a,
                "cp1119" to 0x2b,
                "cp1125" to 0x2c,
                "windows1250" to 0x2d,
                "windows1251" to 0x2e,
                "windows1253" to 0x2f,
                "windows1254" to 0x30,
                "windows1255" to 0x31,
                "windows1256" to 0x32,
                "windows1257" to 0x33,
                "windows1258" to 0x34,
                "rk1048" to 0x35
            ),
            PrinterType.ZJIANG.toString() to mapOf(
                "cp437" to 0x00,
                "shiftjis" to 0x01,
                "cp850" to 0x02,
                "cp860" to 0x03,
                "cp863" to 0x04,
                "cp865" to 0x05,
                "windows1252" to 0x10,
                "cp866" to 0x11,
                "cp852" to 0x12,
                "cp858" to 0x13,
                "windows1255" to 0x20,
                "cp861" to 0x38,
                "cp855" to 0x3c,
                "cp857" to 0x3d,
                "cp862" to 0x3e,
                "cp864" to 0x3f,
                "cp737" to 0x40,
                "cp851" to 0x41,
                "cp869" to 0x42,
                "cp1119" to 0x44,
                "cp1118" to 0x45,
                "windows1250" to 0x48,
                "windows1251" to 0x49,
                "cp3840" to 0x4a,
                "cp3843" to 0x4c,
                "cp3844" to 0x4d,
                "cp3845" to 0x4e,
                "cp3846" to 0x4f,
                "cp3847" to 0x50,
                "cp3848" to 0x51,
                "cp2001" to 0x53,
                "cp3001" to 0x54,
                "cp3002" to 0x55,
                "cp3011" to 0x56,
                "cp3012" to 0x57,
                "cp3021" to 0x58,
                "cp3041" to 0x59,
                "windows1253" to 0x5a,
                "windows1254" to 0x5b,
                "windows1256" to 0x5c,
                "cp720" to 0x5d,
                "windows1258" to 0x5e,
                "cp775" to 0x5f
            ),
            PrinterType.BIXOLON.toString() to mapOf(
                "cp437" to 0x00,
                "shiftjis" to 0x01,
                "cp850" to 0x02,
                "cp860" to 0x03,
                "cp863" to 0x04,
                "cp865" to 0x05,
                "cp851" to 0x0b,
                "cp858" to 0x13
            ),
            PrinterType.STAR.toString() to mapOf(
                "cp437" to 0x00,
                "shiftjis" to 0x01,
                "cp850" to 0x02,
                "cp860" to 0x03,
                "cp863" to 0x04,
                "cp865" to 0x05,
                "windows1252" to 0x10,
                "cp866" to 0x11,
                "cp852" to 0x12,
                "cp858" to 0x13
            ),
            PrinterType.CITIZEN.toString() to mapOf(
                "cp437" to 0x00,
                "shiftjis" to 0x01,
                "cp850" to 0x02,
                "cp860" to 0x03,
                "cp863" to 0x04,
                "cp865" to 0x05,
                "cp852" to 0x12,
                "cp866" to 0x11,
                "cp857" to 0x08,
                "windows1252" to 0x10,
                "cp858" to 0x13,
                "cp864" to 0x28
            ),
            PrinterType.LEGACY.toString() to mapOf(
                "cp437" to 0x00,
                "cp737" to 0x40,
                "cp850" to 0x02,
                "cp775" to 0x5f,
                "cp852" to 0x12,
                "cp855" to 0x3c,
                "cp857" to 0x3d,
                "cp858" to 0x13,
                "cp860" to 0x03,
                "cp861" to 0x38,
                "cp862" to 0x3e,
                "cp863" to 0x04,
                "cp864" to 0x1c,
                "cp865" to 0x05,
                "cp866" to 0x11,
                "cp869" to 0x42,
                "cp936" to 0xff,
                "cp949" to 0xfd,
                "cp950" to 0xfe,
                "cp1252" to 0x10,
                "iso88596" to 0x16,
                "shiftjis" to 0xfc,
                "windows874" to 0x1e,
                "windows1250" to 0x48,
                "windows1251" to 0x49,
                "windows1252" to 0x47,
                "windows1253" to 0x5a,
                "windows1254" to 0x5b,
                "windows1255" to 0x20,
                "windows1256" to 0x5c,
                "windows1257" to 0x19,
                "windows1258" to 0x5e
            )
        )
    }
}