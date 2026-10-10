/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        harvest: {
          50: '#f2fbf4',
          100: '#e1f6e6',
          200: '#c5eccf',
          300: '#98dcab',
          400: '#64c380',
          500: '#3ba75c',
          600: '#2c8747',
          700: '#246b3a',
          800: '#1e5530',
          900: '#1b472a',
        },
        sun: {
          50: '#fffbeb',
          100: '#fef3c7',
          200: '#fde68a',
          300: '#fcd34d',
          400: '#fbbf24',
          500: '#f59e0b',
          600: '#d97706',
        }
      }
    },
  },
  plugins: [],
}
