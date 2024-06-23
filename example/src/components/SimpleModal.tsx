import React from 'react';
import { View, StyleSheet, Text, Button } from 'react-native';
import { type BaseModalProps } from './Modal';
import { Modal } from './Modal';

interface SimpleModalProps extends BaseModalProps {
  title?: string;
  message?: string;
  label?: string;
  onPress?: () => void;
}

export const SimpleModal: React.FC<SimpleModalProps> = ({
  title = 'Ops',
  message,
  label = 'OK',
  onPress,
  children,
  ...rest
}) => {
  const { onRequestClose } = rest;
  const _onPress = () => {
    if (onPress) return onPress();
    onRequestClose();
  };

  return (
    <Modal contentContainerStyle={[styles.container]} {...rest}>
      <View style={styles.titleContainer}>
        <Text style={{ color: '#000' }}>{title}</Text>
      </View>

      <View style={styles.messageContainer}>
        <Text style={{ color: '#000' }}>{message}</Text>
      </View>

      <View>
        <Button title={label} onPress={_onPress} />
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'space-evenly',
  },
  titleContainer: {
    alignItems: 'center',
  },
  messageContainer: {
    alignItems: 'center',
    paddingVertical: 16,
  },
});
