import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  multiply,
  createProfile,
  useScanner,
} from 'react-native-zebra-scanner';
import { ModalScanner } from './components';

createProfile('Zebra Scanner', 'br.com.example.zebra.SCANNER');

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const [showModal1, setShowModal1] = React.useState(false);
  const [showModal2, setShowModal2] = React.useState(false);
  const [showModal3, setShowModal3] = React.useState(false);

  const [_scanner, setBarcode] = React.useState<string>();

  const { scanner, setConfig } = useScanner();

  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);

  const onRequestClose = () => {
    setShowModal1(false);
    setShowModal2(false);
    setShowModal3(false);
  };

  React.useEffect(() => {
    const canScan = !(showModal1 || showModal1 || showModal1);
    console.log({ canScan });
    setConfig({ canScan, canReset: false });
  }, [showModal1, showModal1, showModal1]);

  return (
    <View style={styles.container}>
      <Text>Result: {scanner}</Text>

      <View
        style={{
          paddingTop: 20,
          gap: 20,
        }}
      >
        <Button title="Modal 1" onPress={() => setShowModal1(true)} />

        <Button title="Modal 2" onPress={() => setShowModal2(true)} />

        <Button title="Modal 3" onPress={() => setShowModal3(true)} />
      </View>

      <ModalScanner
        visible={showModal1}
        onRequestClose={onRequestClose}
        title="Modal 1"
      />

      <ModalScanner
        visible={showModal2}
        onRequestClose={onRequestClose}
        title="Modal 2"
      />

      <ModalScanner
        visible={showModal3}
        onRequestClose={onRequestClose}
        title="Modal 3"
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
