package expo.modules.escpos

import expo.modules.escpos.EscPosEncoder.Options

enum class ReceiptModel(val value: String) {
    MODEL_58("58"),
    MODEL_80("80")
}

data class ReceiptConfig(
    val model: ReceiptModel
)

fun receiptImages(config: ReceiptConfig, imageBase64s: List<String>): ByteArray {
    return receiptRender(config) { encoder ->
        renderImages(encoder, imageBase64s)
    }
}

fun receiptPngImages(config: ReceiptConfig, images: List<ByteArray>): ByteArray {
    return receiptRender(config) { encoder ->
        renderPngImages(encoder, images)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun renderImages(encoder: EscPosEncoder, imageBase64s: List<String>): ByteArray {
    encoder.align("center")

    for (img in imageBase64s) {
        encoder.imageBase64(img)
    }

    // Add space at the end and cut
    encoder.newline()
        .newline()
        .newline()
        .newline()
        .newline()
        .cut()

    return encoder.encode()
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun renderPngImages(encoder: EscPosEncoder, images: List<ByteArray>): ByteArray {
    encoder.align("center")

    for (img in images) {
        encoder.imagePng(img)
    }

    // Add space at the end and cut
    encoder.newline()
        .newline()
        .newline()
        .newline()
        .newline()
        .cut()

    return encoder.encode()
}

private fun receiptRender(config: ReceiptConfig, render: (EscPosEncoder) -> ByteArray): ByteArray {
    val modelDefinitions = mapOf(
        ReceiptModel.MODEL_58 to ModelDefinition(32, listOf("cp866")),
        ReceiptModel.MODEL_80 to ModelDefinition(46, listOf("cp866"))
    )

    val model = modelDefinitions[config.model] ?: ModelDefinition(46, listOf("cp866"))

    val encoder = EscPosEncoder(Options(
        width = model.size,
        codepageCandidates = model.codepageCandidates
    ))

    encoder.initialize()
    encoder.codepage("auto")

    return render(encoder)
}

private data class ModelDefinition(
    val size: Int,
    val codepageCandidates: List<String>
)
