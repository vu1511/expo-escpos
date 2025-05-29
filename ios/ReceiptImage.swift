import Foundation
import UIKit

public func receiptImage(config: ReceiptOptions, imageBase64: String) -> [UInt8]
{
  return receiptRender(config: config) { encoder in
    return render(encoder: encoder, imageBase64s: [imageBase64])
  }
}

public func receiptImages(config: ReceiptOptions, imageBase64s: [String])
  -> [UInt8]
{
  return receiptRender(config: config) { encoder in
    return render(encoder: encoder, imageBase64s: imageBase64s)
  }
}

public func receiptPngImages(config: ReceiptOptions, images: [Data]) -> [UInt8]
{
  return receiptRender(config: config) { encoder in
    return renderPng(encoder: encoder, images: images)
  }
}

// MARK: - Optimized functions for direct Data processing
public func receiptImageFromData(config: ReceiptOptions, imageData: Data) throws
  -> [UInt8]
{
  return receiptRender(config: config) { encoder in
    return try renderFromData(encoder: encoder, imageDatas: [imageData])
  }
}

public func receiptImagesFromData(config: ReceiptOptions, imageDatas: [Data])
  throws -> [UInt8]
{
  return receiptRender(config: config) { encoder in
    return try renderFromData(encoder: encoder, imageDatas: imageDatas)
  }
}

// MARK: - Rendering functions
private func render(encoder: EscPosEncoder, imageBase64s: [String]) -> [UInt8] {
  encoder.align("center")
  for img in imageBase64s {
    encoder.image(img)
  }

  encoder.newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .cut()

  return encoder.encode()
}

private func renderPng(encoder: EscPosEncoder, images: [Data]) -> [UInt8] {
  encoder.align("center")
  for img in images {
    encoder.imageData(img)
  }

  encoder.newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .cut()

  return encoder.encode()
}

private func renderFromData(encoder: EscPosEncoder, imageDatas: [Data]) throws
  -> [UInt8]
{
  encoder.align("center")

  for imageData in imageDatas {
    // Convert Data directly to base64 only when needed by the encoder
    let base64String = imageData.base64EncodedString()
    encoder.image(base64String)
  }

  encoder.newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .newline()
    .cut()

  return encoder.encode()
}

private func receiptRender(
  config: ReceiptOptions, render: (EscPosEncoder) throws -> [UInt8]
) -> [UInt8] {
  let modelDefinitions:
    [PrinterModel: (size: Int, codepageCandidates: [String])] = [
      .model58: (58, ["cp866"]),
      .model80: (80, ["cp866"]),
    ]
  let model = modelDefinitions[config.model] ?? (80, ["cp866"])
  let encoder = EscPosEncoder(options: [
    "width": model.size == 58 ? 32 : 46,
    "codepageCandidates": model.codepageCandidates,
  ])
  encoder.initialize()
  encoder.codepage("auto")

  do {
    return try render(encoder)
  } catch {
    return []
  }
}
