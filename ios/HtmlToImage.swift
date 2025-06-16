import CoreImage
import CoreImage.CIFilterBuiltins
import ExpoModulesCore
import UIKit
import WebKit

class HtmlToImageConverter: NSObject, WKNavigationDelegate {
  private var webView: WKWebView
  private let width: CGFloat
  private let maxHeight: CGFloat
  private let completion: (Result<[Data], Error>) -> Void

  private lazy var opaqueFormat: UIGraphicsImageRendererFormat = {
    let format = UIGraphicsImageRendererFormat()
    format.opaque = true
    format.scale = 1.0
    format.preferredRange = .standard
    return format
  }()

  init(
    webView: WKWebView, width: CGFloat, maxHeight: CGFloat,
    completion: @escaping (Result<[Data], Error>) -> Void
  ) {
    self.webView = webView
    self.width = width
    self.maxHeight = maxHeight
    self.completion = completion
    super.init()
  }

  private func generateQrCode(text: String, size: CGFloat = 150, errorCorrection: String = "M") -> Data? {
    let data = text.data(using: .utf8)

    let filter = CIFilter.qrCodeGenerator()
    filter.message = data ?? Data()

    switch errorCorrection.uppercased() {
    case "L":
      filter.correctionLevel = "L"
    case "M":
      filter.correctionLevel = "M"
    case "Q":
      filter.correctionLevel = "Q"
    case "H":
      filter.correctionLevel = "H"
    default:
      filter.correctionLevel = "M"
    }

    guard let outputImage = filter.outputImage else { return nil }

    let scaleX = size / outputImage.extent.size.width
    let scaleY = size / outputImage.extent.size.height
    let transformedImage = outputImage.transformed(by: CGAffineTransform(scaleX: scaleX, y: scaleY))

    let context = CIContext()
    guard let cgImage = context.createCGImage(transformedImage, from: transformedImage.extent) else { return nil }
    let uiImage = UIImage(cgImage: cgImage)
    guard let imageData = uiImage.pngData() else { return nil }

    return imageData
  }

  private func generateBarcode(text: String, width: CGFloat = 200, height: CGFloat = 100) -> Data? {
    let data = text.data(using: .utf8)

    let filter = CIFilter.code128BarcodeGenerator()
    filter.quietSpace = 0
    filter.message = data ?? Data()

    guard let outputImage = filter.outputImage else { return nil }

    let scaleX = width / outputImage.extent.size.width
    let scaleY = height / outputImage.extent.size.height
    let transformedImage = outputImage.transformed(by: CGAffineTransform(scaleX: scaleX, y: scaleY))

    let context = CIContext()
    guard let cgImage = context.createCGImage(transformedImage, from: transformedImage.extent) else { return nil }
    let uiImage = UIImage(cgImage: cgImage)
    guard let imageData = uiImage.pngData() else { return nil }

    return imageData
  }

  static func renderHtmlToImages(config: [String: Any], html: String)
    async throws -> [Data]
  {
    let options = HtmlToImagesOptions(dictionary: config)
    let printerWidth: CGFloat = options.model == .model58 ? 384 : 576
    let maxHeight: CGFloat = options.maxHeightToBreak ?? 1600

    return try await withCheckedThrowingContinuation { continuation in
      // Create WebView on main thread
      DispatchQueue.main.async {
        let webView = WKWebView(frame: CGRect(x: 0, y: 0, width: printerWidth, height: 1))
        webView.isOpaque = true
        webView.backgroundColor = UIColor.white

        let delegate = HtmlToImageConverter(
          webView: webView,
          width: printerWidth,
          maxHeight: maxHeight,
          completion: { result in
            switch result {
            case .success(let imageDataArray):
              continuation.resume(returning: imageDataArray)
            case .failure(let error):
              continuation.resume(throwing: error)
            }
          }
        )

        webView.navigationDelegate = delegate
        objc_setAssociatedObject(webView, "delegate", delegate, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        webView.loadHTMLString(html, baseURL: nil)
      }
    }
  }

  func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
    let convertScript = """
      function convertCodeElements() {
        const qrElements = document.querySelectorAll('[data-type="qrcode"]');
        const barcodeElements = document.querySelectorAll('[data-type="barcode"]');
        const allElements = [];
        
        qrElements.forEach((element, index) => {
          const text = element.getAttribute('data-text') || element.textContent || '';
          const size = element.getAttribute('data-size') || '150';
          const errorCorrection = element.getAttribute('data-error-correction') || 'M';
          
          allElements.push({
            type: 'qrcode',
            text: text,
            size: parseInt(size),
            errorCorrection: errorCorrection,
            elementId: 'qr-code-' + index
          });
          
          element.setAttribute('data-processing-id', 'qr-code-' + index);
        });
        
        barcodeElements.forEach((element, index) => {
          const text = element.getAttribute('data-text') || element.textContent || '';
          const width = element.getAttribute('data-width') || '200';
          const height = element.getAttribute('data-height') || `${width / 2}`;
          
          allElements.push({
            type: 'barcode',
            text: text,
            width: parseInt(width),
            height: parseInt(height),
            elementId: 'barcode-' + index
          });
          
          element.setAttribute('data-processing-id', 'barcode-' + index);
        });
        
        return allElements;
      }
      convertCodeElements();
      """

    webView.evaluateJavaScript(convertScript) { [weak self] (elementsData, error) in
      guard let self = self else { return }

      if let error = error {
        self.completion(.failure(error))
        return
      }

      guard let elements = elementsData as? [[String: Any]], !elements.isEmpty else {
        self.getContentHeightAndResize()
        return
      }

      self.processAllCodeElements(elements: elements)
    }
  }

