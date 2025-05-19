import * as React from 'react';

import { ExpoEscposViewProps } from './ExpoEscpos.types';

export default function ExpoEscposView(props: ExpoEscposViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
