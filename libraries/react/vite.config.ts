import { defineConfig } from 'vite'

import { resolve } from 'path'

import react from '@vitejs/plugin-react'

import dts from 'vite-plugin-dts'

export default defineConfig({
  plugins: [react(), dts({ tsconfigPath: './tsconfig.app.json', rollupTypes: true })],
  build: {
    lib: {
        entry: resolve(__dirname, 'src/index.ts'),
        fileName: 'chatkitty-react',
        formats: ['es'],
    }
  }
})
