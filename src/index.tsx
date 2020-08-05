import { NativeModules } from 'react-native';

type BackgroundLocationType = {
  startLocationUpdates(channelId: number, channelName: string, firstLineText: string, secondLineText: string): void;
  stopLocationUpdates(): void;
  hasStartedLocationUpdates(): Promise<boolean>;
  test(): void;
};

const { BackgroundLocation } = NativeModules;

export default BackgroundLocation as BackgroundLocationType;
