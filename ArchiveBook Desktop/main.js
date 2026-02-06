import { Controller } from './src/controller/controller.js';

const controller = new Controller();

window.onload = () => {
         
  // addEventListeners de la aplicacion

  if (document.getElementById("favourite") != null) document.getElementById("favourite").addEventListener('click', () => controller.tapFavourite(document.getElementById("favourite")));
  if (document.getElementById("to-read") != null) document.getElementById("to-read").addEventListener('click', () => controller.tapToRead(document.getElementById("to-read")));
  if (document.getElementById("to-return") != null) document.getElementById("to-return").addEventListener('click', () => controller.tapToReturn(document.getElementById("to-return")));
  if (document.getElementById("cover") != null) document.getElementById("cover").addEventListener('click', () => controller.openDescriptionPopUp(
    document.getElementById("cover"),"titulo", "autor","sinopsis","isbn","year"
    /*document.getElementById("cover"),document.getElementById("tittle"),document.getElementById("author"),
    document.getElementById("synopsis"),document.getElementById("isbn"),document.getElementById("year")*/
  ));


  if (document.getElementById("login") != null) document.getElementById("login").addEventListener('click', () => controller.login());
  if (document.getElementById("signin") != null) document.getElementById("signin").addEventListener('click', () => controller.signin());
  if (document.getElementById("log-to-sign") != null) document.getElementById("log-to-sign").addEventListener('click', () => controller.change(true));
  if (document.getElementById("sign-to-log") != null) document.getElementById("sign-to-log").addEventListener('click', () => controller.change());

  if (document.getElementById("close-center-popup") != null) document.getElementById("close-center-popup").addEventListener('click', () => controller.closePopUp());
  if (document.getElementById("menuButon") != null) document.getElementById("menuButon").addEventListener('click', () => controller.openFilterPopUp());

  


  controller.loadUser(); 
  
  controller.init();
}