package expo.modules.escpos

/**
 * Supported printer manufacturer types
 * Different manufacturers use different command mappings
 */
enum class PrinterType {
    EPSON,
    ZJIANG,
    BIXOLON,
    STAR,
    CITIZEN,
    LEGACY;

    override fun toString(): String = name.lowercase()
}