import Foundation

public struct Charset {
    private static let specialChars: [Character: [UInt8]] = [
        "Ã": [65, 222],
        "ã": [97, 222],
        "Õ": [79, 222],
        "õ": [111, 222],
        "Ẫ": [194, 222],
        "ẫ": [226, 222],
        "Ẽ": [69, 222],
        "ẽ": [101, 222],
        "Ĩ": [73, 222],
        "ĩ": [105, 222],
        "Ũ": [85, 222],
        "ũ": [117, 222],
        "Ẵ": [195, 222],
        "ẵ": [227, 222],
        "Ễ": [202, 222],
        "ễ": [234, 222],
        "Ỗ": [212, 222],
        "ỗ": [244, 222],
        "Ỡ": [213, 222],
        "ỡ": [245, 222],
        "Ữ": [221, 222],
        "ữ": [253, 222],
        "Ỹ": [89, 222],
        "ỹ": [121, 222],
        "Ì": [73, 204],
        "ì": [105, 204],
        "Ò": [79, 204],
        "ò": [111, 204],
        "Ầ": [194, 204],
        "ầ": [226, 204],
        "Ằ": [195, 204],
        "ằ": [227, 204],
        "Ề": [202, 204],
        "ề": [234, 204],
        "Ồ": [212, 204],
        "ồ": [244, 204],
        "Ờ": [213, 204],
        "ờ": [245, 204],
        "Ừ": [221, 204],
        "ừ": [253, 204],
        "Ỳ": [89, 204],
        "ỳ": [121, 204],
        "Ị": [73, 242],
        "ị": [105, 242],
        "Ọ": [79, 242],
        "ọ": [111, 242],
        "Ạ": [65, 242],
        "ạ": [97, 242],
        "Ậ": [194, 242],
        "ậ": [226, 242],
        "Ặ": [195, 242],
        "ặ": [227, 242],
        "Ẹ": [69, 242],
        "ẹ": [101, 242],
        "Ệ": [202, 242],
        "ệ": [234, 242],
        "Ộ": [212, 242],
        "ộ": [244, 242],
        "Ợ": [213, 242],
        "ợ": [245, 242],
        "Ụ": [85, 242],
        "ụ": [117, 242],
        "Ự": [221, 242],
        "ự": [253, 242],
        "Ỵ": [89, 242],
        "ỵ": [121, 242],
        "Ỏ": [79, 210],
        "ỏ": [111, 210],
        "Ỉ": [73, 210],
        "ỉ": [105, 210],
        "Ả": [65, 210],
        "ả": [97, 210],
        "Ẩ": [194, 210],
        "ẩ": [226, 210],
        "Ẳ": [195, 210],
        "ẳ": [227, 210],
        "Ẻ": [69, 210],
        "ẻ": [101, 210],
        "Ể": [202, 210],
        "ể": [234, 210],
        "Ổ": [212, 210],
        "ổ": [244, 210],
        "Ở": [213, 210],
        "ở": [245, 210],
        "Ủ": [85, 210],
        "ủ": [117, 210],
        "Ử": [221, 210],
        "ử": [253, 210],
        "Ỷ": [89, 210],
        "ỷ": [121, 210],
        "Ý": [89, 236],
        "ý": [121, 236],
        "Ấ": [194, 236],
        "ấ": [226, 236],
        "Ắ": [195, 236],
        "ắ": [227, 236],
        "Ế": [202, 236],
        "ế": [234, 236],
        "Ố": [212, 236],
        "ố": [244, 236],
        "Ớ": [213, 236],
        "ớ": [245, 236],
        "Ứ": [221, 236],
        "ứ": [253, 236]
    ]
    
    public static func encode(_ input: String, hasQuote: Bool = false) -> [UInt8] {
        var output: [UInt8] = []
        let chars = CodePageDefinitions.definitions["windows1258"]?.chars ?? ""
        let offset = CodePageDefinitions.definitions["windows1258"]?.offset ?? 0
        
        for char in input {
            if let codepoint = char.unicodeScalars.first?.value {
                if codepoint < 128 {
                    output.append(UInt8(codepoint))
                } else {
                    if let position = chars.firstIndex(of: char) {
                        let distance = chars.distance(from: chars.startIndex, to: position)
                        output.append(UInt8(offset + distance))
                    } else if codepoint < 256 && (codepoint < offset || codepoint >= offset + chars.count) {
                        output.append(UInt8(codepoint))
                    } else if let specialChar = specialChars[char] {
                        if hasQuote {
                            output.append(contentsOf: specialChar)
                        } else {
                            output.append(specialChar[0])
                        }
                    } else {
                        output.append(0x3f) // Question mark
                    }
                }
            }
        }
        
        return output
    }
} 
