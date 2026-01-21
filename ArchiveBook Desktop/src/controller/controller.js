// Hacer los import de las clases del modelo
//
//
import { Model } from '../model/model.js';

// Hacer los imports de las clases de la vista
//
//
import { View } from '../view/view.js';

export class Controller {

    // Access to view and model classes as private fields
    #model
    #view


    // Instantiating classes
    constructor() {
        this.#model = new Model();
        this.#view = new View();
    }


    // Initializing classes
    init() {
    }

    // Controller methods...
    login() {
        console.log("he hecho click a log")
        app.windowopen()
    }
    signin() {
        console.log("he hecho click a sign")
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