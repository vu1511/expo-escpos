// Reexport the native module. On web, it will be resolved to ExpoEscposModule.web.ts
// and on native platforms to ExpoEscposModule.ts
export { default } from './ExpoEscposModule';
export { default as ExpoEscposView } from './ExpoEscposView';
export * from  './ExpoEscpos.types';
