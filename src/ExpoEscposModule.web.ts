import { registerWebModule, NativeModule } from 'expo';

import { ExpoEscposModuleEvents } from './ExpoEscpos.types';

class ExpoEscposModule extends NativeModule<ExpoEscposModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ExpoEscposModule, 'ExpoEscposModule');
