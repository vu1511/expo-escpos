# expo-escpos

A comprehensive Expo module for ESC/POS thermal printer integration. Supports text formatting, image printing, and receipt generation for 58mm and 80mm thermal printers.

## Features

- 🖨️ **ESC/POS Protocol Support** - Full implementation of ESC/POS commands
- 📱 **Cross-Platform** - Works on both iOS and Android
- 🖼️ **Image Printing** - Print images from base64 strings
- 📝 **Text Formatting** - Bold, italic, underline, and size control
- 📐 **Text Alignment** - Left, center, and right alignment
- 📏 **Multiple Printer Sizes** - Support for 58mm and 80mm thermal printers
- 🏭 **Multi-Brand Support** - Compatible with Epson, Star, Citizen, Bixolon, and Zjiang printers
- ✂️ **Paper Cutting** - Full and partial paper cuts
- 💰 **Cash Drawer Control** - Pulse control for cash drawers
- 🌍 **Codepage Support** - Multiple character encoding support

## Installation

### For managed Expo projects

```bash
expo install expo-escpos
```

### For bare React Native projects

Ensure you have [installed and configured the `expo` package](https://docs.expo.dev/bare/installing-expo-modules/) before continuing.

```bash
npm install expo-escpos
```

#### Configure for Android

No additional configuration required for Android.

#### Configure for iOS

Run the following command after installing the npm package:

```bash
npx pod-install
```

## Usage

### Basic Setup

```typescript
import ExpoEscpos from 'expo-escpos';

// Render receipt data
const receiptData = await ExpoEscpos.renderImages(
  { model: "80" }, // "58" for 58mm, "80" for 80mm printers
  imageBase64Array
);
```

### Complete Example with TCP Printing

```typescript
import ExpoEscpos from 'expo-escpos';
import { Buffer } from 'buffer';
import TcpSocket from 'react-native-tcp-socket';

const printReceipt = async () => {
  try {
    // Convert your images to base64 strings
    const imageBase64s = [
      'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...',
      // Add more base64 images as needed
    ];

    // Generate ESC/POS commands
    const receiptData = await ExpoEscpos.renderImages(
      { model: "80" }, // Use "58" for 58mm printers
      imageBase64s
    );

    // Convert to buffer for sending
    const buffer = Buffer.from(receiptData);

    // Send to thermal printer via TCP
    await sendToThermalPrinter(buffer, {
      host: '192.168.1.100',
      port: 9100
    });

    console.log('Print successful!');
  } catch (error) {
    console.error('Print failed:', error);
  }
};

const sendToThermalPrinter = async (data: Buffer, options: { host: string, port: number }) => {
  return new Promise((resolve, reject) => {
    const client = TcpSocket.createConnection(options, () => {
      client.write(data, undefined, (error) => {
        client.destroy();
        if (error) reject(error);
        else resolve(true);
      });
    });

    client.on('error', reject);
    client.setTimeout(5000);
    client.on('timeout', () => reject(new Error('Connection timeout')));
  });
};
```

### Advanced Usage with Manual Commands

For more control over the printing process, you can also work directly with ESC/POS commands:

```typescript
// Example of generating custom receipt data
const generateCustomReceipt = () => {
  // This would require extending the module or using the raw command capabilities
  // The module primarily focuses on image rendering for receipts
};
```

## API Reference

### `renderImages(config, imageBase64s)`

Renders receipt images using ESC/POS commands.

#### Parameters

- **config** (`object`): Configuration object
  - **model** (`string`): Printer model - `"58"` for 58mm or `"80"` for 80mm printers
- **imageBase64s** (`string[]`): Array of base64-encoded image strings

#### Returns

- **Promise<Uint8Array>**: ESC/POS command data ready to send to printer

#### Example

```typescript
const config = { model: "80" };
const images = [
  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...',
  'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD...'
];

const printData = await ExpoEscpos.renderImages(config, images);
```

## Supported Printer Models

### 58mm Printers
- Width: 32 characters
- Resolution: 384 dots
- Supported models: Most 58mm thermal printers

### 80mm Printers
- Width: 46 characters  
- Resolution: 576 dots
- Supported models: Most 80mm thermal printers

## Supported Printer Brands

- **Epson** - Full ESC/POS command support
- **Star** - Compatible command set
- **Citizen** - Compatible command set  
- **Bixolon** - Compatible command set
- **Zjiang** - Compatible command set
- **Generic ESC/POS** - Most thermal printers following ESC/POS standard

## Connection Methods

While this module generates the ESC/POS commands, you'll need a connection method to send data to your printer:

### TCP/IP (Ethernet/WiFi)
```typescript
import TcpSocket from 'react-native-tcp-socket';
// See complete example above
```

### Bluetooth
```typescript
// Use a Bluetooth library like react-native-bluetooth-serial
// or react-native-bluetooth-classic
```

### USB
```typescript
// Use a USB library for USB-connected printers
// Platform-specific implementation required
```

## Image Requirements

- **Format**: PNG, JPEG, or other formats supported by the platform
- **Encoding**: Base64 string with data URI prefix
- **Recommendation**: Use black and white or high-contrast images for best results
- **Size**: Images will be automatically scaled to fit printer width

### Example Image Processing

```typescript
// Convert image to base64
const imageToBase64 = (imagePath: string): Promise<string> => {
  // Implementation depends on your image source
  // Could be from camera, gallery, or generated content
};
```

## Error Handling

```typescript
try {
  const printData = await ExpoEscpos.renderImages(config, images);
  // Handle successful generation
} catch (error) {
  if (error.message.includes('Array index')) {
    console.error('Image processing error:', error);
  } else if (error.message.includes('codepage')) {
    console.error('Character encoding error:', error);
  } else {
    console.error('Unknown error:', error);
  }
}
```

## Troubleshooting

### Common Issues

1. **"Array index out of bounds"**
   - Usually indicates an image processing issue
   - Verify your base64 images are valid
   - Try with smaller or simpler images

2. **Printer not responding**
   - Check network connectivity (for TCP printers)
   - Verify printer IP address and port
   - Ensure printer is powered on and ready

3. **Garbled output**
   - Check printer model configuration ("58" vs "80")
   - Verify codepage settings for text content

### Debug Mode

Enable detailed logging to troubleshoot issues:

```typescript
// Check the native logs for detailed error information
// Android: adb logcat | grep EscPosEncoder
// iOS: Console app or Xcode console
```

## Contributing

Contributions are very welcome! Please refer to guidelines described in the [contributing guide](https://github.com/expo/expo#contributing).

### Development Setup

1. Clone the repository
2. Install dependencies: `npm install`
3. Run the example: `cd example && npm install && npm start`

## License

MIT

## Support

- 📧 Email: vund.dev@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/vu1511/expo-escpos/issues)
- 📖 Documentation: [GitHub Repository](https://github.com/vu1511/expo-escpos)
