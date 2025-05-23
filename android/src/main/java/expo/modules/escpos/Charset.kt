package expo.modules.escpos

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Represents a fragment of encoded text with its encoding information
 */
@OptIn(ExperimentalUnsignedTypes::class)
data class EncodingFragment (
    val bytes: UByteArray,
    val codepage: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncodingFragment

        if (!bytes.contentEquals(other.bytes)) return false
        if (codepage != other.codepage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + codepage.hashCode()
        return result
    }
}

/**
 * Provides mapping between different character encodings
 */
object CharsetMapping {
    private val charsetMapping = mapOf(
        "ascii" to StandardCharsets.US_ASCII,
        "cp437" to getCodepage("IBM437"),
        "cp720" to getCodepage("IBM720"),
        "cp737" to getCodepage("IBM737"),
        "cp775" to getCodepage("IBM775"),
        "cp850" to getCodepage("IBM850"),
        "cp851" to getCodepage("IBM851"),
        "cp852" to getCodepage("IBM852"),
        "cp853" to getCodepage("IBM853"),
        "cp855" to getCodepage("IBM855"),
        "cp857" to getCodepage("IBM857"),
        "cp858" to getCodepage("IBM858"),
        "cp860" to getCodepage("IBM860"),
        "cp861" to getCodepage("IBM861"),
        "cp862" to getCodepage("IBM862"),
        "cp863" to getCodepage("IBM863"),
        "cp864" to getCodepage("IBM864"),
        "cp865" to getCodepage("IBM865"),
        "cp866" to getCodepage("IBM866"),
        "cp869" to getCodepage("IBM869"),
        "cp874" to getCodepage("IBM874"),
        "cp936" to getCodepage("gb2312"),
        "cp949" to getCodepage("ks_c_5601-1987"),
        "cp950" to getCodepage("big5"),
        "cp1098" to getCodepage("IBM1098"),
        "cp1118" to getCodepage("IBM1118"),
        "cp1119" to getCodepage("IBM1119"),
        "cp1125" to getCodepage("IBM1125"),
        "cp1250" to getCodepage("windows-1250"),
        "cp1251" to getCodepage("windows-1251"),
        "cp1252" to getCodepage("windows-1252"),
        "cp1253" to getCodepage("windows-1253"),
        "cp1254" to getCodepage("windows-1254"),
        "cp1255" to getCodepage("windows-1255"),
        "cp1256" to getCodepage("windows-1256"),
        "cp1257" to getCodepage("windows-1257"),
        "cp1258" to getCodepage("windows-1258"),
        "iso88591" to StandardCharsets.ISO_8859_1,
        "iso88592" to getCodepage("ISO-8859-2"),
        "iso88595" to getCodepage("ISO-8859-5"),
        "iso88596" to getCodepage("ISO-8859-6"),
        "iso88597" to getCodepage("ISO-8859-7"),
        "iso88598" to getCodepage("ISO-8859-8"),
        "iso88599" to getCodepage("ISO-8859-9"),
        "iso885915" to getCodepage("ISO-8859-15"),
        "shiftjis" to getCodepage("shift_jis"),
        "windows874" to getCodepage("windows-874"),
        "windows1250" to getCodepage("windows-1250"),
        "windows1251" to getCodepage("windows-1251"),
        "windows1252" to getCodepage("windows-1252"),
        "windows1253" to getCodepage("windows-1253"),
        "windows1254" to getCodepage("windows-1254"),
        "windows1255" to getCodepage("windows-1255"),
        "windows1256" to getCodepage("windows-1256"),
        "windows1257" to getCodepage("windows-1257"),
        "windows1258" to getCodepage("windows-1258"),
        "rk1048" to getCodepage("PTCP154"),
        "utf8" to StandardCharsets.UTF_8,
        "utf16" to StandardCharsets.UTF_16
    )

    /**
     * Gets a charset by name with fallback to standard ASCII
     */
    private fun getCodepage(name: String): Charset {
        return try {
            Charset.forName(name)
        } catch (e: Exception) {
            StandardCharsets.US_ASCII
        }
    }

    /**
     * Get the Java Charset for the given encoding name
     */
    fun getCharset(encoding: String): Charset {
        return charsetMapping[encoding.lowercase()] ?: StandardCharsets.US_ASCII
    }

    /**
     * Check if encoding is supported
     */
    fun isSupported(encoding: String): Boolean {
        return charsetMapping.containsKey(encoding.lowercase())
    }
}

/**
 * Utility functions for detecting character encodings
 */
object CharsetDetector {
    /**
     * Get the best encoding for a given character
     */
    fun getBestEncoding(char: Char, candidates: List<String>): String? {
        for (encoding in candidates) {
            val charset = CharsetMapping.getCharset(encoding)
            if (canEncode(charset, char)) {
                return encoding
            }
        }
        return null
    }

    /**
     * Check if a charset can encode a character
     */
    private fun canEncode(charset: Charset, char: Char): Boolean {
        return try {
            val str = char.toString()
            val bytes = str.toByteArray(charset)
            val decoded = String(bytes, charset)
            decoded == str
        } catch (e: Exception) {
            false
        }
    }
}
