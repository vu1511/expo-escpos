package expo.modules.escpos

import java.text.Normalizer

@OptIn(ExperimentalUnsignedTypes::class)
data class Fragment(
    val codepage: String,
    val bytes: UByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fragment

        if (codepage != other.codepage) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = codepage.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

/**
 * Handles encoding of text strings into various codepages
 */
@OptIn(ExperimentalUnsignedTypes::class)
object CodepageEncoder {
    /**
     * Get list of supported codepages
     *
     * @return List of supported codepage names
     */
    fun getEncodings(): List<String> {
        return CodePageDefinitions.definitions.keys.toList()
    }

    /**
     * Check if a codepage is supported
     */
    fun supports(codepage: String): Boolean {
        return CharsetMapping.isSupported(codepage)
    }

    /**
     * Encode a string into a specific codepage
     */
    fun encode(text: String, codepage: String): UByteArray {
        // Special handling for Vietnamese windows-1258 codepage
        if (codepage == "windows1258") {
            return encodeVietnamese(text)
        }

        // Use ASCII for standard ASCII characters
        if (codepage == "ascii") {
            val result = UByteArray(text.length)
            for (i in text.indices) {
                val c = text[i].code
                result[i] = if (c < 128) c.toUByte() else 0x3fu // '?' for non-ASCII
            }
            return result
        }

        // Otherwise use the appropriate charset
        val charset = CharsetMapping.getCharset(codepage)
        val encoded = text.toByteArray(charset)
        val result = UByteArray(encoded.size)

        for (i in encoded.indices) {
            result[i] = encoded[i].toUByte()
        }

        return result
    }

    /**
     * Special encoding function for Vietnamese text using windows-1258
     */
    private fun encodeVietnamese(text: String): UByteArray {
        // Use the specific windows-1258 charset
        val charset = CharsetMapping.getCharset("windows1258")
        val encoded = text.toByteArray(charset)
        val result = UByteArray(encoded.size)

        for (i in encoded.indices) {
            result[i] = encoded[i].toUByte()
        }

        return result
    }

    /**
     * Automatically encode a string choosing the most appropriate codepage
     * for each character based on provided codepage candidates
     */
    fun autoEncode(text: String, candidates: List<String>): List<EncodingFragment> {
        val result = mutableListOf<EncodingFragment>()
        var startIndex = 0
        var currentCodepage = candidates.first()
        var i = 0

        while (i < text.length) {
            val char = text[i]

            // For ASCII characters (0-127), we don't need to change codepage
            if (char.code < 128) {
                i++
                continue
            }

            // Find the best encoding for this character
            val bestEncoding = CharsetDetector.getBestEncoding(char, candidates)

            if (bestEncoding == null) {
                // No suitable encoding found, try Vietnamese normalization
                val normalizedChar = removeVNCharacters(char.toString())
                if (normalizedChar.isNotEmpty() && normalizedChar[0].code != char.code) {
                    // If normalized character is different, add current fragment and start a new one
                    if (i > startIndex) {
                        val fragment = EncodingFragment(
                            bytes = encode(text.substring(startIndex, i), currentCodepage),
                            codepage = currentCodepage
                        )
                        result.add(fragment)
                    }

                    // Add the normalized character as its own fragment
                    val normalizedFragment = EncodingFragment(
                        bytes = UByteArray(1) { normalizedChar[0].code.toUByte() },
                        codepage = currentCodepage
                    )
                    result.add(normalizedFragment)

                    // Start a new fragment after this character
                    startIndex = i + 1
                }
                i++
                continue
            }

            if (bestEncoding != currentCodepage) {
                // Add the fragment using the current codepage before changing
                if (i > startIndex) {
                    val fragment = EncodingFragment(
                        bytes = encode(text.substring(startIndex, i), currentCodepage),
                        codepage = currentCodepage
                    )
                    result.add(fragment)
                }

                // Switch to the new codepage
                currentCodepage = bestEncoding
                startIndex = i
            }

            i++
        }

        // Add the final fragment
        if (startIndex < text.length) {
            val fragment = EncodingFragment(
                bytes = encode(text.substring(startIndex), currentCodepage),
                codepage = currentCodepage
            )
            result.add(fragment)
        }

        return result
    }

    /**
     * Remove Vietnamese diacritical marks and normalize characters
     */
    private fun removeVNCharacters(str: String): String {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
            .replace(Regex("[\u0300-\u036f]"), "")
            .replace("đ", "d")
            .replace("Đ", "D")
    }
}

data class TestString(
    val language: String,
    val string: String
)