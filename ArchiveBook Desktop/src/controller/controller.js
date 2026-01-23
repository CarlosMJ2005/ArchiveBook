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
    }


    // Initializing classes
    init() {
    }

    // Controller methods...
    login() {
        console.log("he hecho click a log")
        app.windowopen()
    }
    /*async*/ signin(email, password, confirmation) {
        try {
            if(password.value != confirmation.value){
                this.#loginView.showError()
            }
            else{
                console.log("obtenido correctamente: " + email.value)
                app.windowopen()
            }
        } catch (error) {
            console.log(error)
        }
        
    }
    change(bool = false, event) {
        console.log("cambio entre tipos")
        if (event){
            console.log("entro al event")
            event.preventDefault()
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
}