package expo.modules.escpos

import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoEscposModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ExpoEscpos")

    Function("renderImages") { config: Map<String, Any>, imageBase64s: List<String> ->
      val model = (config["model"] as? String) ?: "80"
      val receiptModel = if (model == "58") ReceiptModel.MODEL_58 else ReceiptModel.MODEL_80
      val receiptConfig = ReceiptConfig(model = receiptModel)
      receiptImages(receiptConfig, imageBase64s)
    }

    AsyncFunction("renderHtmlToImages") { html: String, config: Map<String, Any>, promise: Promise ->
      try {
        val model = (config["model"] as? String) ?: "80"
        val maxHeightToBreak = (config["maxHeightToBreak"] as? Number)?.toInt() ?: 1600
        val receiptModel = if (model == "58") ReceiptModel.MODEL_58 else ReceiptModel.MODEL_80
        val printerWidth = if (receiptModel == ReceiptModel.MODEL_58) 384 else 576

        HtmlToImageConverter.renderHtmlToImages(appContext.reactContext!!, html, printerWidth, maxHeightToBreak) { result ->
          result.fold(
            onSuccess = { imageDataList ->
              val receiptConfig = ReceiptConfig(model = receiptModel)
              val receiptData = receiptPngImages(receiptConfig, imageDataList)
              promise.resolve(receiptData)
            },
            onFailure = { error ->
              promise.reject("RENDER_IMAGES_ERROR", "Failed to generate image preview: ${error.message}", error)
            }
          )
        }
      } catch (e: Exception) {
        promise.reject("RENDER_IMAGES_ERROR", "Failed to generate image preview: ${e.message}", e)
      }
    }
  }
}