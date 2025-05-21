import Foundation

public enum ReceiptModel: String {
    case model58 = "58"
    case model80 = "80"
}

public struct ReceiptConfig {
    public let model: ReceiptModel
    public init(model: ReceiptModel) {
        self.model = model
    }
}

public func receiptImage(config: ReceiptConfig, imageBase64: String) -> [UInt8] {
    return receiptRender(config: config) { encoder in
        return render(encoder: encoder, imageBase64s: [imageBase64])
    }
}

public func receiptImages(config: ReceiptConfig, imageBase64s: [String]) -> [UInt8] {
    return receiptRender(config: config) { encoder in
        return render(encoder: encoder, imageBase64s: imageBase64s)
    }
}

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

private func receiptRender(config: ReceiptConfig, render: (EscPosEncoder) -> [UInt8]) -> [UInt8] {
    let modelDefinitions: [ReceiptModel: (size: Int, codepageCandidates: [String])] = [
        .model58: (58, ["cp866"]),
        .model80: (80, ["cp866"])
    ]
    let model = modelDefinitions[config.model] ?? (80, ["cp866"])
    let encoder = EscPosEncoder(options: [
        "width": model.size == 58 ? 32 : 46,
        "codepageCandidates": model.codepageCandidates
    ])
    encoder.initialize()
    encoder.codepage("auto")
    return render(encoder)
} 
