import { NativeModules, Platform } from 'react-native';
export { useScanner } from './useScanner';

const LINKING_ERROR =
  `The package 'react-native-zebra-scanner' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ZebraScanner = NativeModules.ZebraScanner
  ? NativeModules.ZebraScanner
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return ZebraScanner.multiply(a, b);
}

export function onInit(id: string) {
  return ZebraScanner.onInit(id);
}

export function createProfile(
  profileName: string,
  intentAction: string,
  keystrokeEnabled = false
) {
  return ZebraScanner.createProfile(
    profileName,
    intentAction,
    keystrokeEnabled
  );
}
