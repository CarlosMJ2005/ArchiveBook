// Hacer los import de las clases del modelo
//
//
import { Model } from './src/controller/model.js';

// Hacer los imports de las clases de la vista
//
//
import { View } from './src/controller/view.js';

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

}