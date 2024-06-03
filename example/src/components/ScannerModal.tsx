import React, { useEffect, useState } from 'react';
import { View, Text } from 'react-native';
import { useScanner } from '../../../src/useScanner';
import { Modal } from './Modal';
import { SimpleModal } from './SimpleModal';

interface Modal1Props {
  title: string;
  visible: boolean;
  onRequestClose: () => void;
}

export const ModalScanner: React.FC<Modal1Props> = (props) => {
  const { scanner, setConfig } = useScanner();
  const [text, setText] = useState<string>();

  useEffect(() => {
    setConfig({ canScan: props.visible });
  }, [props.visible]);

  useEffect(() => {
    if (scanner) setText(scanner);
  }, [scanner]);

  return <SimpleModal {...props} message={text}></SimpleModal>;
};
