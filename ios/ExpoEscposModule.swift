import ExpoModulesCore
import WebKit

public class ExpoEscposModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoEscpos")

    Function("renderImages") {
      (config: [String: Any], imageBase64s: [String]) -> [UInt8] in
      let receiptConfig = ReceiptOptions(dictionary: config)
      return receiptImages(config: receiptConfig, imageBase64s: imageBase64s)
    }

    AsyncFunction("renderHtmlToImages") {
      (html: String, config: [String: Any], promise: Promise) in
      Task {
        do {
          let pngImages = try await StylePreservingWebViewDelegate.renderHtmlToImages(config: config, html: html)
          let receiptConfig = ReceiptOptions(dictionary: config)
          let receiptData = receiptPngImages(config: receiptConfig, images: pngImages)
          promise.resolve(receiptData)
        } catch {
          promise.reject(
            "RENDER_IMAGES_ERROR",
            "Failed to generate image preview: \(error.localizedDescription)")
        }
      }
    }
  }
}
