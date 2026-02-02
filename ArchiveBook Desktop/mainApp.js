import { Controller } from './src/controller/controller.js';

const controller = new Controller();

window.onload = () => {
         
  // addEventListeners de la aplicacion

  document.getElementById("favourite").addEventListener('click', () => controller.tapFavourite(document.getElementById("favourite")));
  document.getElementById("to-read").addEventListener('click', () => controller.tapToRead(document.getElementById("to-read")));
  document.getElementById("to-return").addEventListener('click', () => controller.tapToReturn(document.getElementById("to-return")));
  
  controller.init();
}