import { requireNativeView } from 'expo';
import * as React from 'react';

import { ExpoEscposViewProps } from './ExpoEscpos.types';

const NativeView: React.ComponentType<ExpoEscposViewProps> =
  requireNativeView('ExpoEscpos');

export default function ExpoEscposView(props: ExpoEscposViewProps) {
  return <NativeView {...props} />;
}
