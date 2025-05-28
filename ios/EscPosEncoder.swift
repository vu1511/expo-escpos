import Foundation

class EscPosEncoder {
  private var options: [String: Any]
  private var embedded: Bool
  private var buffer: [UInt8]
  private var queued: [UInt8]
  private var cursor: Int
  private var codepage: String
  private var state: [String: Any]

  private let codepageMappings: [String: [String: UInt8]] = [
    "epson": [
      "cp437": 0x00,
      "shiftjis": 0x01,
      "cp850": 0x02,
      "cp860": 0x03,
      "cp863": 0x04,
      "cp865": 0x05,
      "cp851": 0x0b,
      "cp853": 0x0c,
      "cp857": 0x0d,
      "cp737": 0x0e,
      "iso88597": 0x0f,
      "windows1252": 0x10,
      "cp866": 0x11,
      "cp852": 0x12,
      "cp858": 0x13,
      "cp720": 0x20,
      "cp775": 0x21,
      "cp855": 0x22,
      "cp861": 0x23,
      "cp862": 0x24,
      "cp864": 0x25,
      "cp869": 0x26,
      "iso88592": 0x27,
      "iso885915": 0x28,
      "cp1098": 0x29,
      "cp1118": 0x2a,
      "cp1119": 0x2b,
      "cp1125": 0x2c,
      "windows1250": 0x2d,
      "windows1251": 0x2e,
      "windows1253": 0x2f,
      "windows1254": 0x30,
      "windows1255": 0x31,
      "windows1256": 0x32,
      "windows1257": 0x33,
      "windows1258": 0x34,
      "rk1048": 0x35,
    ],
    "zjiang": [
      "cp437": 0x00,
      "shiftjis": 0x01,
      "cp850": 0x02,
      "cp860": 0x03,
      "cp863": 0x04,
      "cp865": 0x05,
      "windows1252": 0x10,
      "cp866": 0x11,
      "cp852": 0x12,
      "cp858": 0x13,
      "windows1255": 0x20,
      "cp861": 0x38,
      "cp855": 0x3c,
      "cp857": 0x3d,
      "cp862": 0x3e,
      "cp864": 0x3f,
      "cp737": 0x40,
      "cp851": 0x41,
      "cp869": 0x42,
      "cp1119": 0x44,
      "cp1118": 0x45,
      "windows1250": 0x48,
      "windows1251": 0x49,
      "cp3840": 0x4a,
      "cp3843": 0x4c,
      "cp3844": 0x4d,
      "cp3845": 0x4e,
      "cp3846": 0x4f,
      "cp3847": 0x50,
      "cp3848": 0x51,
      "cp2001": 0x53,
      "cp3001": 0x54,
      "cp3002": 0x55,
      "cp3011": 0x56,
      "cp3012": 0x57,
      "cp3021": 0x58,
      "cp3041": 0x59,
      "windows1253": 0x5a,
      "windows1254": 0x5b,
      "windows1256": 0x5c,
      "cp720": 0x5d,
      "windows1258": 0x5e,
      "cp775": 0x5f,
    ],
    "bixolon": [
      "cp437": 0x00,
      "shiftjis": 0x01,
      "cp850": 0x02,
      "cp860": 0x03,
      "cp863": 0x04,
      "cp865": 0x05,
      "cp851": 0x0b,
      "cp858": 0x13,
    ],
    "star": [
      "cp437": 0x00,
      "shiftjis": 0x01,
      "cp850": 0x02,
      "cp860": 0x03,
      "cp863": 0x04,
      "cp865": 0x05,
      "windows1252": 0x10,
      "cp866": 0x11,
      "cp852": 0x12,
      "cp858": 0x13,
    ],
    "citizen": [
      "cp437": 0x00,
      "shiftjis": 0x01,
      "cp850": 0x02,
      "cp860": 0x03,
      "cp863": 0x04,
      "cp865": 0x05,
      "cp852": 0x12,
      "cp866": 0x11,
      "cp857": 0x08,
      "windows1252": 0x10,
      "cp858": 0x13,
      "cp864": 0x28,
    ],
    "legacy": [
      "cp437": 0x00,
      "cp737": 0x40,
      "cp850": 0x02,
      "cp775": 0x5f,
      "cp852": 0x12,
      "cp855": 0x3c,
      "cp857": 0x3d,
      "cp858": 0x13,
      "cp860": 0x03,
      "cp861": 0x38,
      "cp862": 0x3e,
      "cp863": 0x04,
      "cp864": 0x1c,
      "cp865": 0x05,
      "cp866": 0x11,
      "cp869": 0x42,
      "cp936": 0xff,
      "cp949": 0xfd,
      "cp950": 0xfe,
      "cp1252": 0x10,
      "iso88596": 0x16,
      "shiftjis": 0xfc,
      "windows874": 0x1e,
      "windows1250": 0x48,
      "windows1251": 0x49,
      "windows1252": 0x47,
      "windows1253": 0x5a,
      "windows1254": 0x5b,
      "windows1255": 0x20,
      "windows1256": 0x5c,
      "windows1257": 0x19,
      "windows1258": 0x5e,
    ],
  ]

