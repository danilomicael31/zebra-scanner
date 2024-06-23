import React from 'react';
import {
  Modal as NativeModal,
  type ModalProps as NativeModalProps,
  type StyleProp,
  StyleSheet,
  View,
  type ViewStyle,
} from 'react-native';

export interface BaseModalProps extends NativeModalProps {
  contentContainerStyle?: StyleProp<ViewStyle>;
  onRequestClose: () => void;
}

export const Modal: React.FC<BaseModalProps> = ({
  children,
  contentContainerStyle,
  style,
  ...rest
}) => {
  return (
    <NativeModal
      transparent
      statusBarTranslucent={false}
      animationType="fade"
      {...rest}
    >
      <View style={styles.container}>
        <View style={[styles.modal, style]}>
          <View style={[contentContainerStyle]}>{children}</View>
        </View>
      </View>
    </NativeModal>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#000000AA',
    justifyContent: 'center',
    alignItems: 'center',
    ...StyleSheet.absoluteFillObject,
  },
  modal: {
    backgroundColor: '#FFF',
    borderRadius: 30,
    width: 250,
    padding: 20,
  },
  headerModal: {
    flexDirection: 'row-reverse',
  },
});
