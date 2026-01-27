import { Controller } from './src/controller/controller.js';


const controller = new Controller();

window.onload = () => {

  document.getElementById("login").addEventListener('click', () => controller.login());
  document.getElementById("signin").addEventListener('click', () => controller.signin());
  document.getElementById("log-to-sign").addEventListener('click', () => controller.change(true));
  document.getElementById("sign-to-log").addEventListener('click', () => controller.change());
  
  controller.init();
}