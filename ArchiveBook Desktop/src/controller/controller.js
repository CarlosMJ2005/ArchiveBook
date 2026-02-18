// Hacer los import de las clases del modelo
//
//
import { Book } from '../model/book.js';

import { User } from '../model/user.js';

import { Library } from '../model/library.js';

// Hacer los imports de las clases de la vista
//
//
import { loginView } from '../view/loginView.js';

import { appView } from '../view/appView.js';

export class Controller {

    // Access to view and model classes as private fields
    #loginView
    #appView
    #library
    #usuarioActivo


    // Instantiating classes
    constructor() {
        //this.#model = new book();
        this.#loginView = new loginView();
        this.#appView = new appView();
        this.#library = new Library()


        window.app.load().then(({ email, password, state }) => {
            console.log('backflip');
            this.#usuarioActivo = new User(email, password, state)
            //console.log(this.#usuarioActivo)
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
                //console.log(data)
                this.#usuarioActivo = new User(email,password,state)
                this.#loginView.reset()
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
                this.#loginView.showError("Fallo en el usuario o contraseña")
            })
    }
    async signin() {

        const email = this.#loginView.getEmailSign()
        const password = this.#loginView.getPasswordSign()
        const confirmation = this.#loginView.getConfirmationSign()

        if (password != confirmation) {
            this.#loginView.showError()
            this.#loginView.showError()
        }
        else {
            app.addUser(email, password)
                .then((data) => {
                    //console.log(data)
                    this.#loginView.setEmailLog(email)
                    this.#loginView.setPasswordLog(password)
                    this.#loginView.setStateCheckbox(false)
                    this.login()
                })
                .catch((error) => {
                    console.log(error.message.substring(error.message.lastIndexOf("Error")))
                })
        }


    }

    //book buttons

    async tapFavorite(button, idLibro) { //añadir funcionalidad con api
        console.log(idLibro)
        if (button.querySelector('img').src.endsWith('heart.png')) { // cuando se marca como fav
            console.log(await app.addFavorite(idLibro))
            this.startLoad()
            //this.#appView.tapFavorite(button, false)
        }
        else {// cuando se desmarca como fav
            console.log(await app.removeFavorite(idLibro))
            this.startLoad()
        }
    }
    tapToReturn(button, idLibro) { //añadir funcionalidad con api
        console.log(idLibro)
        if (button.querySelector('img').src.endsWith('notification.png')) {
            this.#appView.tapToReturn(button, false)
        }
        else {
            this.#appView.tapToReturn(button, true)
        }
    }
    async tapToRead(button, idLibro) { //añadir funcionalidad con api
        console.log(idLibro)
        if (button.querySelector('img').src.endsWith('bookmark.png')) {
            console.log(await app.addToRead(idLibro))
            this.startLoad()
        }
        else {
            this.#appView.tapToRead(button, true)
        }
    }

    //log-sign switch

    change(bool = false) {
        this.#loginView.change(bool)
    }

    //toggle password
    showPassword(icon, input){
        //console.log(icon)
        //console.log(input)
        this.#loginView.showPassword(icon,input)
    }

    //pop ups

    closePopUp() {
        this.#appView.closePopUp()
    }
    openFilterPopUp() {
        this.#appView.openFilterPopUp()
    }

    // book loading

    async startLoad() {
        
        const [favs, reads, returns] = await Promise.all([
        app.getFavorites(),
        app.getToReturn(),
        app.getToRead()
        ]);

        
        //console.log(returns)
        
        this.loadAll(favs, returns, reads)
    }
    
    loadAll(favs,returns,reads) {
        //console.log(new Date())

        app.getAllBooks()
            .then((datos) => {
                //console.log(new Date())
                this.#appView.eraseAllList()
                this.#appView.eraseBestList()
                this.#appView.eraseFavList()
                this.#appView.eraseReadList()
                this.#appView.eraseReturnList()

                this.#library.eraseAll()

                JSON.parse(JSON.stringify(datos)).forEach(bookEntry => {
                    //console.log(bookEntry)
                    
                    let favorite = false
                    let toRead = false
                    let toReturn = false
                    
                    favorite = favs.some(favEntry => {
                        if (bookEntry.idLibro === favEntry.libro.idLibro) {
                            console.log("Bua, israel, eres un fiera")
                            return true;
                        }
                    });
                    
                    toRead = reads.some(readEntry => {
                        if (bookEntry.idLibro === readEntry.libro.idLibro) {
                            console.log("Bua, israel, eres un fiera 2")
                            return true;
                        }
                    });
                    toReturn = returns.some(returnEntry => {
                        if (bookEntry.idLibro === returnEntry.libro.idLibro && !returnEntry.devuelto) {
                            console.log("Bua, israel, eres un fiera 3")
                            return true;
                        }
                    });
                    

                    let actualBook = new Book(bookEntry, favorite, toRead, toReturn)
                    console.log(actualBook)

                    if (actualBook.getBestBool()){
                        this.#library.pushBestList(actualBook)
                    }
                    if (actualBook.getReturnBool()){
                        this.#library.pushReturnList(actualBook)
                    }
                    if (actualBook.getFavBool()){
                        this.#library.pushFavList(actualBook)
                    }
                    if (actualBook.getReadBool()){
                        this.#library.pushReadList(actualBook)
                    }
                    

                    this.#appView.createBook(
                        actualBook.getId(),
                        actualBook.getCover(),
                        actualBook.getTitle(),
                        actualBook.getAuthor(),
                        actualBook.getPublisher(), 
                        actualBook.getSynopsis(),
                        actualBook.getCategory(),
                        actualBook.getYear(),
                        actualBook.getIsbn(),
                        actualBook.getBestBool(),
                        actualBook.getFavBool(),
                        actualBook.getReturnBool(),
                        actualBook.getReadBool(),
                        this)
                });
            })
            .catch((error) => {
                console.log("Get-all-books " + error.message.substring(error.message.lastIndexOf("Error")))

            })
        console.log("Favorites:")
        console.log(this.#library.getFavList())
        console.log("Best Sellers:")
        console.log(this.#library.getBestList())
        console.log("To Retruns:")
        console.log(this.#library.getReturnList())
        console.log("To Read:")
        console.log(this.#library.getReadList())
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