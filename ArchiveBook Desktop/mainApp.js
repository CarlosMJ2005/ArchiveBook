import { Controller } from './src/controller/controller.js';

const controller = new Controller();

window.onload = () => {
         
  // addEventListeners de la aplicacion

  //document.getElementById("login").addEventListener('click', () => controller.login());
  
  controller.init();
}