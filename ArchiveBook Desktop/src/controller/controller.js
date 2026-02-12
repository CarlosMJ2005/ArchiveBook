// Hacer los import de las clases del modelo
//
//
import { book } from '../model/book.js';

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
        //this.#model = new book();
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

    tapFavourite(button) { //añadir funcionalidad con api
        this.#appView.tapFavourite(button)
    }
    tapToReturn(button) { //añadir funcionalidad con api
        this.#appView.tapToReturn(button)
    }
    tapToRead(button) { //añadir funcionalidad con api
        this.#appView.tapToRead(button)
    }

    //log-sign switch

    change(bool = false) {
        this.#loginView.change(bool)
    }

    //pop ups

    closePopUp() {
        this.#appView.closePopUp()

    }
    openFilterPopUp() {
        this.#appView.openFilterPopUp()

    }
    openDescriptionPopUp(cover, title, author, synopsis, isbn, year) {
        console.log("pup")
        console.log(title)
        console.log(author)
        console.log(synopsis)
        console.log(isbn)
        console.log(year)
        console.log("pip")

        this.#appView.openDescriptionPopUp(cover, title, author, synopsis, isbn, year)

    }

    // book loading

    async startLoad() {
        
        const [favs/*, returns, reads*/] = await Promise.all([
        app.getFavourites(),
        //app.getToReturn(),
        //app.getToRead()
        ]);
        
        this.loadAll(favs/*, returns, reads*/)
    }
    
    loadAll(favs/*,returns,reads*/) {
        console.log(new Date())
        //let url = "http://192.168.207.76:8080/api/libros"; // Steven
        //let url = "http://192.168.207.83:8080/api/libros"; // Israel

        app.getAllBooks()
            .then((datos) => {
                console.log(new Date())
                this.#appView.eraseAllList()
                this.#appView.eraseBestList()
                JSON.parse(JSON.stringify(datos)).forEach(bookEntry => {
                    console.log(bookEntry)
                    
                    let favourite = false
                    /*
                    let toRead = false
                    let toReturn = false
                    */
                    favourite = favs.some(favEntry => {
                        if (bookEntry.idLibro === favEntry.libro.idLibro) {
                            console.log("Bua, israel, eres un fiera")
                            return true;
                        }
                    });
                    /*
                    toRead = reads.some(readEntry => {
                        if (bookEntry.idLibro === readEntry.libro.idLibro) {
                            console.log("Bua, israel, eres un fiera 2")
                            return true;
                        }
                    });
                    toReturn = returns.some(returnEntry => {
                        if (bookEntry.idLibro === returnEntry.libro.idLibro) {
                            console.log("Bua, israel, eres un fiera 3")
                            return true;
                        }
                    });
                    */

                    let actualBook = new book(bookEntry)
                    this.#appView.createBook(bookEntry, "all", this)
                });
            })
            .catch((error) => {
                console.log("Get-all-books " + error.message.substring(error.message.lastIndexOf("Error")))

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