  init(options: [String: Any] = [:]) {
    self.options = [
      "width": nil as Any?,
      "embedded": false,
      "wordWrap": true,
      "imageMode": "column",
      "codepageMapping": "epson",
      "codepageCandidates": [
        "cp437",
        "cp858",
        "cp860",
        "cp861",
        "cp863",
        "cp865",
        "cp852",
        "cp857",
        "cp855",
        "cp866",
        "cp869",
      ],
    ]

    // Merge provided options with defaults
    for (key, value) in options {
      self.options[key] = value
    }

    self.embedded =
      (self.options["width"] as? Int) != nil
      && (self.options["embedded"] as? Bool) == true
    self.buffer = []
    self.queued = []
    self.cursor = 0
    self.codepage = "ascii"

    self.state = [
      "codepage": 0,
      "align": "left",
      "bold": false,
      "italic": false,
      "underline": false,
      "invert": false,
      "width": 1,
      "height": 1,
    ]
  }

  private func reset(options: [String: Any] = [:]) {
    self.options = [
      "width": nil as Any?,
      "embedded": false,
      "wordWrap": true,
      "imageMode": "column",
      "codepageMapping": "epson",
      "codepageCandidates": [
        "cp437",
        "cp858",
        "cp860",
        "cp861",
        "cp863",
        "cp865",
        "cp852",
        "cp857",
        "cp855",
        "cp866",
        "cp869",
      ],
    ]

    // Merge provided options with defaults
    for (key, value) in options {
      self.options[key] = value
    }

    self.embedded =
      (self.options["width"] as? Int) != nil
      && (self.options["embedded"] as? Bool) == true
    self.buffer = []
    self.queued = []
    self.cursor = 0
    self.codepage = "ascii"

    self.state = [
      "codepage": 0,
      "align": "left",
      "bold": false,
      "italic": false,
      "underline": false,
      "invert": false,
      "width": 1,
      "height": 1,
    ]
  }

  private func encode(_ value: String) -> [UInt8] {
    if self.codepage != "auto" {
      return CodePageEncoder.encode(input: value, codepage: self.codepage)
    }

    var codepages: [String: UInt8]

    if let mapping = self.options["codepageMapping"] as? String {
      codepages = self.codepageMappings[mapping] ?? [:]
    } else if let mapping = self.options["codepageMapping"] as? [String: UInt8]
    {
      codepages = mapping
    } else {
      codepages = [:]
    }

    let fragments = CodePageEncoder.autoEncode(
      input: value,
      candidates: self.options["codepageCandidates"] as? [String] ?? [])

    var length = 0
    for fragment in fragments {
      length += 3 + fragment.bytes.count
    }

    var result = [UInt8](repeating: 0, count: length)
    var index = 0

    for fragment in fragments {
      result[index] = 0x1b
      result[index + 1] = 0x74
      result[index + 2] = codepages[fragment.codepage] ?? 0
      result.replaceSubrange(
        (index + 3)..<(index + 3 + fragment.bytes.count), with: fragment.bytes)
      index += 3 + fragment.bytes.count

      self.state["codepage"] = codepages[fragment.codepage] ?? 0
    }

    return result
  }

  private func queue(_ value: [UInt8]) {
    self.queued.append(contentsOf: value)
  }

  private func flush() {
    if self.embedded {
      let width = self.options["width"] as? Int ?? 0
      let indent = width - self.cursor

      if self.state["align"] as? String == "left" {
        self.queued.append(contentsOf: Array(repeating: 0x20, count: indent))
      }

      if self.state["align"] as? String == "center" {
        let remainder = indent % 2
        let indent = indent >> 1

        if indent > 0 {
          self.queued.append(contentsOf: Array(repeating: 0x20, count: indent))
        }

        if indent + remainder > 0 {
          self.queued.insert(
            contentsOf: Array(repeating: 0x20, count: indent + remainder), at: 0
          )
        }
      }

      if self.state["align"] as? String == "right" {
        self.queued.insert(
          contentsOf: Array(repeating: 0x20, count: indent), at: 0)
      }
    }

    self.buffer.append(contentsOf: self.queued)
    self.queued = []
    self.cursor = 0
  }

