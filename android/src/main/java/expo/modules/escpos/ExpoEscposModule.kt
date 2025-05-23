package expo.modules.escpos

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

  }
}
