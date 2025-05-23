package expo.modules.escpos

import expo.modules.escpos.EscPosEncoder.Options

enum class ReceiptModel(val value: String) {
    MODEL_58("58"),
    MODEL_80("80")
}

data class ReceiptConfig(
    val model: ReceiptModel
)

/**
 * Generate a receipt with a single image
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun receiptImage(config: ReceiptConfig, imageBase64: String): ByteArray {
    return receiptRender(config) { encoder ->
        renderImages(encoder, listOf(imageBase64))
    }
}

/**
 * Generate a receipt with multiple images
 */
fun receiptImages(config: ReceiptConfig, imageBase64s: List<String>): ByteArray {
    return receiptRender(config) { encoder ->
        renderImages(encoder, imageBase64s)
    }
}

/**
 * Render one or more images as a receipt
 */
@OptIn(ExperimentalUnsignedTypes::class)
private fun renderImages(encoder: EscPosEncoder, imageBase64s: List<String>): ByteArray {
    encoder.align("center")

    for (img in imageBase64s) {
        encoder.image(img)
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
