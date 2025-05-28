import Foundation

public struct CodePageDefinition {
  public let name: String
  public let languages: [String]
  public let offset: Int
  public let chars: String

  public init(name: String, languages: [String], offset: Int, chars: String) {
    self.name = name
    self.languages = languages
    self.offset = offset
    self.chars = chars
  }
}

public struct CodePageDefinitions {
  public static let definitions: [String: CodePageDefinition] = [
    "cp437": CodePageDefinition(
      name: "USA, Standard Europe",
      languages: ["en"],
      offset: 128,
      chars:
        "ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜ¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp720": CodePageDefinition(
      name: "Arabic",
      languages: ["ar"],
      offset: 128,
      chars:
        #"\x80\x81éâ\x84à\x86çêëèïî\x8d\x8e\x8f\x90\u0651\u0652ô¤ـûùءآأؤ£إئابةتثجحخدذرزسشص«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀ضطظعغفµقكلمنهوىي≡\u064b\u064c\u064d\u064e\u064f\u0650≈°∙·√ⁿ²■\u00a0"#
    ),
    "cp737": CodePageDefinition(
      name: "Greek",
      languages: ["el"],
      offset: 128,
      chars:
        "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρσςτυφχψ░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀ωάέήϊίόύϋώΆΈΉΊΌΎΏ±≥≤ΪΫ÷≈°∙·√ⁿ²■"
    ),
    "cp775": CodePageDefinition(
      name: "Baltic Rim",
      languages: ["et", "lt"],
      offset: 128,
      chars:
        "ĆüéāäģåćłēŖŗīŹÄÅÉæÆōöĢ¢ŚśÖÜø£Ø×¤ĀĪóŻżź\"¦©®¬½¼Ł«»░▒▓│┤ĄČĘĖ╣║╗╝ĮŠ┐└┴┬├─┼ŲŪ╚╔╩╦╠═╬Žąčęėįšųūž┘┌█▄▌▐▀ÓßŌŃõÕµńĶķĻļņĒŅ'­±\"¾¶§÷„°∙·¹³²■ "
    ),
    "cp850": CodePageDefinition(
      name: "Multilingual",
      languages: ["en"],
      offset: 128,
      chars:
        "ÇüéâäůćçłëŐőîŹÄĆÉĹĺôöĽľŚśÖÜŤťŁ×čáíóúĄąŽžĘę¬źČş«»░▒▓│┤ÁÂĚŞ╣║╗╝Żż┐└┴┬├─┼Ăă╚╔╩╦╠═╬¤đĐĎËďŇÍÎě┘┌█▄ŢŮ▀ÓßÔŃńňŠšŔÚŕŰýÝţ´­˝˛ˇ˘§÷¸°¨˙űŘř■ "
    ),
    "cp851": CodePageDefinition(
      name: "Greek",
      languages: ["el"],
      offset: 128,
      chars:
        "ÇüéâäàΆçêëèïîΈÄΉΊ ΌôöΎûùΏÖÜά£έήίϊΐόύΑΒΓΔΕΖΗ½ΘΙ«»░▒▓│┤ΚΛΜΝ╣║╗╝ΞΟ┐└┴┬├─┼ΠΡ╚╔╩╦╠═╬ΣΤΥΦΧΨΩαβγ┘┌█▄δε▀ζηθικλμνξοπρσςτ´­±υφχ§ψ΅°¨ωϋΰώ■ "
    ),
    "cp852": CodePageDefinition(
      name: "Latin 2",
      languages: ["hu", "pl", "cz"],
      offset: 128,
      chars:
        "ÇüéâäůćçłëŐőîŹÄĆÉĹĺôöĽľŚśÖÜŤťŁ×čáíóúĄąŽžĘę¬źČş«»░▒▓│┤ÁÂĚŞ╣║╗╝Żż┐└┴┬├─┼Ăă╚╔╩╦╠═╬¤đĐĎËďŇÍÎě┘┌█▄ŢŮ▀ÓßÔŃńňŠšŔÚŕŰýÝţ´­˝˛ˇ˘§÷¸°¨˙űŘř■ "
    ),
    "cp855": CodePageDefinition(
      name: "Cyrillic",
      languages: ["bg"],
      offset: 128,
      chars:
        "ђЂѓЃёЁєЄѕЅіІїЇјЈљЉњЊћЋќЌўЎџЏюЮъЪаАбБцЦдДеЕфФгГ«»░▒▓│┤хХиИ╣║╗╝йЙ┐└┴┬├─┼кК╚╔╩╦╠═╬¤лЛмМнНоОп┘┌█▄Пя▀ЯрРсСтТуУжЖвВьЬ№­ыЫзЗшШэЭщЩчЧ§■ "
    ),
    "cp857": CodePageDefinition(
      name: "Turkish",
      languages: ["tr"],
      offset: 128,
      chars:
        "ÇüéâäàåçêëèïîıÄÅÉæÆôöòûùİÖÜø£ØŞşáíóúñÑĞğ¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤ºªÊËÈÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµ×ÚÛÙìÿ¯´­±¾¶§÷¸°¨·¹³²■ "
    ),
    "cp858": CodePageDefinition(
      name: "Euro",
      languages: ["en"],
      offset: 128,
      chars:
        "ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤ðÐÊËÈ€ÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµþÞÚÛÙýÝ¯´­±‗¾¶§÷¸°¨·¹³²■ "
    ),
    "cp860": CodePageDefinition(
      name: "Portuguese",
      languages: ["pt"],
      offset: 128,
      chars:
        "ÇüéâãàÁçêÊèÍÔìÃÂÉÀÈôõòÚùÌÕÜ¢£Ù₧ÓáíóúñÑªº¿Ò¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp861": CodePageDefinition(
      name: "Icelandic",
      languages: ["is"],
      offset: 128,
      chars:
        "ÇüéâäàåçêëèÐðÞÄÅÉæÆôöþûÝýÖÜø£Ø₧ƒáíóúÁÍÓÚ¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp862": CodePageDefinition(
      name: "Hebrew",
      languages: ["he"],
      offset: 128,
      chars:
        "אבגדהוזחטיךכלםמןנסעףפץצקרשת¢£¥₧ƒáíóúñÑªº¿⌐¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp863": CodePageDefinition(
      name: "Canadian French",
      languages: ["fr"],
      offset: 128,
      chars:
        "ÇüéâÂà¶çêëèïî‗À§ÉÈÊôËÏûù¤ÔÜ¢£ÙÛƒ¦´óú¨¸³¯Î⌐¬½¼¾«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp864": CodePageDefinition(
      name: "Arabic",
      languages: ["ar"],
      offset: 0,
      chars:
        #"\x80\x81éâ\x84à\x86çêëèïî\x8d\x8e\x8f\x90\u0651\u0652ô¤ـûùءآأؤ£إئابةتثجحخدذرزسشص«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀ضطظعغفµقكلمنهوىي≡\u064b\u064c\u064d\u064e\u064f\u0650≈°∙·√ⁿ²■\u00a0"#
    ),
    "cp865": CodePageDefinition(
      name: "Nordic",
      languages: ["sv", "dk"],
      offset: 128,
      chars:
        "ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø₧ƒáíóúñÑªº¿⌐¬½¼¡«¤░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ "
    ),
    "cp866": CodePageDefinition(
      name: "Cyrillic 2",
      languages: ["ru"],
      offset: 128,
      chars:
        "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмноп░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀рстуфхцчшщъыьэюяЁёЄєЇїЎў°∙·√№¤■"
    ),
    "cp869": CodePageDefinition(
      name: "Greek",
      languages: ["el"],
      offset: 128,
      chars:
        "Ά·¬¦''Έ―ΉΊΪΌΎΫ©Ώ²³ά£έήίϊΐόύΑΒΓΔΕΖΗ½ΘΙ«»░▒▓│┤ΚΛΜΝ╣║╗╝ΞΟ┐└┴┬├─┼ΠΡ╚╔╩╦╠═╬ΣΤΥΦΧΨΩαβγ┘┌█▄δε▀ζηθικλμνξοπρσςτ΄­±υφχ§ψ΅°¨ωϋΰώ■ "
    ),
    "cp874": CodePageDefinition(
      name: "Thai",
      languages: ["th"],
      offset: 128,
      chars:
        "€����…�����������‘’“”•–—�������� กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮฯะัาำิีึืฺุู����฿เแโใไๅๆ็่้๊๋์ํ๎๏๐๑๒๓๔๕๖๗๘๙๚๛����"
    ),
    "cp932": CodePageDefinition(
      name: "Japanese",
      languages: ["jp"],
      offset: 128,
      chars: "｡｢｣､･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ"
    ),
    "cp936": CodePageDefinition(
      name: "Simplified Chinese",
      languages: ["zh"],
      offset: 128,
      chars:
        "啊阿埃挨哎唉哀皑癌艾爱隘鞍氨安俺按暗岸胺案肮昂盎凹敖熬翱袄傲奥懊澳芭捌扒叭吧笆八疤巴拔跋靶把耙坝霸罢爸白柏百摆佰败拜稗斑班搬扳般颁板版扮拌伴瓣半办绊邦帮梆榜膀绑棒磅蚌镑傍谤苞胞包褒剥"
    ),
    "cp949": CodePageDefinition(
      name: "Korean",
      languages: ["ko"],
      offset: 128,
      chars:
        "가각간갇갈갉갊감갑값갓갔강갖갗같갚갛개객갠갤갬갭갯갰갱갸갹갼걀걋걍걔걘걜거걱건걷걸걺검겁것겄겅겆겉겊겋게겐겔겜겝겟겠겡겨격겪견겯결겸겹겻겼경곁계곈곌곕곗고곡곤곧골곪곬곯곰곱곳공곶과곽관괄"
    ),
    "cp950": CodePageDefinition(
      name: "Traditional Chinese",
      languages: ["zh"],
      offset: 128,
      chars:
        "一丁七丈三上不丐且丕世丘丙業叢東絲丞丟並丨丫中丰串臨丸丹主乃久么么之烏乍乎乏樂乒乓喬乖乘乙九乞也習鄉書買亂乳乾了予爭事二于云互五井亞些亟交"
    ),
    "cp1250": CodePageDefinition(
      name: "Central Europe",
      languages: ["hu", "pl", "cz"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡ˆ‰Š‹ŚŤŽŹ'""•–—™š›śťžźˇ˘Ł¤¦§¨©«¬®°±²³´µ¶·¸¹»ŒœŸ¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĎĐÑÒÓÔÕÖ×ØÙÚÛÜÝŔŘßàáâãäåæçèéêëìíîïďđñòóôõö÷øùúûüýŕřÿ"#
    ),
    "cp1251": CodePageDefinition(
      name: "Cyrillic",
      languages: ["ru", "bg"],
      offset: 128,
      chars:
        #"ЂЃ‚ѓ„…†‡€‰Љ‹ЊЌЋЏђ‘'""•–—™љ›њќћџЎўЈ¤Ґ¦§Ё©«¬®Ї°±Ііґµ¶·ё№»јЅѕїАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя"#
    ),
    "cp1252": CodePageDefinition(
      name: "Latin I",
      languages: ["en", "fr", "de"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡ˆ‰Š‹ŒŽŸ''""•–—™š›œžŸ¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ"#
    ),
    "cp1253": CodePageDefinition(
      name: "Greek",
      languages: ["el"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡€‰‹""•–—™›""΅Ά£¤¥¦§¨©«¬®―°±²³΄µ¶·ΈΉΊ»Ό½ΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώ"#
    ),
    "cp1254": CodePageDefinition(
      name: "Turkish",
      languages: ["tr"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡ˆ‰Š‹ŒŽŸ''""•–—™š›œžŸ¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜİŞßàáâãäåæçèéêëìíîïğñòóôõö÷øùúûüışÿ"#
    ),
    "cp1255": CodePageDefinition(
      name: "Hebrew",
      languages: ["he"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡ˆ‰‹""•–—™›""¡¢£₪¥¦§¨©×«¬®¯°±²³´µ¶·¸¹÷»¼½¾¿אבגדהוזחטיךכלםמןנסעףפץצקרשתװױײ׳״"#
    ),
    "cp1256": CodePageDefinition(
      name: "Arabic",
      languages: ["ar"],
      offset: 128,
      chars:
        #"€پ‚ƒ„…†‡ˆ‰ٹ‹Œچژڈگ''""•–—™ڑ›œں،؛ڈ°¢£¤¥¦§¨©ھ«¬®¯°±²³´µ¶·¸¹؛»¼½¾؟ہءآأؤإئابةتثجحخدذرزسشصضطظعغفقكلمنهوىيًٌٍَُِّْ"#
    ),
    "cp1257": CodePageDefinition(
      name: "Baltic",
      languages: ["et", "lt", "lv"],
      offset: 128,
      chars:
        #"€‚ƒ„…†‡€‰‹""•–—™›""¡¢£¤¥¦§Ø©Ŗ«¬®Æ°±²³"µ¶·ø¹ŗ»¼½¾æĄĮĀĆÄÅÆĒČÉĘĖĢĶĪĻŠŃŅÓŌÕÖ×ŲŁŚŪÜŻŽßąįāćäåæēčéęėģķīļšńņóōõö÷ųłśūüżž˙"#
    ),
    "windows1258": CodePageDefinition(
      name: "Vietnamese",
      languages: ["vi"],
      offset: 128,
      chars:
        #"€�‚ƒ„…†‡ˆ‰�‹Œ����‘’“”•–—˜™�›œ��Ÿ ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂĂÄÅÆÇÈÉÊË̀ÍÎÏĐÑ̉ÓÔƠÖ×ØÙÚÛÜỮßàáâăäåæçèéêë́íîïđṇ̃óôơö÷øùúûüư₫ÿ"#
    ),
  ]

  public static func getDefinition(for codePage: String) -> CodePageDefinition?
  {
    return definitions[codePage]
  }

  public static func getAvailableCodePages() -> [String] {
    return Array(definitions.keys)
  }

  public static func getCodePagesForLanguage(_ language: String) -> [String] {
    return definitions.filter { $0.value.languages.contains(language) }.map {
      $0.key
    }
  }
}
