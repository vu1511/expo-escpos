import { Button, SafeAreaView } from "react-native";
import { handlePrint } from "./printers";

export default function App() {
  return (
    <SafeAreaView style={{ marginTop: 120 }}>
      <Button title="Print value" onPress={handlePrint} />
    </SafeAreaView>
  );
}
