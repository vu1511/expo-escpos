import { NativeModule, requireNativeModule } from 'expo';

import { ExpoEscposModuleEvents } from './ExpoEscpos.types';

declare class ExpoEscposModule extends NativeModule<ExpoEscposModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoEscposModule>('ExpoEscpos');
