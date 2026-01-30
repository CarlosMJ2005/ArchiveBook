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
    #token 


    // Instantiating classes
    constructor() {
        this.#model = new Model();
        this.#loginView = new loginView();
        this.#appView = new appView();

        /*
        window.app.getToken((token) => {
            console.log("actualizo el tocken se me ha inviado un send")
            this.#token = token
            this.startLoad()
        })
        */
    }

    // Initializing classes
    init() {
    }

    // Controller methods...
    async login() {
        const email = this.#loginView.getEmailLog()
        const password = this.#loginView.getPasswordLog()
        const state = this.#loginView.getStateCheckbox()

        app.verify(email,password,state)
            .then((data) => {
                console.log(data)
                this.loadAll()
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
                this.#loginView.reset()
            })
    }
    /*async*/ signin() {
        try {
            const email = this.#loginView.getEmailSign()
            const password = this.#loginView.getPasswordSign()
            const confirmation = this.#loginView.getConfirmationSign()

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
    startLoad() {
        this.loadBest()
        this.loadFavourite()
        this.loadToRead()
        this.loadToReturn()
        this.loadAll()
    }
    loadBest() {

    }
    loadFavourite() {

    }
    loadToRead() {

    }
    loadToReturn() {

    }
    loadAll() {
        //let url = "http://192.168.207.76:8080/api/libros"; // Steven

        app.getAllBooks()
            .then((data) => {
                JSON.parse(JSON.stringify(data)).forEach(element => {
                    console.log(element.titulo)
                });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
                
            })
    }

    loadUser() {
        app.loadUser()
            .then((lista) => {
                try {
                    console.log(lista.state)
                    console.log(lista.email + " " + lista.password)
                    if (lista.state == true) {
                        this.#loginView.fulfill(lista.email, lista.password, lista.state)
                    }
                } catch (error) {
                    console.log(error)
                }
            })
            .catch((err) => {
                console.log("no hay un usuario guardado")
            })
    }
}