import { useEffect, useId, useState } from 'react';
import { DeviceEventEmitter } from 'react-native';
import { onInit } from 'react-native-zebra-scanner';

type OnCallBackScanner = (data: string) => void;

interface UseScannerConfig {
  id?: string;
  canScan?: boolean;
  canReset?: boolean;
  timeOutToReset?: number;
  onCallbackScanner?: OnCallBackScanner;
}

const DEFAULT_CONFIG: UseScannerConfig = {
  canReset: true,
  canScan: true,
  timeOutToReset: 500,
  onCallbackScanner: console.log,
};

export const useScanner = (config?: UseScannerConfig) => {
  const [scanner, setScanner] = useState<string>();

  const _id = useId();
  const _config = { ...DEFAULT_CONFIG, ...config };

  const onScanner = (data: string) => {
    setScanner(data);
  };

  useEffect(() => {
    if (scanner && _config.onCallbackScanner) {
      _config.onCallbackScanner(scanner);
    }
    if (scanner && _config.canReset)
      setTimeout(() => setScanner(undefined), _config.timeOutToReset);
  }, [scanner, _config]);

  useEffect(() => {
    if (!_config.canScan) return;

    const eventId = _config.id ?? _id;
    onInit(eventId);
    DeviceEventEmitter.addListener(`onScanner-${eventId}`, onScanner);
  }, [_id, _config]);

  return scanner;
};
