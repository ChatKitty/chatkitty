import {defineConfig} from 'vite'

import {resolve} from 'path'

import react from '@vitejs/plugin-react'

import dts from 'vite-plugin-dts'

export default defineConfig({
    plugins: [react(), dts({tsconfigPath: './tsconfig.app.json', rollupTypes: true})],
    build: {
        minify: false,
        sourcemap: true,
        lib: {
            entry: resolve(__dirname, 'src/index.ts'),
            name: 'chatkitty-react',
            fileName: (format) => `chatkitty-react.${format}.js`,
        },
        rollupOptions: {
            external: ["react"],
            output: {
                globals: {
                    react: "react"
                },
            },
        },
    }
})
