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
//import { app } from 'electron';



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

    }

    // Initializing classes
    init() {

    }

    setUsuarioActivo(usuario) {
         this.#usuarioActivo = usuario;   
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
                this.#loginView.showErrorLog("Username or password error")
            })
    }
    async signin() {

        const email = this.#loginView.getEmailSign()
        const password = this.#loginView.getPasswordSign()
        const confirmation = this.#loginView.getConfirmationSign()

        if (password != confirmation) {
            this.#loginView.showErrorSign("Passwords don't match")
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
                    this.#loginView.showErrorSign("That user already exists, try to log in")
                })
        }


    }

    //book buttons

    async tapFavorite(button, idLibro) { //añadir funcionalidad con api
        //console.log(idLibro)
        if (button.querySelector('img').src.endsWith('heart.png')) { // cuando se marca como fav
            try {
                console.log(await app.addFavorite(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
        }
        else {// cuando se desmarca como fav
            try {
                console.log(await app.removeFavorite(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
        }
    }
    async tapToReturn(button, idLibro) { //añadir funcionalidad con api
        console.log(idLibro)
        if (button.querySelector('img').src.endsWith('notification.png')) {
            try {
                console.log(await app.addToReturn(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
        }
        else {
            try {
                console.log(await app.removeToReturn(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
        }
    }
    async tapToRead(button, idLibro) { //añadir funcionalidad con api
        console.log(idLibro)
        if (button.querySelector('img').src.endsWith('bookmark.png')) {
            try {
                console.log(await app.addToRead(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
        }
        else {
            try {
                console.log(await app.removeToRead(idLibro))
                this.startLoad()
            } catch (error) {
                console.log(error)
            }
            
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
        
        const [favs,returns, reads, ] = await Promise.all([
        app.getFavorites(),
        app.getToReturn(),
        app.getToRead()
        ]);

        
        //console.log(returns)
        
        this.loadAll(favs, returns, reads)
    }
    
    async loadAll(favs, returns, reads) {

    try {

        const datos = await app.getAllBooks()

        this.#appView.clearAllLists()
        this.#library.eraseAll()

        for (const bookEntry of datos) {

            let favorite = favs.some(f => 
                bookEntry.idLibro === f.libro.idLibro
            )

            let toRead = reads.some(r => 
                bookEntry.idLibro === r.libro.idLibro
            )

            let toReturn = returns.some(r => 
                bookEntry.idLibro === r.libro.idLibro && !r.devuelto
            )

            let cover = null

            let actualBook

            try {
                cover = await app.getCover(bookEntry.idLibro)
            } catch (error) {
                console.log("Sin portada para libro " + bookEntry.idLibro)
            }

            if (cover) {
                actualBook = new Book(
                    bookEntry,
                    favorite,
                    toRead,
                    toReturn,
                    cover
                )
            } else {
                actualBook = new Book(
                    bookEntry,
                    favorite,
                    toRead,
                    toReturn
                )
            }



            if (actualBook.getBestBool())
                this.#library.pushBestList(actualBook)

            if (actualBook.getReturnBool())
                this.#library.pushReturnList(actualBook)

            if (actualBook.getFavBool())
                this.#library.pushFavList(actualBook)

            if (actualBook.getReadBool())
                this.#library.pushReadList(actualBook)

            this.#library.pushAllList(actualBook)

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
                this
            )
        }

    } catch (error) {
        console.log("Get-all-books " + error.message)
    }
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

    if (!query) {
        this.startLoad(); // restaura lista completa
        return;
    }

    const filtered = allBooks.filter(book => {
        let targetValue = "";

        switch(this.#searchMode) {
            case 'author': targetValue = book.getAuthor(); break;
            case 'publisher': targetValue = book.getPublisher(); break;
            case 'category': targetValue = book.getCategory(); break;
            default: targetValue = book.getTitle();
        }

        return (targetValue ?? "")
            .toString()
            .toLowerCase()
            .includes(query);
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