import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import {
  multiply,
  createProfile,
  useScanner,
} from 'react-native-zebra-scanner';

createProfile('Zebra Scanner', 'br.com.example.zebra.SCANNER');

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const [, setBarcode] = React.useState<string>();

  const scanner = useScanner({
    onCallbackScanner: setBarcode,
    canReset: false,
  });

  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);

  React.useEffect(() => {
    console.log({ scanner });
    // setTimeout(() => setBarcode(undefined), 500);
  }, [scanner]);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
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
