import CoreImage
import Foundation
import UIKit

public class Image {
  // MARK: - Properties
  private var pixels: [[UInt8]]
  private var data: [UInt8]
  private var width: Int
  private var height: Int
  private var colors: Int

  // MARK: - Initialization
  public init(pixels: [[UInt8]], width: Int, height: Int, colors: Int) {
    self.pixels = pixels
    self.width = width
    self.height = height
    self.colors = colors

    // Convert pixels to binary data
    self.data = pixels.map { pixel in
      if pixel.count < 4 || pixel[3] == 0 { return 0 }
      let shouldBeWhite = pixel[0] > 200 && pixel[1] > 200 && pixel[2] > 200
      return shouldBeWhite ? 0 : 1
    }
  }

  // MARK: - Public Methods
  public func toBitmap(density: Int = 24) -> (data: [[UInt8]], density: Int) {
    var result: [[UInt8]] = []
    let c = density / 8

    // n blocks of lines
    let n = Int(ceil(Double(height) / Double(density)))

    for y in 0..<n {
      var ld: [UInt8] = []

      for x in 0..<width {
        for b in 0..<density {
          let i = x * c + (b >> 3)

          while ld.count <= i {
            ld.append(0)
          }

          let l = y * density + b
          if l < height {
            if data[l * width + x] == 1 {
              ld[i] |= UInt8(0x80 >> (b & 0x7))
            }
          }
        }
      }
      result.append(ld)
    }

    return (result, density)
  }

  public func toRaster() -> (data: [UInt8], width: Int, height: Int) {
    var result: [UInt8] = []

    // n blocks of lines
    let n = Int(ceil(Double(width) / 8.0))

    for y in 0..<height {
      for x in 0..<n {
        var byte: UInt8 = 0

        for b in 0..<8 {
          let i = x * 8 + b
          let c = x * 8 + b

          if c < width {
            if data[y * width + i] == 1 {
              byte |= UInt8(0x80 >> (b & 0x7))
            }
          }
        }
        result.append(byte)
      }
    }

    return (result, n, height)
  }

  // MARK: - Static Methods
  public static func load(from base64PngImage: String) -> Image? {
    guard let imageData = Data(base64Encoded: base64PngImage),
      let uiImage = UIImage(data: imageData)
    else {
      return nil
    }
    
    return self.loadFromData(from: imageData)
  }

  public static func loadFromData(from imageData: Data) -> Image? {
    guard let uiImage = UIImage(data: imageData) else {
      return nil
    }

    // Convert UIImage to pixel data
    guard let cgImage = uiImage.cgImage else { return nil }

    let width = cgImage.width
    let height = cgImage.height
    let bytesPerPixel = 4
    let bytesPerRow = width * bytesPerPixel
    let bitsPerComponent = 8

    var rawData = [UInt8](repeating: 0, count: width * height * bytesPerPixel)

    guard
      let context = CGContext(
        data: &rawData,
        width: width,
        height: height,
        bitsPerComponent: bitsPerComponent,
        bytesPerRow: bytesPerRow,
        space: CGColorSpaceCreateDeviceRGB(),
        bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue
      )
    else { return nil }

    context.draw(cgImage, in: CGRect(x: 0, y: 0, width: width, height: height))

    // Convert raw data to pixels array
    var pixels: [[UInt8]] = []
    for i in stride(from: 0, to: rawData.count, by: bytesPerPixel) {
      let pixel = Array(rawData[i..<i + bytesPerPixel])
      pixels.append(pixel)
    }

    return Image(
      pixels: pixels, width: width, height: height, colors: bytesPerPixel)
  }
}
