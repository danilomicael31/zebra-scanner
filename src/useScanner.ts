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
};

export const useScanner = () => {
  const [scanner, setScanner] = useState<string>();
  const [config, _setConfig] = useState<UseScannerConfig>(DEFAULT_CONFIG);

  const _id = useId();

  const onScanner = (data: string) => {
    setScanner(data);
  };

  const setConfig = (config: UseScannerConfig) => {
    _setConfig({ ...DEFAULT_CONFIG, ...config });
  };

  useEffect(() => {
    if (scanner && config.canScan && config.onCallbackScanner) {
      config.onCallbackScanner(scanner);
    }
    if (scanner && config.canScan && config.canReset)
      setTimeout(() => setScanner(undefined), config.timeOutToReset);
  }, [scanner, config]);

  useEffect(() => {
    const eventId = config.id ?? _id;
    if (!config.canScan) {
      DeviceEventEmitter.removeAllListeners(`onScanner-${eventId}`);
      return;
    }

    onInit(eventId);
    DeviceEventEmitter.addListener(`onScanner-${eventId}`, onScanner);
  }, [_id, config]);

  return { scanner, setConfig };
};
