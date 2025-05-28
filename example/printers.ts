import ExpoEscposModule from "expo-escpos";
import TcpSocket from "react-native-tcp-socket";
import Socket from "react-native-tcp-socket/lib/types/Socket";
import { commentReceiptHtml, generateShipmentHtml } from "./data";
import { Alert } from "react-native";

type TcpOption = {
  port: number;
  host: string;
};

export const handlePrint = async (html: string, host: string) => {
  const startTime = performance.now();
  const receiptData = await ExpoEscposModule.renderHtmlToImages(html, {
    model: "80",
  });
  const endTime = performance.now();
  sendTcpData(receiptData, {
    port: 9100,
    host: host,
  });
  Alert.alert(`Time taken: ${endTime - startTime}ms`);
};

export const printCommentReceipt = async (host: string) => {
  handlePrint(commentReceiptHtml, host);
};

export const printShipmentReceipt = async (host: string) => {
  handlePrint(generateShipmentHtml(4), host);
};

export const printlargeShipmentReceipt = async (host: string) => {
  handlePrint(generateShipmentHtml(20), host);
};

const createTcpClient = (options: TcpOption): Promise<TcpSocket.Socket> => {
  return new Promise((resolve, reject) => {
    let client = TcpSocket.createConnection(options, () => {
      resolve(client);
    });
    client.setTimeout(5000);
    client.on("timeout", () => {
      console.log("connect timeout");
      reject("Socket timed out");
    });
    client.on("error", function (error) {
      console.log("connect error");
      reject(error);
    });
  });
};

const writeAsync = (client: Socket, data: Uint8Array): Promise<void> => {
  return new Promise((resolve, reject) => {
    client.write(data, undefined, (error) => {
      if (error) reject(error);
      else resolve();
    });
  });
};

export const sendTcpData = async (
  data: Uint8Array,
  options: TcpOption,
  chunkSize: number = 8192
) => {
  let client: Socket | null = null;
  try {
    client = await createTcpClient(options);
    const totalChunks = Math.ceil(data.length / chunkSize);

    for (let i = 0; i < totalChunks; i++) {
      const start = i * chunkSize;
      const end = start + chunkSize;
      const chunk = data.slice(start, end);
      await writeAsync(client, chunk);
    }
  } finally {
    client?.destroy();
  }
};
