package expo.modules.escpos

data class CodePageDefinition(
    val name: String,
    val languages: List<String>,
    val offset: Int,
    val chars: String
)

object CodePageDefinitions {
    val definitions = mapOf(
        "cp437" to CodePageDefinition(
            name = "USA, Standard Europe",
            languages = listOf("en"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜ¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp720" to CodePageDefinition(
            name = "Arabic",
            languages = listOf("ar"),
            offset = 128,
            chars = """
                \u0080\u0081éâ\u0084à\u0086çêëèïî\u008D\u008E\u008F\u0090\u0651\u0652ô¤ـûùءآأؤ£إئابةتثجحخدذرزسشص«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀ضطظعغفµقكلمنهوىي≡\u064B\u064C\u064D\u064E\u064F\u0650≈°∙·√ⁿ²■\u00A0
            """.trimIndent()
        ),
        "cp737" to CodePageDefinition(
            name = "Greek",
            languages = listOf("el"),
            offset = 128,
            chars = """
                ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρσςτυφχψ░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀ωάέήϊίόύϋώΆΈΉΊΌΎΏ±≥≤ΪΫ÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp775" to CodePageDefinition(
            name = "Baltic Rim",
            languages = listOf("et", "lt"),
            offset = 128,
            chars = """
                ĆüéāäģåćłēŖŗīŹÄÅÉæÆōöĢ¢ŚśÖÜø£Ø×¤ĀĪóŻżź"¦©®¬½¼Ł«»░▒▓│┤ĄČĘĖ╣║╗╝ĮŠ┐└┴┬├─┼ŲŪ╚╔╩╦╠═╬Žąčęėįšųūž┘┌█▄▌▐▀ÓßŌŃõÕµńĶķĻļņĒŅ'­±"¾¶§÷„°∙·¹³²■ 
            """.trimIndent()
        ),
        "cp850" to CodePageDefinition(
            name = "Multilingual",
            languages = listOf("en"),
            offset = 128,
            chars = """
                ÇüéâäůćçłëŐőîŹÄĆÉĹĺôöĽľŚśÖÜŤťŁ×čáíóúĄąŽžĘę¬źČş«»░▒▓│┤ÁÂĚŞ╣║╗╝Żż┐└┴┬├─┼Ăă╚╔╩╦╠═╬¤đĐĎËďŇÍÎě┘┌█▄ŢŮ▀ÓßÔŃńňŠšŔÚŕŰýÝţ´­˝˛ˇ˘§÷¸°¨˙űŘř■ 
            """.trimIndent()
        ),
        "cp851" to CodePageDefinition(
            name = "Greek",
            languages = listOf("el"),
            offset = 128,
            chars = """
                ÇüéâäàΆçêëèïîΈÄΉΊ ΌôöΎûùΏÖÜά£έήίϊΐόύΑΒΓΔΕΖΗ½ΘΙ«»░▒▓│┤ΚΛΜΝ╣║╗╝ΞΟ┐└┴┬├─┼ΠΡ╚╔╩╦╠═╬ΣΤΥΦΧΨΩαβγ┘┌█▄δε▀ζηθικλμνξοπρσςτ´­±υφχ§ψ΅°¨ωϋΰώ■ 
            """.trimIndent()
        ),
        "cp852" to CodePageDefinition(
            name = "Latin 2",
            languages = listOf("hu", "pl", "cz"),
            offset = 128,
            chars = """
                ÇüéâäůćçłëŐőîŹÄĆÉĹĺôöĽľŚśÖÜŤťŁ×čáíóúĄąŽžĘę¬źČş«»░▒▓│┤ÁÂĚŞ╣║╗╝Żż┐└┴┬├─┼Ăă╚╔╩╦╠═╬¤đĐĎËďŇÍÎě┘┌█▄ŢŮ▀ÓßÔŃńňŠšŔÚŕŰýÝţ´­˝˛ˇ˘§÷¸°¨˙űŘř■ 
            """.trimIndent()
        ),
        "cp853" to CodePageDefinition(
            name = "Turkish",
            languages = listOf("tr"),
            offset = 128,
            chars = """
                ÇüéâäàĉçêëèïîìÄĈÉċĊôöòûùİÖÜĝ£Ĝ×ĵáíóúñÑĞğĤĥ½Ĵş«»░▒▓│┤ÁÂÀŞ╣║╗╝Żż┐└┴┬├─┼Ŝŝ╚╔╩╦╠═╬¤ÊËÈıÍÎÏ┘┌█▄Ì▀ÓßÔÒĠġµĦħÚÛÙŬŭ´­ℓŉ˘§÷¸°¨˙³²■ 
            """.trimIndent()
        ),
        "cp855" to CodePageDefinition(
            name = "Cyrillic",
            languages = listOf("bg"),
            offset = 128,
            chars = """
                ђЂѓЃёЁєЄѕЅіІїЇјЈљЉњЊћЋќЌўЎџЏюЮъЪаАбБцЦдДеЕфФгГ«»░▒▓│┤хХиИ╣║╗╝йЙ┐└┴┬├─┼кК╚╔╩╦╠═╬¤лЛмМнНоОп┘┌█▄Пя▀ЯрРсСтТуУжЖвВьЬ№­ыЫзЗшШэЭщЩчЧ§■ 
            """.trimIndent()
        ),
        "cp857" to CodePageDefinition(
            name = "Turkish",
            languages = listOf("tr"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîıÄÅÉæÆôöòûùİÖÜø£ØŞşáíóúñÑĞğ¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤ºªÊËÈÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµ×ÚÛÙìÿ¯´­±¾¶§÷¸°¨·¹³²■ 
            """.trimIndent()
        ),
        "cp858" to CodePageDefinition(
            name = "Euro",
            languages = listOf("en"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤ðÐÊËÈ€ÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµþÞÚÛÙýÝ¯´­±‗¾¶§÷¸°¨·¹³²■ 
            """.trimIndent()
        ),
        "cp860" to CodePageDefinition(
            name = "Portuguese",
            languages = listOf("pt"),
            offset = 128,
            chars = """
                ÇüéâãàÁçêÊèÍÔìÃÂÉÀÈôõòÚùÌÕÜ¢£Ù₧ÓáíóúñÑªº¿Ò¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp861" to CodePageDefinition(
            name = "Icelandic",
            languages = listOf("is"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèÐðÞÄÅÉæÆôöþûÝýÖÜø£Ø₧ƒáíóúÁÍÓÚ¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp862" to CodePageDefinition(
            name = "Hebrew",
            languages = listOf("he"),
            offset = 128,
            chars = """
                אבגדהוזחטיךכלםמןנסעףפץצקרשת¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp863" to CodePageDefinition(
            name = "Canadian French",
            languages = listOf("fr"),
            offset = 128,
            chars = """
                ÇüéâÂà¶çêëèïîÀ§ÉÈÊôËÏûù¤ÔÜ¢£ÙÛƒ¦´óú¨¸³¯Î⌐¬½¼¾«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp864" to CodePageDefinition(
            name = "Arabic",
            languages = listOf("ar"),
            offset = 0,
            chars = """
                \u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !"#$٪&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~°·∙√▒─│┼┤┬├┴┐┌└┘β∞φ±½¼≈«»ﻷﻸﻻﻼ ­ﺂ£¤ﺄﺎﺏﺕﺙ،ﺝﺡﺥ٠١٢٣٤٥٦٧٨٩ﻑ؛ﺱﺵﺹ؟¢ﺀﺁﺃﺅﻊﺋﺍﺑﺓﺗﺛﺟﺣﺧﺩﺫﺭﺯﺳﺷﺻﺿﻁﻅﻋﻏ¦¬÷×ﻉـﻓﻗﻛﻟﻣﻧﻫﻭﻯﻳﺽﻌﻎﻍﻡﹽّﻥﻩﻬﻰﻲﻐﻕﻵﻶﻝﻙﻱ■
            """.trimIndent()
        ),
        "cp865" to CodePageDefinition(
            name = "Nordic",
            languages = listOf("sv", "dk"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø₧ƒáíóúñÑªº¿⌐¬½¼¡«¤░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp866" to CodePageDefinition(
            name = "Cyrillic 2",
            languages = listOf("ru"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀рстуфхцчшщъыьэюяЁёЄєЇїЎў°∙·√№¤■ 
            """.trimIndent()
        ),
        "cp869" to CodePageDefinition(
            name = "Greek",
            languages = listOf("el"),
            offset = 128,
            chars = """
                Ά·¬¦''Έ―ΉΊΪΌΎΫ©Ώ²³ά£έήίϊΐόύΑΒΓΔΕΖΗ½ΘΙ«»░▒▓│┤ΚΛΜΝ╣║╗╝ΞΟ┐└┴┬├─┼ΠΡ╚╔╩╦╠═╬ΣΤΥΦΧΨΩαβγ┘┌█▄δε▀ζηθικλμνξοπρσςτ΄­±υφχ§ψ΅°¨ωϋΰώ■ 
            """.trimIndent()
        ),
        "cp874" to CodePageDefinition(
            name = "Thai",
            languages = listOf("th"),
            offset = 128,
            chars = """
                €…''""•–—  กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮฯะัาำิีึืฺุู฿เแโใไๅๆ็่้๊๋์ํ๎๏๐๑๒๓๔๕๖๗๘๙๚๛
            """.trimIndent()
        ),
        "cp1098" to CodePageDefinition(
            name = "Farsi",
            languages = listOf("fa"),
            offset = 128,
            chars = """
                \u0020\u0020\u060c\u061b\u061f\u064b\u0622\ufe82\uf8fa\u0627\ufe8e\uf8fb\u0621\u0623\ufe84\uf8f9\u0624\ufe8b\u0628\ufe91\ufb56\ufb58\u062a\ufe97\u062b\ufe9b\u062c\ufe9f\ufb7a\ufb7c\u00d7\u062d\ufea3\u062e\ufea7\u062f\u0630\u0631\u0632\ufb8a\u0633\ufeb3\u0634\ufeb7\u0635\ufebb\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u0636\ufebf\ufec1\ufec3\u2563\u2551\u2557\u255d\u00a4\ufec5\u2510\u2514\u2534\u252c\u251c\u2500\u253c\ufec7\u0639\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u0020\ufeca\ufecb\ufecc\u063a\ufece\ufecf\ufed0\u0641\ufed3\u2518\u250c\u2588\u2584\u0642\ufed7\u2580\ufb8e\ufedb\ufb92\ufb94\u0644\ufedf\u0645\ufee3\u0646\ufee7\u0648\u0647\ufeeb\ufeec\ufba4\ufbfc\u00ad\ufbfd\ufbfe\u0640\u0660\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u25a0\u00a0
            """.trimIndent()
        ),
        "cp1118" to CodePageDefinition(
            name = "Lithuanian",
            languages = listOf("lt"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜ¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤ĄČĘĖ╣║╗╝ĮŠ┐└┴┬├─┼ŲŪ╚╔╩╦╠═╬Žąčęėįšųūž┘┌█▄▌▐▀αβΓπΣσµτΦΘΩδ∞φε⋂≡±≥≤„"÷≈°∙˙√ⁿ²■ 
            """.trimIndent()
        ),
        "cp1119" to CodePageDefinition(
            name = "Lithuanian",
            languages = listOf("lt"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤ĄČĘĖ╣║╗╝ĮŠ┐└┴┬├─┼ŲŪ╚╔╩╦╠═╬Žąčęėįšųūž┘┌█▄▌▐▀рстуфхцчшщъыьэюяЁё≥≤„"÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp1125" to CodePageDefinition(
            name = "Ukrainian",
            languages = listOf("uk"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀рстуфхцчшщъыьэюяЁёҐґЄєІіЇї·√№¤■ 
            """.trimIndent()
        ),
        "cp1162" to CodePageDefinition(
            name = "Thai",
            languages = listOf("th"),
            offset = 128,
            chars = """
                €''""•–—  กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮฯะัาำิีึืฺุู฿เแโใไๅๆ็่้๊๋์ํ๎๏๐๑๒๓๔๕๖๗๘๙๚๛
            """.trimIndent()
        ),
        "cp2001" to CodePageDefinition(
            name = "Lithuanian KBL or 771",
            languages = listOf("lt"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█ĄąČčрстуфхцчшщъыьэюяĘęĖėĮįŠšŲųŪūŽž■ 
            """.trimIndent()
        ),
        "cp3001" to CodePageDefinition(
            name = "Estonian 1 or 1116",
            languages = listOf("et"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤šŠÊËÈıÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµžŽÚÛÙýÝ¯´­±‗¾¶§÷¸°¨·¹³²■ 
            """.trimIndent()
        ),
        "cp3002" to CodePageDefinition(
            name = "Estonian 2",
            languages = listOf("et"),
            offset = 128,
            chars = """
                 ¡¢£¤¥¦§¨©ª«¬­®‾°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏŠÑÒÓÔÕÖ×ØÙÚÛÜÝŽßàáâãäåæçèéêëìíîïšñòóôõö÷øùúûüýžÿ
            """.trimIndent()
        ),
        "cp3011" to CodePageDefinition(
            name = "Latvian 1",
            languages = listOf("lv"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜ¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤Ā╢ņ╕╣║╗╝╜╛┐└┴┬├─┼ā╟╚╔╩╦╠═╬╧Š╤čČ╘╒ģĪī┘┌█▄ūŪ▀αßΓπΣσµτΦΘΩδ∞φε∩ĒēĢķĶļĻžŽ∙·√Ņš■ 
            """.trimIndent()
        ),
        "cp3012" to CodePageDefinition(
            name = "Latvian 2 (modified 866)",
            languages = listOf("lv"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤Ā╢ņ╕╣║╗╝Ō╛┐└┴┬├─┼ā╟╚╔╩╦╠═╬╧Š╤čČ╘╒ģĪī┘┌█▄ūŪ▀рстуфхцчшщъыьэюяĒēĢķĶļĻžŽō·√Ņš■ 
            """.trimIndent()
        ),
        "cp3021" to CodePageDefinition(
            name = "Bulgarian (MIK)",
            languages = listOf("bg"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя└┴┬├─┼╣║╚╔╩╦╠═╬┐░▒▓│┤№§╗╝┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3041" to CodePageDefinition(
            name = "Maltese ISO 646",
            languages = listOf("mt"),
            offset = 0,
            chars = """
                \u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZġżħ^_ċabcdefghijklmnopqrstuvwxyzĠŻĦĊ\u007F
            """.trimIndent()
        ),
        "cp3840" to CodePageDefinition(
            name = "Russian (modified 866)",
            languages = listOf("ru"),
            offset = 128,
            chars = """
                АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀рстуфхцчшщъыьэюя≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3841" to CodePageDefinition(
            name = "Ghost",
            languages = listOf("ru"),
            offset = 128,
            chars = """
                ғәёіїјҝөўүӽӈҹҷє£ҒӘЁІЇЈҜӨЎҮӼӇҸҶЄЪ !"#$%&'()*+,-./0123456789:;<=>?юабцдефгхийклмнопярстужвьызшэщчъЮАБЦДЕФГХИЙКЛМНОПЯРСТУЖВЬЫЗШЭЩЧ∅
            """.trimIndent()
        ),
        "cp3843" to CodePageDefinition(
            name = "Polish (Mazovia)",
            languages = listOf("pl"),
            offset = 128,
            chars = """
                ÇüéâäàąçêëèïîćÄĄĘęłôöĆûùŚÖÜ¢Ł¥śƒŹŻóÓńŃźż¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3844" to CodePageDefinition(
            name = "Czech (Kamenický)",
            languages = listOf("cz"),
            offset = 128,
            chars = """
                ČüéďäĎŤčěĚĹÍľĺÄÁÉžŽôöÓůÚýÖÜŠĽÝŘťáíóúňŇŮÔšřŕŔ¼§«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3845" to CodePageDefinition(
            name = "Hungarian (CWI-2)",
            languages = listOf("hu"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîÍÄÁÉæÆőöÓűÚŰÖÜ¢£¥₧ƒáíóúñÑªŐ¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3846" to CodePageDefinition(
            name = "Turkish",
            languages = listOf("tr"),
            offset = 128,
            chars = """
                ÇüéâäàåçêëèïîıÄÅÉæÆôöòûùİÖÜ¢£¥ŞşáíóúñÑĞğ¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ 
            """.trimIndent()
        ),
        "cp3847" to CodePageDefinition(
            name = "Brazil ABNT",
            languages = listOf("pt"),
            offset = 256,
            chars = ""
        ),
        "cp3848" to CodePageDefinition(
            name = "Brazil ABICOMP",
            languages = listOf("pt"),
            offset = 160,
            chars = """
                 ÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖŒÙÚÛÜŸ¨£¦§°¡àáâãäçèéêëìíîïñòóôõöœùúûüÿßªº¿±
            """.trimIndent()
        ),
        "iso88591" to CodePageDefinition(
            name = "Latin 1",
            languages = listOf("en"),
            offset = 128,
            chars = """
                 ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ
            """.trimIndent()
        ),
        "iso88592" to CodePageDefinition(
            name = "Latin 2",
            languages = listOf("hu", "pl", "cz"),
            offset = 128,
            chars = """
                 Ą˘Ł¤ĽŚ§¨ŠŞŤŹ­ŽŻ°ą˛ł´ľśˇ¸šşťź˝žżŔÁÂĂÄĹĆÇČÉĘËĚÍÎĎĐŃŇÓÔŐÖ×ŘŮÚŰÜÝŢßŕáâăäĺćçčéęëěíîďđńňóôőö÷řůúűüýţ˙
            """.trimIndent()
        ),
        "iso88597" to CodePageDefinition(
            name = "Greek",
            languages = listOf("el"),
            offset = 128,
            chars = """
                 ''£€₯¦§¨©ͺ«¬­―°±²³΄΅Ά·ΈΉΊ»Ό½ΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώ
            """.trimIndent()
        ),
        "iso885915" to CodePageDefinition(
            name = "Latin 9",
            languages = listOf("fr"),
            offset = 128,
            chars = """
                 ¡¢£€¥Š§š©ª«¬­®¯°±²³Žµ¶·ž¹º»ŒœŸ¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ
            """.trimIndent()
        ),
        "rk1048" to CodePageDefinition(
            name = "Kazakh",
            languages = listOf("kk"),
            offset = 128,
            chars = """
                ЂЃ‚ѓ„…†‡€‰Љ‹ЊҚҺЏђ''""•–—™љ›њқһџ ҰұӘ¤Ө¦§Ё©Ғ«¬­®Ү°±Ііөµ¶·ё№ғ»әҢңүАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя
            """.trimIndent()
        ),
        "windows1250" to CodePageDefinition(
            name = "Latin 2",
            languages = listOf("hu", "pl", "cz"),
            offset = 128,
            chars = """
                €‚„…†‡‰Š‹ŚŤŽŹ''""•–—™š›śťžź ˇ˘Ł¤Ą¦§¨©Ş«¬­®Ż°±˛ł´µ¶·¸ąş»Ľ˝ľżŔÁÂĂÄĹĆÇČÉĘËĚÍÎĎĐŃŇÓÔŐÖ×ŘŮÚŰÜÝŢßŕáâăäĺćçčéęëěíîďđńňóôőö÷řůúűüýţ˙
            """.trimIndent()
        ),
        "windows1251" to CodePageDefinition(
            name = "Cyrillic",
            languages = listOf("ru"),
            offset = 128,
            chars = """
                ЂЃ‚ѓ„…†‡€‰Љ‹ЊЌЋЏђ''""•–—™љ›њќћџ ЎўЈ¤Ґ¦§Ё©Є«¬­®Ї°±Ііґµ¶·ё№є»јЅѕїАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя
            """.trimIndent()
        ),
        "windows1252" to CodePageDefinition(
            name = "Latin",
            languages = listOf("fr"),
            offset = 128,
            chars = """
                €‚ƒ„…†‡ˆ‰Š‹ŒŽ''""•–—˜™š›œžŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ
            """.trimIndent()
        ),
        "windows1253" to CodePageDefinition(
            name = "Greek",
            languages = listOf("el"),
            offset = 128,
            chars = """
                €‚ƒ„…†‡‰‹'''"•–—™›°±²³΄µ¶·ΈΉΊ»Ό½ΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώ
            """.trimIndent()
        ),
        "windows1254" to CodePageDefinition(
            name = "Turkish",
            languages = listOf("tr"),
            offset = 128,
            chars = """
                €‚ƒ„…†‡ˆ‰Š‹Œ'''"•–—˜™›œŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂĂÄÅÆÇÈÉÊË̀ÍÎÏĐÑ̉ÓÔƠÖ×ØÙÚÛÜỮßàáâăäåæçèéêë́íîïđṇ̃óôơö÷øùúûüư₫ÿ
            """.trimIndent()
        ),
        "windows1255" to CodePageDefinition(
            name = "Hebrew",
            languages = listOf("he"),
            offset = 128,
            chars = """
                €‚ƒ„…†‡ˆ‰‹'''"•–—˜™› ¡¢£₪¥¦§¨©×«¬­®¯°±²³´µ¶·¸¹÷»¼½¾¿ְֱֲֳִֵֶַָֹֺֻּֽ־ֿ׀ׁׂ׃װױײ׳״אבגדהוזחטיךכלםמןנסעףפץצקרשת‎‏
            """.trimIndent()
        ),
        "windows1256" to CodePageDefinition(
            name = "Arabic",
            languages = listOf("ar"),
            offset = 128,
            chars = """
                €پ‚ƒ„…†‡ˆ‰ٹ‹Œچژڈگ''""•–—ک™ڑ›œں ،¢£¤¥¦§¨©ھ«¬­®¯°±²³´µ¶·¸¹؛»¼½¾؟ہءآأؤإئابةتثجحخدذرزسشصض×طظعغـفقكàلâمنهوçèéêëىيîïًٌٍَôُِ÷ّùْûü‎‏ے
            """.trimIndent()
        ),
        "windows1257" to CodePageDefinition(
            name = "Baltic Rim",
            languages = listOf("et", "lt"),
            offset = 128,
            chars = """
                €‚„…†‡‰‹¨ˇ¸''""•–—™›¯˛ ¢£¤¦§Ø©Ŗ«¬­®Æ°±²³´µ¶·ø¹ŗ»¼½¾æĄĮĀĆÄÅĘĒČÉŹĖĢĶĪĻŠŃŅÓŌÕÖ×ŲŁŚŪÜŻŽßąįāćäåęēčéźėģķīļšńņóōõö÷ųłśūüżž˙
            """.trimIndent()
        ),
        "windows1258" to CodePageDefinition(
            name = "Vietnamese",
            languages = listOf("vi"),
            offset = 128,
            chars = """
                €‚ƒ„…†‡ˆ‰‹Œ'''"•–—˜™›œŸ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂĂÄÅÆÇÈÉÊË̀ÍÎÏĐÑ̉ÓÔƠÖ×ØÙÚÛÜỮßàáâăäåæçèéêë́íîïđṇ̃óôơö÷øùúûüư₫ÿ
            """.trimIndent()
        )
    )
}