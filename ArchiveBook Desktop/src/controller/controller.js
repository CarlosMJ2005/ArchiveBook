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


        window.app.load().then(() => {
            console.log('backflip');
            this.startLoad();
        });

    }

    // Initializing classes
    init() {
    }

    // Controller methods...
    async login() {
        const email = this.#loginView.getEmailLog()
        const password = this.#loginView.getPasswordLog()
        const state = this.#loginView.getStateCheckbox()

        app.verify(email, password, state)
            .then((data) => {
                console.log(data)
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

    //book buttons

    tapFavourite(button) {
        this.#appView.tapFavourite(button)
    }
    tapToReturn(button) {
        this.#appView.tapToReturn(button)
    }
    tapToRead(button) {
        this.#appView.tapToRead(button)
    }

    //log-sign switch

    change(bool = false) {
        this.#loginView.change(bool)
    }

    //pop ups

    closePopUp(){
        this.#appView.closePopUp()
        
    }
    openFilterPopUp(){
        this.#appView.openFilterPopUp()
        
    }
    openDescriptionPopUp(cover,title,author,synopsis,isbn,year){
        console.log("pup")
        console.log(title.value)
        console.log(author.value)
        console.log(synopsis.value)
        console.log(isbn.value)
        console.log(year.value)
        console.log("pip")
        
        this.#appView.openDescriptionPopUp(cover,title,author,synopsis,isbn,year)
        
    }

    // book loading

    startLoad() {
        this.loadAll()

        this.loadBest()
        this.loadFavourite()
        this.loadToRead()
        this.loadToReturn()
    }
    loadBest(){

    }
    loadFavourite(){
        
    }
    loadToRead(){
        
    }
    loadToReturn(){

    }
    /*
    loadBest() {
        app.getBest()
            .then((datos) => {
                    JSON.parse(JSON.stringify(datos)).forEach(element => {
                        console.log(element.titulo)
                    });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))

            })
    }
    loadFavourite() {
        app.getFavourites()
            .then((datos) => {
                    JSON.parse(JSON.stringify(datos)).forEach(element => {
                        console.log(element.titulo)
                    });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))

            })
    }
    loadToRead() {
        app.getToRead()
            .then((datos) => {
                    JSON.parse(JSON.stringify(datos)).forEach(element => {
                        console.log(element.titulo)
                    });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))

            })
    }
    loadToReturn() {
        app.getToReturn()
            .then((datos) => {
                    JSON.parse(JSON.stringify(datos)).forEach(element => {
                        console.log(element.titulo)
                    });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))

            })
    }
    */
    loadAll() {
        //let url = "http://192.168.207.76:8080/api/libros"; // Steven
        //let url = "http://192.168.207.83:8080/api/libros"; // Israel

        app.getAllBooks()
            .then((datos) => {
                    JSON.parse(JSON.stringify(datos)).forEach(element => {
                        console.log(element.titulo)
                    });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))

            })
    }

    //apply user data

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