  private func wrap(_ value: String, position: Int? = nil) -> [String] {
    // If no position is specified and word wrap is disabled or no width is set, return the value as is
    if position == nil
      && (!(options["wordWrap"] as? Bool ?? true) || options["width"] == nil)
    {
      return [value]
    }

    // Get the width to wrap at (either specified position or default width)
    let width = position ?? (options["width"] as? Int ?? 0)

    // Create indent string based on cursor position
    let indent = String(repeating: "-", count: cursor)

    // Combine indent and value
    let fullText = indent + value

    // Split into lines
    var lines: [String] = []
    var currentLine = ""
    var currentLength = 0

    // Split by words
    let words = fullText.components(separatedBy: .whitespaces)

    for word in words {
      let wordLength = word.count

      // If adding this word would exceed the width, start a new line
      if currentLength + wordLength + (currentLine.isEmpty ? 0 : 1) > width {
        if !currentLine.isEmpty {
          lines.append(currentLine)
        }
        currentLine = word
        currentLength = wordLength
      } else {
        // Add word to current line
        if !currentLine.isEmpty {
          currentLine += " "
          currentLength += 1
        }
        currentLine += word
        currentLength += wordLength
      }
    }

    // Add the last line if it's not empty
    if !currentLine.isEmpty {
      lines.append(currentLine)
    }

    // Remove the indent from the beginning of each line
    let result = lines.map { line in
      if line.hasPrefix(indent) {
        return String(line.dropFirst(indent.count))
      }
      return line
    }

    return result
  }

  private func restoreState() {
    self.bold(self.state["bold"] as? Bool ?? false)
      .italic(self.state["italic"] as? Bool ?? false)
      .underline(self.state["underline"] as? Bool ?? false)
      .invert(self.state["invert"] as? Bool ?? false)

    self.queue([0x1b, 0x74, self.state["codepage"] as? UInt8 ?? 0])
  }

  private func getCodepageIdentifier(_ codepage: String) -> UInt8 {
    var codepages: [String: UInt8]

    if let mapping = self.options["codepageMapping"] as? String {
      codepages = self.codepageMappings[mapping] ?? [:]
    } else if let mapping = self.options["codepageMapping"] as? [String: UInt8]
    {
      codepages = mapping
    } else {
      codepages = [:]
    }

    return codepages[codepage] ?? 0
  }

  func initialize() -> EscPosEncoder {
    self.queue([0x1b, 0x40])
    self.flush()
    return self
  }

  func codepage(_ codepage: String) -> EscPosEncoder {
    if codepage == "auto" {
      self.codepage = codepage
      return self
    }

    if !CodePageEncoder.supports(codepage: codepage) {
      fatalError("Unknown codepage")
    }

    var codepages: [String: UInt8]

    if let mapping = self.options["codepageMapping"] as? String {
      codepages = self.codepageMappings[mapping] ?? [:]
    } else if let mapping = self.options["codepageMapping"] as? [String: UInt8]
    {
      codepages = mapping
    } else {
      codepages = [:]
    }

    if let code = codepages[codepage] {
      self.codepage = codepage
      self.state["codepage"] = code
      self.queue([0x1b, 0x74, code])
    } else {
      fatalError("Codepage not supported by printer")
    }

    return self
  }

  func text(_ value: String, wrap: Int? = nil) -> EscPosEncoder {
    let lines = self.wrap(value, position: wrap)

    for (index, line) in lines.enumerated() {
      let bytes = self.encode(line)
      if (self.options["width"] as? Int) == 46
        && (self.state["align"] as? String) != "center"
      {
        // k80 margin 2 space
        self.queue(Array(repeating: 0x20, count: 2))
      }
      self.queue(bytes)

      self.cursor += line.count * (self.state["width"] as? Int ?? 1)

      if let width = self.options["width"] as? Int, !self.embedded {
        self.cursor = self.cursor % width
      }

      if index < lines.count - 1 {
        self.newline()
      }
    }

    return self
  }

  func newline() -> EscPosEncoder {
    self.flush()
    self.queue([0x0a, 0x0d])

    if self.embedded {
      self.restoreState()
    }

    return self
  }

  func line(_ value: String, wrap: Int? = nil) -> EscPosEncoder {
    self.text(value, wrap: wrap)
    self.newline()
    return self
  }

  func underline(_ value: Bool? = nil) -> EscPosEncoder {
    let newValue = value ?? !(self.state["underline"] as? Bool ?? false)
    self.state["underline"] = newValue
    self.queue([0x1b, 0x2d, newValue ? 1 : 0])
    return self
  }

  func italic(_ value: Bool? = nil) -> EscPosEncoder {
    let newValue = value ?? !(self.state["italic"] as? Bool ?? false)
    self.state["italic"] = newValue
    self.queue([0x1b, 0x34, newValue ? 1 : 0])
    return self
  }

