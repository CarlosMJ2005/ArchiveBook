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
    #token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiZGFtIiwiZXhwIjoxNzY5NjYzNDUzLCJpYXQiOjE3Njk2Mjc0NTMsInNjb3BlIjoiUk9MRV9BRE1JTiJ9.ogy3EMEkhZzasHynD_sSY8QD7LRiYUmfO9WoMDeWLnY8pyzfqpi-YQSvcHf92reTar-TGduMj9ZKWgPjsE0xrdv7fxmZWFd6C9XcdXCyUDs3m_AmfsPHrw_9ACnKOLEshDOVj-Q5CzcqvZCmdtWQmteYLq4SW1uiQalYEkbgFFIbRP1K0PlC1cFqg_RV3GJqyvqKxwY7mV8ZcLVXiV0R-27klFIToGL_yx8CDU4062RideEsjb_9sAnVboSanaZOEFgW8Q1HA9wxETVPzZp03tHj8cCOpRqeNIshbwsEvB2xTiCwrEY__sXS7CqWB26EqgIRvPG7fBFC8ArO0iDcYw"


    // Instantiating classes
    constructor() {
        this.#model = new Model();
        this.#loginView = new loginView();
        this.#appView = new appView();
        this.loadUser()
        this.loadBest()
        this.loadFavourite()
        this.loadToRead()
        this.loadToReturn()
        this.loadAll()
    }


    // Initializing classes
    init() {
    }

    // Controller methods...
    async login() {
        const email = this.#loginView.getEmailLog()
        const password = this.#loginView.getPasswordLog()
        const state = this.#loginView.getStateCheckbox()

        app.saveUser(email, password, state)

        console.log(email, password)
        app.windowOpen()
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
    loadBest() {
        
    }
    loadFavourite() {

    }
    loadToRead() {

    }
    loadToReturn() {

    }
    loadAll() {
        let url = "http://192.168.207.38:8080/api/libros";

        fetch(url, {
            method: 'GET', // 'POST', 'PUT', 'DELETE', etc.
            headers: {
                'Content-Type': 'application/json', 
                'Authorization': `Bearer ${this.#token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                JSON.parse(JSON.stringify(data)).forEach(element => {
                    console.log(element.titulo)
                });
            })
            .catch(error => console.error(error));
    }

    loadUser() {
        app.loadUser()
            .then((lista) => {
                try {
                    console.log(state)
                    if (lista.state == true) {
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