import ExpoModulesCore
import UIKit
import WebKit

class StylePreservingWebViewDelegate: NSObject, WKNavigationDelegate {
  private var webView: WKWebView
  private let width: CGFloat
  private let maxHeight: CGFloat
  private let completion: (Result<[Data], Error>) -> Void

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

  static func renderHtmlToImages(config: [String: Any], html: String)
    async throws -> [Data]
  {
    let options = HtmlToImagesOptions(dictionary: config)
    let printerWidth: CGFloat = options.model == .model58 ? 384 : 576
    let maxHeight: CGFloat = options.maxHeightToBreak ?? 1600
    print(maxHeight)

    return try await withCheckedThrowingContinuation { continuation in
      // Create WebView on main thread
      DispatchQueue.main.async {
        // Create a dedicated WebView for this render operation
        let webView = WKWebView(
          frame: CGRect(x: 0, y: 0, width: printerWidth, height: 1))
        webView.isOpaque = false
        webView.backgroundColor = UIColor.white

        // Create a delegate to handle rendering
        let delegate = StylePreservingWebViewDelegate(
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
        objc_setAssociatedObject(
          webView, "delegate", delegate, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)

        // Load the HTML
        webView.loadHTMLString(html, baseURL: nil)
      }
    }
  }

  func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
    // Get content height and resize WebView
    webView.evaluateJavaScript("document.body.scrollHeight") {
      [weak self] (height, error) in
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
      self.webView.frame = CGRect(
        x: 0, y: 0, width: self.width, height: contentHeight)

      // Take snapshot after resize
      self.takeSnapshot()
    }
  }

  private func takeSnapshot() {
    if #available(iOS 11.0, *) {
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

        // Resize to exact intrinsic width
        let targetWidth: CGFloat = self.width
        let targetHeight = image.size.height * (targetWidth / image.size.width)

        UIGraphicsBeginImageContextWithOptions(
          CGSize(width: targetWidth, height: targetHeight), false, 1.0)
        image.draw(
          in: CGRect(x: 0, y: 0, width: targetWidth, height: targetHeight))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        guard let finalImage = resizedImage else {
          self.completion(
            .failure(
              NSError(
                domain: "WebViewCapture", code: 3,
                userInfo: [
                  NSLocalizedDescriptionKey: "Failed to create final image"
                ])))
          return
        }

        // Slice the image into parts
        var imageParts: [Data] = []
        let totalHeight = finalImage.size.height
        var currentY: CGFloat = 0

        while currentY < totalHeight {
          let sliceHeight = min(self.maxHeight, totalHeight - currentY)

          UIGraphicsBeginImageContextWithOptions(
            CGSize(width: targetWidth, height: sliceHeight), false, 1.0)
          finalImage.draw(at: CGPoint(x: 0, y: -currentY))
          if let sliceImage = UIGraphicsGetImageFromCurrentImageContext(),
            let pngData = sliceImage.pngData()
          {
            imageParts.append(pngData)
          }
          UIGraphicsEndImageContext()

          currentY += sliceHeight
        }

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
    } else {
      // Fallback for older iOS versions
      UIGraphicsBeginImageContextWithOptions(webView.bounds.size, false, 1.0)
      webView.drawHierarchy(in: webView.bounds, afterScreenUpdates: true)
      let image = UIGraphicsGetImageFromCurrentImageContext()
      UIGraphicsEndImageContext()

      guard let capturedImage = image else {
        self.completion(
          .failure(
            NSError(
              domain: "WebViewCapture", code: 5,
              userInfo: [NSLocalizedDescriptionKey: "Failed to capture image"]))
        )
        return
      }

      // Slice the image into parts for older iOS versions
      var imageParts: [Data] = []
      let totalHeight = capturedImage.size.height
      var currentY: CGFloat = 0

      while currentY < totalHeight {
        let sliceHeight = min(self.maxHeight, totalHeight - currentY)
        let sliceRect = CGRect(
          x: 0, y: currentY, width: self.width, height: sliceHeight)

        UIGraphicsBeginImageContextWithOptions(
          CGSize(width: self.width, height: sliceHeight), false, 1.0)
        capturedImage.draw(at: CGPoint(x: 0, y: -currentY))
        if let sliceImage = UIGraphicsGetImageFromCurrentImageContext(),
          let pngData = sliceImage.pngData()
        {
          imageParts.append(pngData)
        }
        UIGraphicsEndImageContext()

        currentY += sliceHeight
      }

      if imageParts.isEmpty {
        self.completion(
          .failure(
            NSError(
              domain: "WebViewCapture", code: 6,
              userInfo: [
                NSLocalizedDescriptionKey: "Failed to create image slices"
              ])))
        return
      }

      self.completion(.success(imageParts))
    }
  }
}
