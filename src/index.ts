import { requireNativeModule } from "expo";

export type PrinterModel = "58" | "80";

export type ReceiptConfig = {
  model: PrinterModel;
};

export type renderImages = (
  config: ReceiptConfig,
  imageBase64s: string[]
) => Uint8Array;

export type HtmlToImagesOptions = {
  model: PrinterModel;
  maxHeightToBreak?: number;
};

export type renderHtmlToImages = (
  html: string,
  config: HtmlToImagesOptions
) => Promise<Uint8Array>;

export interface ExpoEscposModule {
  renderImages: renderImages;
  renderHtmlToImages: renderHtmlToImages;
}

export default requireNativeModule<ExpoEscposModule>("ExpoEscpos");
