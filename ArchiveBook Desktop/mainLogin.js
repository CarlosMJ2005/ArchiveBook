import { Controller } from './src/controller/controller.js';

const controller = new Controller();

window.onload = () => {
         
  // addEventListeners de la aplicacion

  const emailLog = document.getElementById("email-log")
  const passwordlog = document.getElementById("password-log")

  const emailSign = document.getElementById("email-sign")
  const passwordSign = document.getElementById("password-sign")
  const confirmationSign = document.getElementById("confirmation-sign")

  document.getElementById("login").addEventListener('click', () => controller.login(emailLog,passwordlog));
  document.getElementById("signin").addEventListener('click', () => controller.signin(emailSign,passwordSign,confirmationSign));
  document.getElementById("log-to-sign").addEventListener('click', () => controller.change(true));
  document.getElementById("sign-to-log").addEventListener('click', () => controller.change(false));
  
  controller.init();
}