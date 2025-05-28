import Foundation

struct Fragment {
  let codepage: String
  var bytes: [UInt8]
}

class CodePageEncoder {
  static func supports(codepage: String) -> Bool {
    guard let definition = CodePageDefinitions.definitions[codepage] else {
      return false
    }
    return !definition.chars.isEmpty
  }

  static func encode(input: String, codepage: String) -> [UInt8] {
    if codepage == "windows1258" {
      var output = [UInt8](repeating: 0, count: input.count)
      let chars = CodePageDefinitions.definitions["windows1258"]?.chars ?? ""
      let offset = CodePageDefinitions.definitions["windows1258"]?.offset ?? 128

      for (c, char) in input.enumerated() {
        let codepoint = char.unicodeScalars.first?.value ?? 0

        if codepoint < 128 {
          output[c] = UInt8(codepoint)
        } else {
          if let position = chars.firstIndex(of: char) {
            let distance = chars.distance(from: chars.startIndex, to: position)
            output[c] = UInt8(offset + distance)
          } else if codepoint < 256
            && (codepoint < offset || codepoint >= offset + chars.count)
          {
            output[c] = UInt8(codepoint)
          } else {
            output[c] = 0x3f  // Question mark for unsupported characters
          }
        }
      }
      return output
    }

    var output = [UInt8](repeating: 0, count: input.count)
    var chars = String(repeating: "\u{0000}", count: 128)
    var offset: Int = 128

    if let definition = CodePageDefinitions.definitions[codepage] {
      chars = definition.chars
      offset = definition.offset
    }

    for (c, char) in input.enumerated() {
      let codepoint = char.unicodeScalars.first?.value ?? 0

      if codepoint < 128 {
        output[c] = UInt8(codepoint)
      } else {
        if let position = chars.firstIndex(of: char) {
          output[c] = UInt8(
            offset + chars.distance(from: chars.startIndex, to: position))
        } else if codepoint < 256
          && (codepoint < offset || codepoint >= offset + chars.count)
        {
          output[c] = UInt8(codepoint)
        } else {
          output[c] = 0x3f
        }
      }
    }

    return output
  }

  static func autoEncode(input: String, candidates: [String]) -> [Fragment] {
    var fragments: [Fragment] = []
    var fragment = -1
    var current: String?
    var notFoundCount = 0

    for (c, char) in input.enumerated() {
      let codepoint = char.unicodeScalars.first?.value ?? 0

      var available: String?
      var charByte: UInt8 = 0

      if codepoint < 128 {
        available = current ?? candidates.first
        charByte = UInt8(codepoint)
      }

      if available == nil && current != nil {
        if let definition = CodePageDefinitions.definitions[current!],
          let position = definition.chars.firstIndex(of: char)
        {
          available = current
          charByte = UInt8(
            definition.offset
              + definition.chars.distance(
                from: definition.chars.startIndex, to: position))
        }
      }

      if available == nil {
        for candidate in candidates {
          if let definition = CodePageDefinitions.definitions[candidate],
            let position = definition.chars.firstIndex(of: char)
          {
            available = candidate
            charByte = UInt8(
              definition.offset
                + definition.chars.distance(
                  from: definition.chars.startIndex, to: position))
            break
          }
        }
      }

      if available == nil {
        available = current ?? candidates.first
        charByte = 0x3f
        notFoundCount += 1
        let rem = removeVNCharacters(String(char))
        if let firstScalar = rem.unicodeScalars.first {
          charByte = UInt8(firstScalar.value)
        }
      }

      if current != available {
        if current != nil {
          fragments[fragment].bytes = Array(fragments[fragment].bytes)
        }

        fragment += 1
        fragments.append(Fragment(codepage: available!, bytes: []))
        current = available
      }

      fragments[fragment].bytes.append(charByte)
    }

    if current != nil {
      fragments[fragment].bytes = Array(fragments[fragment].bytes)
    }

    return fragments
  }

  private static func removeVNCharacters(_ str: String) -> String {
    return str
      .decomposedStringWithCanonicalMapping
      .replacingOccurrences(
        of: "[\\u0300-\\u036f]", with: "", options: .regularExpression
      )
      .replacingOccurrences(of: "đ", with: "d")
      .replacingOccurrences(of: "Đ", with: "D")
  }
}
