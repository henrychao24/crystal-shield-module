import { NativeModules } from 'react-native';

type BackgroundLocationType = {
  setApiKey(apiKey: string): void;
  startLocationUpdates(interval: number, channelId: number, channelName: string, firstLineText: string, secondLineText: string): void;
  stopLocationUpdates(): void;
  hasStartedLocationUpdates(): Promise<boolean>;
  performUpgrade(fileAbsolutePath: string): void;
  test(): void;
};

const { BackgroundLocation } = NativeModules;

export default BackgroundLocation as BackgroundLocationType;
