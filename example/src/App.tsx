import * as React from 'react';
import {
  StyleSheet,
  View,
  NativeEventEmitter,
  NativeModules,
  Button,
} from 'react-native';
import BackgroundLocation from 'crystal-shield-module';

export default function App() {
  React.useEffect(() => {
    // TestLib.multiply(3, 7).then(setResult);
    // TestLib.plus(3, 7).then(setResult2);

    (async () => {
      const hasStartedLocationUpdates = await BackgroundLocation.hasStartedLocationUpdates();
      console.log('has: ' + hasStartedLocationUpdates);
      if (!hasStartedLocationUpdates) {
        BackgroundLocation.startLocationUpdates(2000, 2001, '水晶盾', '水晶盾', '定位服务运行中...');
        console.log('started')
      }
    })();

    const eventEmitter = new NativeEventEmitter(NativeModules.ToastLib);
    const eventListener = eventEmitter.addListener('EventLocation', (event) => {
      if (event) {
        if (event.error) {
          console.log(event.error);
          return;
        }
        if (event.location) {
          console.log(event.location.latitude);
        }
      }
    });

    return () => {
      eventListener.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Button
        title="stop"
        onPress={() => {
          BackgroundLocation.stopLocationUpdates();
        }}
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
});
