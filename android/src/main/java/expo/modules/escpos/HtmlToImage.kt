package expo.modules.escpos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.min

object HtmlToImage {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

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
                webView.settings.loadWithOverviewMode = true
                webView.settings.useWideViewPort = true
                webView.setBackgroundColor(Color.WHITE)

                // Set the initial size
                webView.layout(0, 0, width, 1)

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        val heightScript = "(function() { return document.body.scrollHeight; })();"

                        webView.evaluateJavascript(heightScript) { heightString ->
                            val height = heightString.toFloatOrNull()

                            if (height != null) {
                                val density = webView.resources.displayMetrics.density
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
                }

                // Load the HTML
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            } catch (e: Exception) {
                callback(Result.failure(e))
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
