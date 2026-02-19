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
    #searchMode = 'title';


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

    tapFavourite(button, idLibro) { //añadir funcionalidad con api
        this.#appView.tapFavourite(button)
        console.log(idLibro)
    }
    tapToReturn(button, idLibro) { //añadir funcionalidad con api
        this.#appView.tapToReturn(button)
        console.log(idLibro)
    }
    tapToRead(button, idLibro) { //añadir funcionalidad con api
        this.#appView.tapToRead(button)
        console.log(idLibro)
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
        
        const [favs, returns, reads] = await Promise.all([
        app.getFavourites(),
        app.getToReturn(),
        app.getToRead()
        ]);

        
        //console.log(returns)
        
        this.loadAll(favs , returns, reads)
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
                JSON.parse(JSON.stringify(datos)).forEach(bookEntry => {
                    //console.log(bookEntry)
                    
                    let favourite = false
                    let toRead = false
                    let toReturn = false
                    
                    favourite = favs.some(favEntry => {
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
                    
                    //sT this.#library.pushAllList(new Book(bookEntry, favourite, toRead, toReturn));
                    //sT this.#library.pushAllList(actualBook);
                    let actualBook = new Book(bookEntry, favourite, toRead, toReturn)
                    this.#library.pushAllList(actualBook);
                    
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
    //Steven
    setSearchMode(mode) {
        this.#searchMode = mode;
        let placeholder = "Nombre del libro...";
        if (mode === 'author') placeholder = "Nombre del autor...";
        else if (mode === 'publisher') placeholder = "Nombre de la editorial...";
        else if (mode === 'category') placeholder = "Categoría...";

        this.#appView.updateSearchInterface(placeholder);
        this.#appView.openFilterPopUp(); 
    }

    executeSearch() {
        const query = this.#appView.getSearchValue();
        const allBooks = this.#library.getAllList();
        
        const filtered = allBooks.filter(book => {
            let targetValue = "";
            switch(this.#searchMode) {
                case 'author': targetValue = book.getAuthor(); break;
                case 'publisher': targetValue = book.getPublisher(); break;
                case 'category': targetValue = book.getCategory(); break;
                default: targetValue = book.getTitle();
            }
            return targetValue.toLowerCase().includes(query);
        });

        this.#appView.clearAllLists();
        filtered.forEach(book => {
            this.#appView.createBook(
                book.getId(), book.getCover(), book.getTitle(), 
                book.getAuthor(), book.getPublisher(), book.getSynopsis(),
                book.getCategory(), book.getYear(), book.getIsbn(),
                book.getBestBool(), book.getFavBool(), book.getReturnBool(),
                book.getReadBool(), this
            );
        });
    }
}