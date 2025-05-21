import ExpoModulesCore

public class ExpoEscposModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoEscpos")

    Function("renderImages") { (config: [String: Any], imageBase64s: [String]) -> [UInt8] in
      let model = config["model"] as? String ?? "80"
      let receiptModel = model == "58" ? ReceiptModel.model58 : ReceiptModel.model80
      let receiptConfig = ReceiptConfig(model: receiptModel)
      return receiptImages(config: receiptConfig, imageBase64s: imageBase64s)
    }
  }
}
