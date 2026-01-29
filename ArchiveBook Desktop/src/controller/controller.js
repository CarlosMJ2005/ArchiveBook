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
    #token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiZGFtIiwiZXhwIjoxNzY5NzM1Njk1LCJpYXQiOjE3Njk2OTk2OTUsInNjb3BlIjoiUk9MRV9BRE1JTiJ9.J23LX3uGXHR_dzcc9qE8gCQ4KxkEv4ETu6EU8m4EC9FYTPDKobU7aJPSa4cbR86lLQo88xtRg5RwvtlpUFmkw-UIZS-3HV8Q8v1Vj4iruH3HakQ-D3aG10kSWXibaVLUsu98dsXEb-PigCv-4IHV2nuzHAdrt5izUMKMxEUIHR6GJXiv2UMFWRKpt8BG3ak5h1Eg6Xeci80ti0GsycWUmDyLrAy48FQueMlcSWVEbwfOC7A4H1OSbukNrwq4sCDwSjX32uA5I1S097uRjiCmgrXgAwUBjkzg3LUTA_hYXG9Fox96_gch0Cxk5JJOrymkAHzOtXyuEYe_azUVB7kwig"


    // Instantiating classes
    constructor() {
        this.#model = new Model();
        this.#loginView = new loginView();
        this.#appView = new appView();
        
        window.app.getToken((token) => { 
            console.log("entro a recivir producto porque se me ha inviado un send")
            this.#token = token
            })
    }

    // Initializing classes
    init() {
    }

    // Controller methods...
    async login() {
        const email = this.#loginView.getEmailLog()
        const password = this.#loginView.getPasswordLog()
        const state = this.#loginView.getStateCheckbox()

        let url = "http://192.168.207.38:8080/token"; // Carlos

        fetch(url, {
            method: 'POST', // 'GET', 'PUT', 'DELETE', etc.
            headers: {
                'Content-Type': 'application/json', 
                'Authorization': 'Basic ' + btoa("dam:1234") //cambiar cuando podamos crear usuarios
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                this.#token = response
                console.log(this.#token)
                app.saveUser(email, password, state)
                app.windowOpen()
            })
            .catch(error => {
                console.log(error)
                this.#loginView.showError()
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
    startLoad(){
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
        let url = "http://192.168.207.38:8080/api/libro"; // Carlos

        fetch(url, {
            method: 'GET', // 'POST', 'PUT', 'DELETE', etc.
            headers: {
                'Content-Type': 'application/json', 
                'Authorization': `Bearer ${this.#token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                return response.json()
            })
            .then(data => {
                JSON.parse(JSON.stringify(data)).forEach(element => {
                    console.log(element.titulo)
                });
            })
            .catch(error => console.log(error))
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