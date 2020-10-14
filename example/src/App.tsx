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
        BackgroundLocation.startLocationUpdates(
          2000,
          2001,
          '水晶盾',
          '水晶盾',
          '定位服务运行中...'
        );
        console.log('started');
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
      BackgroundLocation.stopLocationUpdates();
      eventListener.remove();
    };
  }, []);

  const requestOnceLocation = () => {
    BackgroundLocation.requestOnceLocation()
      .then((res) => {
        const newRes = { ...res };
        console.log(JSON.stringify(newRes));
      })
      .catch((e) => {
        console.log(e.userInfo.error.errorCode);
      });
  };

  return (
    <View style={styles.container}>
      <Button
        title="停止连续定位"
        onPress={() => {
          BackgroundLocation.stopLocationUpdates();
        }}
      />
      <Button title="单次定位" onPress={requestOnceLocation} />
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
