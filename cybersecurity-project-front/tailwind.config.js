/** @type {import('tailwindcss').Config} */
module.exports = {

   darkMode: 'class', // Necesario para el modo oscuro de Flowbite
  // Le decimos a Tailwind que busque clases en todos los archivos HTML y TS de tu proyecto.
  // También le decimos que mire dentro de la carpeta de Flowbite.
  content: [
    "./src/**/*.{html,ts}",
    "./node_modules/flowbite/**/*.js" 
  ],
  theme: {
    extend: {},
  },
  // Aquí activamos Flowbite como un plugin de Tailwind.
  plugins: [
    require('flowbite/plugin')
  ],
};
