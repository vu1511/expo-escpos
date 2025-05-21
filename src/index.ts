import { requireNativeModule } from "expo";

export type renderImages = (
  config: ReceiptConfig,
  imageBase64s: string[]
) => Uint8Array;

export type ReceiptConfig = {
  model: "58" | "80";
};

declare class ExpoEscposModule {
  renderImages: renderImages;
}

export default requireNativeModule<ExpoEscposModule>("ExpoEscpos");
