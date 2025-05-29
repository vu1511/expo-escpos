package expo.modules.escpos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import kotlin.math.ceil

/**
 * Represents a processed image raster ready for printing
 */
@OptIn(ExperimentalUnsignedTypes::class)
data class ImageRaster @OptIn(ExperimentalUnsignedTypes::class) constructor(
    val width: Int,
    val height: Int,
    val data: UByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageRaster

        if (width != other.width) return false
        if (height != other.height) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + data.contentHashCode()
        return result
    }
}

/**
 * Handles image processing for ESC/POS printing
 */
@OptIn(ExperimentalUnsignedTypes::class)
object Image {
    /**
     * Load an image from a base64 encoded string
     */
    fun load(base64: String): Bitmap {
        // Clean the base64 string
        val cleanBase64 = base64.trim()
            .replace("\\s".toRegex(), "")  // Remove any whitespace

        // Remove any prefix (like "data:image/png;base64,")
        val base64Data = if (cleanBase64.contains(",")) {
            cleanBase64.split(",")[1]
        } else {
            cleanBase64
        }

        try {
            val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: throw IllegalArgumentException("Failed to decode image data")
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid base64 image data: ${e.message}")
        }
    }

    /**
     * Convert a bitmap to a 1-bit black and white bitmap using dithering
     */
    private fun ditherBitmap(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height

        // Create a new bitmap for 1-bit black and white
        val bwBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Convert each pixel to black or white using Floyd-Steinberg dithering
        val pixels = IntArray(width * height)
        original.getPixels(pixels, 0, width, 0, 0, width, height)

        val errors = Array(height) { FloatArray(width) }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val pixel = pixels[index]

                // Calculate the grayscale value (0.0 - 1.0)
                val alpha = Color.alpha(pixel) / 255f
                val red = Color.red(pixel) / 255f
                val green = Color.green(pixel) / 255f
                val blue = Color.blue(pixel) / 255f
                var gray = 0.299f * red + 0.587f * green + 0.114f * blue
                gray *= alpha

                // Add the error from previous pixels
                gray += errors[y][x]

                // Determine if the pixel should be black or white
                val bw = if (gray > 0.5f) 1.0f else 0.0f

                // Calculate the error
                val error = gray - bw

                // Distribute the error to neighboring pixels
                if (x + 1 < width) {
                    errors[y][x + 1] += error * 7 / 16
                }
                if (y + 1 < height) {
                    if (x > 0) {
                        errors[y + 1][x - 1] += error * 3 / 16
                    }
                    errors[y + 1][x] += error * 5 / 16
                    if (x + 1 < width) {
                        errors[y + 1][x + 1] += error * 1 / 16
                    }
                }

                // Set the pixel color
                pixels[index] = if (bw > 0.5f) Color.WHITE else Color.BLACK
            }
        }

        bwBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bwBitmap
    }

    /**
     * Create a thermal printer optimized bitmap from base64
     */
    fun loadForThermalPrinter(base64: String, maxWidthDots: Int = 576): Bitmap {
        val originalBitmap = load(base64)

        // Scale down if the image is too wide for thermal printer
        return if (originalBitmap.width > maxWidthDots) {
            val scaleFactor = maxWidthDots.toFloat() / originalBitmap.width
            val newHeight = (originalBitmap.height * scaleFactor).toInt()
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidthDots, newHeight, true)
            originalBitmap.recycle() // Free memory
            scaledBitmap
        } else {
            originalBitmap
        }
    }

    /**
     * Convert a bitmap to ESC/POS raster format
     */
    fun Bitmap.toRaster(): ImageRaster {
        // First dither to pure black and white
        val bwBitmap = ditherBitmap(this)

        // Calculate dimensions
        val width = bwBitmap.width
        val height = bwBitmap.height
        val bytesPerLine = ceil(width / 8.0).toInt()
        val rasterData = UByteArray(bytesPerLine * height)

        // Create raster data
        for (y in 0 until height) {
            for (x in 0 until width) {
                // Get pixel color (1 for black, 0 for white) - ESC/POS expects BLACK = 1
                val color = if (bwBitmap.getPixel(x, y) == Color.BLACK) 1 else 0

                // Calculate position in the raster data
                val bytePos = y * bytesPerLine + x / 8
                val bitPos = 7 - (x % 8)  // MSB first

                if (color == 1) {
                    // Set the bit for black pixels
                    rasterData[bytePos] = (rasterData[bytePos].toInt() or (1 shl bitPos)).toUByte()
                }
            }
        }

        bwBitmap.recycle() // Free memory

        // Return the actual width in dots, not bytesPerLine * 8
        return ImageRaster(width, height, rasterData)
    }
}