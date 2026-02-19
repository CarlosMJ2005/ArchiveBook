import { Controller } from './src/controller/controller.js';

import { User } from './src/model/user.js';

const controller = new Controller();

window.onload = () => {
         
  // addEventListeners de la aplicacion
  if (document.getElementById("toggle-password-log") != null) document.getElementById("toggle-password-log").addEventListener('click', () => controller.showPassword(document.getElementById("toggle-password-log"),document.getElementById("password-log")))
  if (document.getElementById("toggle-password-sign") != null) document.getElementById("toggle-password-sign").addEventListener('click', () => controller.showPassword(document.getElementById("toggle-password-sign"),document.getElementById("password-sign")));
  if (document.getElementById("toggle-confirmation-sign") != null) document.getElementById("toggle-confirmation-sign").addEventListener('click', () => controller.showPassword(document.getElementById("toggle-confirmation-sign"),document.getElementById("confirmation-sign")));

  if (document.getElementById("login") != null) document.getElementById("login").addEventListener('click', () => controller.login());
  if (document.getElementById("signin") != null) document.getElementById("signin").addEventListener('click', () => controller.signin());
  if (document.getElementById("log-to-sign") != null) document.getElementById("log-to-sign").addEventListener('click', () => controller.change(true));
  if (document.getElementById("sign-to-log") != null) document.getElementById("sign-to-log").addEventListener('click', () => controller.change());

  if (document.getElementById("close-center-popup") != null) document.getElementById("close-center-popup").addEventListener('click', () => controller.closePopUp());
  if (document.getElementById("menuButon") != null) document.getElementById("menuButon").addEventListener('click', () => controller.openFilterPopUp());

  if (document.getElementById("filterTitle")) document.getElementById("filterTitle").addEventListener('click', () => controller.setSearchMode('title'));
  if (document.getElementById("filterAuthor")) document.getElementById("filterAuthor").addEventListener('click', () => controller.setSearchMode('author'));
  if (document.getElementById("filterPublisher")) document.getElementById("filterPublisher").addEventListener('click', () => controller.setSearchMode('publisher'));
  if (document.getElementById("filterCategories")) document.getElementById("filterCategories").addEventListener('click', () => controller.setSearchMode('category'));


  if (document.getElementById("search")) document.getElementById("search").addEventListener('click', () => controller.executeSearch());



  controller.loadUser(); 
  
  controller.init();
}

        window.app.load().then(({ email, password, state }) => {
            console.log('backflip');
            controller.setUsuarioActivo(new User(email, password, state))
            //console.log(this.#usuarioActivo)
            controller.startLoad();
        });
