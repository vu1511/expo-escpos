# Expo ESC/POS

A React Native library for converting HTML to ESC/POS commands that support thermal printer image printing. This is especially useful when your thermal printer doesn't support Vietnamese text or other complex character sets.

## Main Features

The primary feature of this library is to convert HTML content to ESC/POS commands that can be sent to thermal printers. This approach allows you to:

1. **Print Vietnamese text** (and other complex characters) on printers that don't natively support them
2. **Generate QR codes and barcodes** directly from HTML attributes
3. **Create complex layouts** using HTML/CSS and convert them to printer-compatible images
4. **Support both 58mm and 80mm thermal printers**

## Installation

```bash
npm install expo-escpos
# or
yarn add expo-escpos
```

## Usage

### Basic HTML to ESC/POS Conversion

```typescript
import { renderHtmlToImages } from 'expo-escpos';

const printReceipt = async () => {
  const html = `
    <html>
      <body>
        <h1>Receipt</h1>
        <p>Customer: John Doe</p>
        <p>Total: $25.50</p>
      </body>
    </html>
  `;

  try {
    const escPosData = await renderHtmlToImages(html, {
      model: "80", // "58" for 58mm or "80" for 80mm printers
      maxHeightToBreak: 1600 // Optional: max height before breaking into multiple images
    });

    // Send escPosData to your thermal printer
    await sendToPrinter(escPosData);
  } catch (error) {
    console.error('Failed to generate receipt:', error);
  }
};
```

### QR Code Generation

To generate QR codes in your HTML, use the `data-type="qrcode"` attribute:

```html
<!-- Basic QR Code -->
<div data-type="qrcode" data-text="https://example.com">QR Code</div>

<!-- QR Code with custom size -->
<div data-type="qrcode" data-text="Order #12345" data-size="200">Order QR</div>

<!-- QR Code with error correction level -->
<div 
  data-type="qrcode" 
  data-text="Important Data" 
  data-size="150" 
  data-error-correction="H">
  High Quality QR
</div>
```

**QR Code Attributes:**
- `data-type="qrcode"` - Required to identify this as a QR code element
- `data-text` - The text/URL to encode (uses element text content if not provided)
- `data-size` - Size in pixels (default: 150)
- `data-error-correction` - Error correction level: "L", "M", "Q", "H" (default: "M")

### Barcode Generation

To generate barcodes (Code 128), use the `data-type="barcode"` attribute:

```html
<!-- Basic Barcode -->
<div data-type="barcode" data-text="123456789">Barcode</div>

<!-- Barcode with custom dimensions -->
<div 
  data-type="barcode" 
  data-text="PRODUCT001" 
  data-width="300" 
  data-height="100">
  Product Code
</div>
```

**Barcode Attributes:**
- `data-type="barcode"` - Required to identify this as a barcode element
- `data-text` - The text to encode (uses element text content if not provided)
- `data-width` - Width in pixels (default: 200)
- `data-height` - Height in pixels (default: width/2)

### Complete Example with QR Codes and Barcodes

```typescript
const generateReceiptWithCodes = async () => {
  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <style>
        body { font-family: Arial, sans-serif; padding: 20px; text-align: center; }
        .header { margin-bottom: 20px; }
        .item { margin: 10px 0; padding: 10px; border-bottom: 1px dashed #000; }
        .codes { margin-top: 20px; }
      </style>
    </head>
    <body>
      <div class="header">
        <h1>STORE NAME</h1>
        <p>Receipt #12345</p>
        <p>Date: ${new Date().toLocaleDateString()}</p>
      </div>

      <div class="item">
        <p><strong>Product A</strong></p>
        <p>Price: $10.00</p>
        <!-- Product barcode -->
        <div data-type="barcode" data-text="PROD001" data-width="200" data-height="60"></div>
      </div>

      <div class="item">
        <p><strong>Product B</strong></p>
        <p>Price: $15.50</p>
        <!-- Another product barcode -->
        <div data-type="barcode" data-text="PROD002" data-width="200" data-height="60"></div>
      </div>

      <div class="codes">
        <p><strong>Scan for receipt details:</strong></p>
        <!-- QR code with receipt information -->
        <div 
          data-type="qrcode" 
          data-text="https://store.com/receipt/12345" 
          data-size="150"
          data-error-correction="M">
        </div>
        
        <p style="margin-top: 20px;"><strong>Order ID:</strong></p>
        <!-- Order barcode -->
        <div data-type="barcode" data-text="ORD12345" data-width="250" data-height="80"></div>
      </div>

      <div style="margin-top: 30px;">
        <p>Thank you for your purchase!</p>
      </div>
    </body>
    </html>
  `;

  try {
    const escPosData = await renderHtmlToImages(html, {
      model: "80",
      maxHeightToBreak: 1600
    });

    // Send to printer via TCP, Bluetooth, or USB
    await sendToPrinter(escPosData);
  } catch (error) {
    console.error('Print failed:', error);
  }
};
```

## API Reference

### `renderHtmlToImages(html, options)`

Converts HTML content to ESC/POS commands for thermal printing.

**Parameters:**
- `html` (string): The HTML content to convert
- `options` (object):
  - `model` (string): Printer model - `"58"` for 58mm or `"80"` for 80mm printers
  - `maxHeightToBreak` (number, optional): Maximum height in pixels before breaking into multiple images (default: 1600)

**Returns:** Promise<Uint8Array> - ESC/POS command data ready to send to printer

### `renderImages(config, imageBase64s)`

Converts base64 image strings directly to ESC/POS commands.

**Parameters:**
- `config` (object):
  - `model` (string): Printer model - `"58" | "80"`
- `imageBase64s` (string[]): Array of base64-encoded image strings

**Returns:** Uint8Array - ESC/POS command data

## Printer Communication

The library generates ESC/POS commands but doesn't handle printer communication. You'll need to send the data using:

- **TCP/IP**: For network printers
- **Bluetooth**: For Bluetooth printers  
- **USB**: For USB printers

Example TCP printing:

```typescript
import TcpSocket from 'react-native-tcp-socket';

const sendToPrinter = async (data: Uint8Array, printerIP: string) => {
  return new Promise((resolve, reject) => {
    const client = TcpSocket.createConnection({
      port: 9100,
      host: printerIP
    }, () => {
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

## Supported Printer Models

- **58mm thermal printers** (384px width)
- **80mm thermal printers** (576px width)

## Error Handling

The library includes comprehensive error handling for:
- Invalid HTML content
- QR code/barcode generation failures
- Image processing errors
- Memory management

## Performance Tips

1. **Optimize HTML**: Keep HTML simple and avoid complex CSS
2. **Image sizes**: Use appropriate image sizes for your printer model
3. **Break long receipts**: Use `maxHeightToBreak` to split very long receipts
4. **Test thoroughly**: Always test with your specific printer model

## License

MIT

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
