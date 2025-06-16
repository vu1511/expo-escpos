package expo.modules.escpos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.min

object HtmlToImageConverter {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private fun generateQrCode(text: String, size: Int = 150, errorCorrection: String = "M"): ByteArray? {
        return try {
            val hints = hashMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.MARGIN] = 0

            when (errorCorrection.uppercase()) {
                "L" -> hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
                "M" -> hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
                "Q" -> hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.Q
                "H" -> hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
                else -> hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
            }

            val writer = MultiFormatWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints)
            
            val bitmap = createBitmapFromBitMatrix(bitMatrix)
            bitmapToByteArray(bitmap)
        } catch (e: Exception) {
            null
        }
    }
    
    private fun generateBarcode(text: String, width: Int = 200, height: Int = 100): ByteArray? {
        return try {
            val hints = hashMapOf<EncodeHintType, Any>()
            hints[EncodeHintType.MARGIN] = 0

            val writer = MultiFormatWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height, hints)
            
            val bitmap = createBitmapFromBitMatrix(bitMatrix)
            bitmapToByteArray(bitmap)
        } catch (e: Exception) {
            null
        }
    }
    
    private fun createBitmapFromBitMatrix(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        
        return bitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        outputStream.close()
        bitmap.recycle()
        return byteArray
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun renderHtmlToImages(
        context: Context,
        html: String,
        width: Int,
        maxHeightToBreak: Int,
        callback: (Result<List<ByteArray>>) -> Unit
    ) {
        // Must run WebView on main thread
        Handler(Looper.getMainLooper()).post {
            try {
                val webView = WebView(context)
                webView.settings.javaScriptEnabled = true
                webView.settings.useWideViewPort = true
                webView.setBackgroundColor(Color.WHITE)

                // Set the initial size
                webView.layout(0, 0, width, 1)

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        // First convert barcodes and QR codes to images
                        val convertScript = """
                        function convertCodeElements() {
                            const qrElements = document.querySelectorAll('[data-type="qrcode"]');
                            const barcodeElements = document.querySelectorAll('[data-type="barcode"]');
                            const allElements = [];
                            
                            qrElements.forEach((element, index) => {
                                const text = element.getAttribute('data-text') || element.textContent || '';
                                const size = element.getAttribute('data-size') || '150';
                                const errorCorrection = element.getAttribute('data-error-correction') || 'M';
                                
                                allElements.push({
                                    type: 'qrcode',
                                    text: text,
                                    size: parseInt(size),
                                    errorCorrection: errorCorrection,
                                    elementId: 'qr-code-' + index
                                });
                                
                                element.setAttribute('data-processing-id', 'qr-code-' + index);
                            });
                            
                            barcodeElements.forEach((element, index) => {
                                const text = element.getAttribute('data-text') || element.textContent || '';
                                const width = element.getAttribute('data-width') || '200';
                                const height = element.getAttribute('data-height') || `${'$'}{width / 2}`;
                                
                                allElements.push({
                                    type: 'barcode',
                                    text: text,
                                    width: parseInt(width),
                                    height: parseInt(height),
                                    elementId: 'barcode-' + index
                                });
                                
                                element.setAttribute('data-processing-id', 'barcode-' + index);
                            });
                            
                            return allElements;
                        }
                        convertCodeElements();
                        """.trimIndent()

                        webView.evaluateJavascript(convertScript) { elementsDataJson ->
                            // Process the elements data and generate images
                            coroutineScope.launch {
                                try {
                                    val processedElements = processCodeElements(elementsDataJson)

                                    withContext(Dispatchers.Main) {
                                        updateAllElements(webView, processedElements) {
                                            // After updating elements, get final content height
                                            getContentHeightAndResize(webView, width, maxHeightToBreak, callback)
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        webView.destroy()
                                    }
                                    callback(Result.failure(e))
                                }
                            }
                        }
                    }
                }

                // Load the HTML
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }
    
    private suspend fun processCodeElements(elementsDataJson: String): Map<String, String> {
        return withContext(Dispatchers.Default) {
            val processedElements = mutableMapOf<String, String>()

            try {
                // Parse the JSON-like string (simplified parsing for the data structure we expect)
                val elementsData = parseElementsData(elementsDataJson)

                for (elementData in elementsData) {
                    val type = elementData["type"] as? String ?: continue
                    val text = elementData["text"] as? String ?: continue
                    val elementId = elementData["elementId"] as? String ?: continue
                    
                    val imageData = when (type) {
                        "qrcode" -> {
                            val size = (elementData["size"] as? Int) ?: 150
                            val errorCorrection = (elementData["errorCorrection"] as? String) ?: "M"
                            generateQrCode(text, size, errorCorrection)
                        }
                        "barcode" -> {
                            val width = (elementData["width"] as? Int) ?: 200
                            val height = (elementData["height"] as? Int) ?: 100
                            generateBarcode(text, width, height)
                        }
                        else -> null
                    }
                    
                    imageData?.let {
                        val base64String = Base64.encodeToString(it, Base64.NO_WRAP)
                        processedElements[elementId] = base64String
                    }
                }
            } catch (e: Exception) {
                // Log error but continue with empty map
            }
            
            processedElements
        }
    }
    
    private fun parseElementsData(jsonString: String): List<Map<String, Any>> {
        val elements = mutableListOf<Map<String, Any>>()
        
        val cleanJson = jsonString.trim().removeSurrounding("[", "]")
        
        if (cleanJson.isBlank()) return elements
        
        val objectStrings = cleanJson.split("},{").map {
            if (!it.startsWith("{")) "{$it" else it
        }.map {
            if (!it.endsWith("}")) "$it}" else it
        }
        
        for (objStr in objectStrings) {
            try {
                val element = mutableMapOf<String, Any>()
                
                // Extract key-value pairs
                val content = objStr.removeSurrounding("{", "}")
                val pairs = content.split(",")
                
                for (pair in pairs) {
                    val keyValue = pair.split(":")
                    if (keyValue.size == 2) {
                        val key = keyValue[0].trim().removeSurrounding("\"")
                        val value = keyValue[1].trim()
                        
                        when {
                            value.startsWith("\"") && value.endsWith("\"") -> {
                                element[key] = value.removeSurrounding("\"")
                            }
                            value.toIntOrNull() != null -> {
                                element[key] = value.toInt()
                            }
                            else -> {
                                element[key] = value.removeSurrounding("\"")
                            }
                        }
                    }
                }
                
                if (element.isNotEmpty()) {
                    elements.add(element)
                }
            } catch (e: Exception) {
                // Skip malformed objects
            }
        }
        
        return elements
    }
    
    private fun updateAllElements(webView: WebView, processedElements: Map<String, String>, onComplete: () -> Unit) {
        if (processedElements.isEmpty()) {
            onComplete()
            return
        }
        
        val updateScript = processedElements.map { (elementId, base64) ->
            val safeElementId = elementId.replace("-", "_")
            """
            const element$safeElementId = document.querySelector('[data-processing-id="$elementId"]');
            if (element$safeElementId) {
                const img = document.createElement('img');
                img.src = 'data:image/png;base64,$base64';
                img.style.display = 'block';
                element$safeElementId.innerHTML = '';
                element$safeElementId.appendChild(img);
            }
            """.trimIndent()
        }.joinToString("\n")
        
        webView.evaluateJavascript("(function() { $updateScript })();") {
            onComplete()
        }
    }
    
    private fun getContentHeightAndResize(
        webView: WebView, 
        width: Int, 
        maxHeightToBreak: Int, 
        callback: (Result<List<ByteArray>>) -> Unit
    ) {
        val density = webView.resources.displayMetrics.density
        val zoomPercentage = (1.0 / density * 100).toInt()
        val jsScript = """
            (function() {
                document.body.style.zoom = '$zoomPercentage%';
                return document.body.scrollHeight;
            })();
        """.trimIndent()

        webView.evaluateJavascript(jsScript) { heightString ->
            val height = heightString.toFloatOrNull()

            if (height != null) {
                val contentHeight = (height * density).toInt()

                try {
                    // Resize WebView to content dimensions
                    webView.layout(0, 0, width, contentHeight)

                    // Wait a bit more for rendering to complete after resize
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Process bitmap in background using coroutines
                        coroutineScope.launch {
                            try {
                                val imageParts = withContext(Dispatchers.Default) {
                                    processBitmap(webView, width, maxHeightToBreak)
                                }

                                // Clean up WebView on main thread
                                withContext(Dispatchers.Main) {
                                    webView.destroy()
                                }

                                if (imageParts.isEmpty()) {
                                    callback(Result.failure(Exception("Failed to create image slices")))
                                } else {
                                    callback(Result.success(imageParts))
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    webView.destroy()
                                }
                                callback(Result.failure(e))
                            }
                        }
                    }, 100) // Additional wait after resize
                } catch (e: Exception) {
                    webView.destroy()
                    callback(Result.failure(Exception("Failed to get content height: ${e.message}")))
                }
            }
        }
    }

    private suspend fun processBitmap(webView: WebView, width: Int, maxHeightToBreak: Int): List<ByteArray> {
        return withContext(Dispatchers.IO) {
            val bitmap = Bitmap.createBitmap(webView.width, webView.height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            
            withContext(Dispatchers.Main) {
                webView.draw(canvas)
            }

            val imageParts = mutableListOf<ByteArray>()
            val totalHeight = bitmap.height
            var currentY = 0

            try {
                while (currentY < totalHeight) {
                    val sliceHeight = min(maxHeightToBreak, totalHeight - currentY)

                    // Create a bitmap for this slice
                    val sliceBitmap = Bitmap.createBitmap(
                        bitmap, 0, currentY, width, sliceHeight
                    )

                    // Convert to PNG
                    val outputStream = ByteArrayOutputStream()
                    sliceBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    imageParts.add(outputStream.toByteArray())

                    // Clean up
                    sliceBitmap.recycle()
                    outputStream.close()

                    // Move to next slice
                    currentY += sliceHeight
                }
            } finally {
                // Clean up main bitmap
                bitmap.recycle()
            }

            imageParts
        }
    }
}
