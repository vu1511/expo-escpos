import Foundation

public enum PrinterModel: String {
  case model58 = "58"
  case model80 = "80"
}

public struct ReceiptOptions {
  public let model: PrinterModel

  public init(dictionary: [String: Any]) {
    let modelString = dictionary["model"] as? String ?? "80"
    self.model = PrinterModel(rawValue: modelString) ?? .model80
  }
}

public struct HtmlToImagesOptions {
  public let model: PrinterModel
  public let maxHeightToBreak: CGFloat?

  public init(dictionary: [String: Any]) {
    let modelString = dictionary["model"] as? String ?? "80"
    self.model = PrinterModel(rawValue: modelString) ?? .model80
    self.maxHeightToBreak = dictionary["maxHeightToBreak"] as? CGFloat
  }
}
