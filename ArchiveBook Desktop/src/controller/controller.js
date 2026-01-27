// Hacer los import de las clases del modelo
//
//
import { Model } from '../model/model.js';

// Hacer los imports de las clases de la vista
//
//
import { loginView } from '../view/loginView.js';

import { appView } from '../view/appView.js';

export class Controller {

    // Access to view and model classes as private fields
    #model
    #loginView
    #appView


    // Instantiating classes
    constructor() {
        this.#model = new Model();
        this.#loginView = new loginView();
        this.#appView = new appView();
        this.loadUser()
    }


    // Initializing classes
    init() {
    }

    // Controller methods...
    async login(email, password) {
        console.log(email.value, password.value)
        app.saveUser(email.value, password.value)
        //app.windowOpen()
    }
    /*async*/ signin(email, password, confirmation) {
        try {
            if (password.value != confirmation.value) {
                this.#loginView.showError()
            }
            else {
                console.log("obtenido correctamente: " + email.value)
                app.windowOpen()
            }
        } catch (error) {
            console.log(error)
        }

    }
    change(bool = false, event) {
        console.log("cambio entre tipos")
        if (event) {
            console.log("entro al event")
        }
        if (bool) {
            console.log("muestro sign")
            document.getElementById('sign').classList.remove("d-none")
            document.getElementById('log').classList.add("d-none")
        }
        else {
            console.log("muestro log")
            document.getElementById('log').classList.remove("d-none")
            document.getElementById('sign').classList.add("d-none")
        }
    }

    loadUser(){
        
    }
}