  private func processAllCodeElements(elements: [[String: Any]]) {
    var processedElements: [String: String] = [:]

    for elementData in elements {
      guard let type = elementData["type"] as? String,
        let text = elementData["text"] as? String,
        let elementId = elementData["elementId"] as? String
      else {
        continue
      }

      var base64Image: String?
      if type == "qrcode" {
        guard let size = elementData["size"] as? Int else { continue }
        let errorCorrection = elementData["errorCorrection"] as? String ?? "M"
        let image = self.generateQrCode(text: text, size: CGFloat(size), errorCorrection: errorCorrection)
        base64Image = image?.base64EncodedString()
      } else if type == "barcode" {
        let width = elementData["width"] as? Int ?? 200
        let height = elementData["height"] as? Int ?? 100
        let image = self.generateBarcode(text: text, width: CGFloat(width), height: CGFloat(height))
        base64Image = image?.base64EncodedString()
      }

      if let imageBase64 = base64Image {
        processedElements[elementId] = imageBase64
      }
    }

    self.updateAllElements(processedElements: processedElements)
  }

  private func updateAllElements(processedElements: [String: String]) {
    let updateScript = """
      (function() {
        \(processedElements.map { (elementId, base64) in
        return """
        const element\(elementId.replacingOccurrences(of: "-", with: "_")) = document.querySelector('[data-processing-id="\(elementId)"]');
        if (element\(elementId.replacingOccurrences(of: "-", with: "_"))) {
          const img = document.createElement('img');
          img.src = 'data:image/png;base64,\(base64)';
          img.style.display = 'block';
          element\(elementId.replacingOccurrences(of: "-", with: "_")).innerHTML = '';
          element\(elementId.replacingOccurrences(of: "-", with: "_")).appendChild(img);
        }
        """
      }.joined(separator: "\n"))
      })();
      """

    webView.evaluateJavaScript(updateScript) { [weak self] (_, error) in
      guard let self = self else { return }

      if let error = error {
        self.completion(.failure(error))
        return
      }

      self.getContentHeightAndResize()
    }
  }

  private func getContentHeightAndResize() {
    webView.evaluateJavaScript("document.body.scrollHeight") { [weak self] (height, error) in
      guard let self = self, let contentHeight = height as? CGFloat else {
        self?.completion(
          .failure(
            NSError(
              domain: "WebViewCapture", code: 1,
              userInfo: [
                NSLocalizedDescriptionKey: "Failed to get content height"
              ])))
        return
      }

      // Resize WebView to exact content dimensions
      self.webView.frame = CGRect(x: 0, y: 0, width: self.width, height: contentHeight)

      // Take snapshot after resize
      self.takeSnapshot()
    }
  }

  private func sliceImage(_ image: UIImage, width: CGFloat, maxHeight: CGFloat) -> [Data] {
    var imageParts: [Data] = []
    let totalHeight = image.size.height
    var currentY: CGFloat = 0

    while currentY < totalHeight {
      let sliceHeight = min(maxHeight, totalHeight - currentY)

      let renderer = UIGraphicsImageRenderer(size: CGSize(width: width, height: sliceHeight), format: opaqueFormat)
      let sliceImage = renderer.image { context in
        UIColor.white.setFill()
        context.fill(CGRect(x: 0, y: 0, width: width, height: sliceHeight))
        image.draw(at: CGPoint(x: 0, y: -currentY))
      }

      if let pngData = sliceImage.pngData() {
        imageParts.append(pngData)
      }

      currentY += sliceHeight
    }

    return imageParts
  }

  private func takeSnapshot() {
    let config = WKSnapshotConfiguration()
    config.rect = webView.bounds

    webView.takeSnapshot(with: config) { [weak self] (image, error) in
      guard let self = self else { return }

      if let error = error {
        self.completion(.failure(error))
        return
      }

      guard let image = image else {
        self.completion(
          .failure(
            NSError(
              domain: "WebViewCapture", code: 2,
              userInfo: [NSLocalizedDescriptionKey: "Failed to create image"])
          ))
        return
      }

      // Process image in background
      DispatchQueue.global(qos: .userInitiated).async {
        let targetWidth: CGFloat = self.width
        let targetHeight = image.size.height * (targetWidth / image.size.width)

        let renderer = UIGraphicsImageRenderer(
          size: CGSize(width: targetWidth, height: targetHeight), format: self.opaqueFormat)
        let resizedImage = renderer.image { context in
          UIColor.white.setFill()
          context.fill(CGRect(x: 0, y: 0, width: targetWidth, height: targetHeight))
          image.draw(in: CGRect(x: 0, y: 0, width: targetWidth, height: targetHeight))
        }

        let imageParts = self.sliceImage(resizedImage, width: targetWidth, maxHeight: self.maxHeight)

        DispatchQueue.main.async {
          if imageParts.isEmpty {
            self.completion(
              .failure(
                NSError(
                  domain: "WebViewCapture", code: 4,
                  userInfo: [
                    NSLocalizedDescriptionKey: "Failed to create image slices"
                  ])))
            return
          }

          self.completion(.success(imageParts))
        }
      }
    }
  }
}
