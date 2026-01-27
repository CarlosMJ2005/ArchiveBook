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
    async login() {
        const email =this.#loginView.getEmailLog()
        const password =this.#loginView.getPasswordLog()
        const state = this.#loginView.getStateCheckbox()
        
        app.saveUser(email, password, state)

        console.log(email, password)
        app.windowOpen()
    }
    /*async*/ signin() {
        try {
            const email =this.#loginView.getEmailSign()
            const password =this.#loginView.getPasswordSign()
            const confirmation =this.#loginView.getConfirmationSign()

            if (password != confirmation) {
                this.#loginView.showError()
            }
            else {
                console.log("obtenido correctamente: " + email)
                app.saveUser(email, password, false)
                app.windowOpen()
            }
        } catch (error) {
            console.log(error)
        }

    }
    change(bool = false) {
        this.#loginView.change(bool)
    }

    loadUser(){
        app.loadUser()
            .then((lista) => {
                try {
                    console.log(state)
                    if(lista.state == true){
                    this.#loginView.fulfill(lista.email, lista.password, lista.state)
                }
                } catch (error) {
                    console.log("la estructra del archivo no es correcta")
                }
                
            })
            .catch((err) => {
                console.log("no hay un usuario guardado")
            })
    }
}