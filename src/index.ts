/**
 * Expo ESC/POS - Convert HTML to ESC/POS commands for thermal printers.
 *
 * Main features:
 * - Convert HTML (with QR/barcode tags) to ESC/POS commands for image printing
 * - Supports Vietnamese and other complex characters by rendering as images
 * - Supports both 58mm and 80mm printer models
 *
 * @module expo-escpos
 *
 * @section Custom HTML Tag Attributes
 *
 * To print QR codes and barcodes, add special attributes to your HTML tags:
 *
 * **QR Code Tag Example:**
 *   <div
 *     data-type="qrcode"
 *     data-text="https://example.com"   // (required) The text or URL to encode
 *     data-size="150"                   // (optional) Size in pixels (default: 150)
 *     data-error-correction="M"         // (optional) Error correction: "L", "M", "Q", "H" (default: "M")
 *   ></div>
 *
 * **Barcode Tag Example:**
 *   <div
 *     data-type="barcode"
 *     data-text="123456789"             // (required) The text to encode
 *     data-width="200"                  // (optional) Width in pixels (default: 200)
 *     data-height="80"                  // (optional) Height in pixels (default: width/2)
 *   ></div>
 *
 * **Attribute Reference:**
 * - `data-type` (required): "qrcode" or "barcode". Tells the renderer what to generate.
 * - `data-text` (required): The content to encode in the QR code or barcode. If omitted, uses the element's text content.
 * - `data-size` (QR only, optional): Size of the QR code in pixels. Default: 150.
 * - `data-error-correction` (QR only, optional): Error correction level for QR code. "L", "M", "Q", or "H". Default: "M".
 * - `data-width` (Barcode only, optional): Width of the barcode in pixels. Default: 200.
 * - `data-height` (Barcode only, optional): Height of the barcode in pixels. Default: width/2.
 *
 * @example
 * // Print a QR code:
 * const html = `
 *   <div data-type="qrcode" data-text="https://example.com" data-size="150"></div>
 * `;
 * const data = await renderHtmlToImages(html, { model: "80" });
 * // Send 'data' to your printer
 *
 * // Print a barcode:
 * const html2 = `
 *   <div data-type="barcode" data-text="123456789" data-width="200" data-height="80"></div>
 * `;
 * const data2 = await renderHtmlToImages(html2, { model: "80" });
 * // Send 'data2' to your printer
 */
import { requireNativeModule } from "expo";

/**
 * Supported printer models.
 * - "58": 58mm (384px width)
 * - "80": 80mm (576px width)
 */
export type PrinterModel = "58" | "80";

/**
 * Configuration for rendering images to ESC/POS.
 * @property model - Printer model ("58" or "80")
 */
export type ReceiptConfig = {
  model: PrinterModel;
};

/**
 * Function signature for rendering images to ESC/POS commands.
 * @param config - Printer configuration
 * @param imageBase64s - Array of base64-encoded image strings
 * @returns ESC/POS command data as Uint8Array
 */
export type RenderImagesType = (
  config: ReceiptConfig,
  imageBase64s: string[]
) => Uint8Array;

/**
 * Options for converting HTML to images for ESC/POS.
 * @property model - Printer model ("58" or "80")
 * @property maxHeightToBreak - Optional: max height in pixels before breaking into multiple images (default: 1600)
 */
export type HtmlToImagesOptions = {
  model: PrinterModel;
  maxHeightToBreak?: number;
};

/**
 * Function signature for converting HTML to ESC/POS commands.
 *
 * @param html - HTML string to convert. To print a QR code or barcode, use:
 *   <div data-type="qrcode" data-text="https://example.com" data-size="150"></div>
 *   <div data-type="barcode" data-text="123456789" data-width="200" data-height="80"></div>
 *
 *   Custom attributes:
 *   - data-type: "qrcode" or "barcode" (required)
 *   - data-text: Content to encode (required)
 *   - data-size: QR code size in px (QR only, optional)
 *   - data-error-correction: QR error correction (QR only, optional)
 *   - data-width: Barcode width in px (Barcode only, optional)
 *   - data-height: Barcode height in px (Barcode only, optional)
 *
 * @param config - Options for rendering
 * @returns Promise resolving to ESC/POS command data as Uint8Array
 */
export type RenderHtmlToImagesType = (
  html: string,
  config: HtmlToImagesOptions
) => Promise<Uint8Array>;

/**
 * Native module interface for Expo ESC/POS.
 */
export interface ExpoEscposModule {
  PrinterModel: PrinterModel;
  HtmlToImagesOptions: HtmlToImagesOptions;
  renderImages: RenderImagesType;
  renderHtmlToImages: RenderHtmlToImagesType;
}

const ExpoEscpos = requireNativeModule<ExpoEscposModule>("ExpoEscpos");

export const { renderImages, renderHtmlToImages } = ExpoEscpos;

export default ExpoEscpos;