  func bold(_ value: Bool? = nil) -> EscPosEncoder {
    let newValue = value ?? !(self.state["bold"] as? Bool ?? false)
    self.state["bold"] = newValue
    self.queue([0x1b, 0x45, newValue ? 1 : 0])
    return self
  }

  func width(_ width: Int = 1) -> EscPosEncoder {
    if width < 1 || width > 8 {
      fatalError("Width must be between 1 and 8")
    }

    self.state["width"] = width

    let height = self.state["height"] as? Int ?? 1
    self.queue([0x1d, 0x21, UInt8((height - 1) | ((width - 1) << 4))])

    return self
  }

  func height(_ height: Int = 1) -> EscPosEncoder {
    if height < 1 || height > 8 {
      fatalError("Height must be between 1 and 8")
    }

    self.state["height"] = height

    let width = self.state["width"] as? Int ?? 1
    self.queue([0x1d, 0x21, UInt8((height - 1) | ((width - 1) << 4))])

    return self
  }

  func invert(_ value: Bool? = nil) -> EscPosEncoder {
    let newValue = value ?? !(self.state["invert"] as? Bool ?? false)
    self.state["invert"] = newValue
    self.queue([0x1d, 0x42, newValue ? 1 : 0])
    return self
  }

  func size(_ value: String) -> EscPosEncoder {
    let sizeValue: UInt8 = value == "small" ? 0x01 : 0x00
    self.queue([0x1b, 0x4d, sizeValue])
    return self
  }

  func align(_ value: String) -> EscPosEncoder {
    let alignments: [String: UInt8] = [
      "left": 0x00,
      "center": 0x01,
      "right": 0x02,
    ]

    if let alignment = alignments[value] {
      self.state["align"] = value

      if !self.embedded {
        self.queue([0x1b, 0x61, alignment])
      }
    } else {
      fatalError("Unknown alignment")
    }

    return self
  }

  func cut(_ value: String? = nil) -> EscPosEncoder {
    if self.embedded {
      fatalError("Cut is not supported in table cells or boxes")
    }

    let data: UInt8 = value == "partial" ? 0x01 : 0x00
    self.queue([0x1d, 0x56, data])

    return self
  }

  func pulse(_ device: Int = 0, on: Int = 100, off: Int = 500) -> EscPosEncoder
  {
    if self.embedded {
      fatalError("Pulse is not supported in table cells or boxes")
    }

    let onValue = min(500, Int(round(Double(on) / 2.0)))
    let offValue = min(500, Int(round(Double(off) / 2.0)))

    self.queue([
      0x1b, 0x70, device != 0 ? 1 : 0, UInt8(onValue & 0xff),
      UInt8(offValue & 0xff),
    ])

    return self
  }

  func raw(_ data: [UInt8]) -> EscPosEncoder {
    self.queue(data)
    return self
  }

  func encode() -> [UInt8] {
    self.flush()

    var result: [UInt8] = []

    for item in self.buffer {
      result.append(item)
    }

    self.reset()

    return result
  }

  public func image(_ base64: String) -> EscPosEncoder {
    guard let image = Image.load(from: base64) else {
      // Could throw or log an error here if desired
      return self
    }
    let (rasterData, widthBytes, height) = image.toRaster()
    // widthBytes is the number of bytes per row (width in pixels / 8)
    // height is the number of rows
    var command: [UInt8] = [0x1d, 0x76, 0x30, 0]
    // widthBytes and height are UInt16, little endian
    command.append(UInt8(widthBytes & 0xff))
    command.append(UInt8((widthBytes >> 8) & 0xff))
    command.append(UInt8(height & 0xff))
    command.append(UInt8((height >> 8) & 0xff))
    command.append(contentsOf: rasterData)
    self.queue(command)
    self.flush()
    return self
  }

  public func imageData(_ image: Data) -> EscPosEncoder {
    guard let image = Image.loadFromData(from: image) else {
      // Could throw or log an error here if desired
      return self
    }
    let (rasterData, widthBytes, height) = image.toRaster()
    // widthBytes is the number of bytes per row (width in pixels / 8)
    // height is the number of rows
    var command: [UInt8] = [0x1d, 0x76, 0x30, 0]
    // widthBytes and height are UInt16, little endian
    command.append(UInt8(widthBytes & 0xff))
    command.append(UInt8((widthBytes >> 8) & 0xff))
    command.append(UInt8(height & 0xff))
    command.append(UInt8((height >> 8) & 0xff))
    command.append(contentsOf: rasterData)
    self.queue(command)
    self.flush()
    return self
  }
}
