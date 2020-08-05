import { NativeModules } from 'react-native';

type CrystalShieldModuleType = {
  multiply(a: number, b: number): Promise<number>;
};

const { CrystalShieldModule } = NativeModules;

export default CrystalShieldModule as CrystalShieldModuleType;
