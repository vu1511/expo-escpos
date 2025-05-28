import React, { useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Button,
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import {
  printCommentReceipt,
  printlargeShipmentReceipt,
  printShipmentReceipt,
} from "./printers";

export default function App() {
  const [isPrinting, setIsPrinting] = useState(false);
  const [printerIP, setPrinterIP] = useState("192.168.16.254");

  const handlePrint = async (printFunction: () => Promise<void>) => {
    if (!printerIP) {
      Alert.alert("Error", "Please enter printer IP");
      return;
    }

    setIsPrinting(true);
    try {
      await printFunction();
    } catch (error) {
      Alert.alert("Error", "Failed to print receipt. Please try again.");
    } finally {
      setIsPrinting(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Expo ESC/POS Demo</Text>
        <Text style={styles.subtitle}>Select a type to print</Text>
      </View>

      <View style={styles.settingsContainer}>
        <View style={styles.inputContainer}>
          <Text style={styles.inputLabel}>Printer IP:</Text>
          <TextInput
            style={styles.input}
            value={printerIP}
            onChangeText={setPrinterIP}
            placeholder="Enter printer IP"
            keyboardType="numeric"
            editable={!isPrinting}
          />
        </View>
      </View>

      <View style={styles.buttonContainer}>
        <Button
          title="📝 Print Comment Receipt"
          onPress={() => handlePrint(() => printCommentReceipt(printerIP))}
          disabled={isPrinting}
        />

        <View style={styles.spacer} />

        <Button
          title="📦 Print Shipment Receipt"
          onPress={() => handlePrint(() => printShipmentReceipt(printerIP))}
          disabled={isPrinting}
        />

        <View style={styles.spacer} />

        <Button
          title="🚚 Print Large Shipment Receipt"
          onPress={() =>
            handlePrint(() => printlargeShipmentReceipt(printerIP))
          }
          disabled={isPrinting}
        />
      </View>

      {isPrinting && (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#0000ff" />
          <Text style={styles.loadingText}>Printing receipt...</Text>
        </View>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
  },
  header: {
    padding: 20,
    alignItems: "center",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: "#666",
  },
  settingsContainer: {
    paddingHorizontal: 16,
  },
  inputContainer: {
    marginBottom: 12,
  },
  inputLabel: {
    fontSize: 14,
    color: "#666",
    marginBottom: 4,
  },
  input: {
    height: 40,
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 6,
    paddingHorizontal: 12,
    fontSize: 16,
    backgroundColor: "#fff",
  },
  buttonContainer: {
    padding: 20,
  },
  spacer: {
    height: 32,
  },
  loadingContainer: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(255, 255, 255, 0.8)",
  },
  loadingText: {
    marginTop: 10,
    fontSize: 16,
    color: "#333",
  },
});
