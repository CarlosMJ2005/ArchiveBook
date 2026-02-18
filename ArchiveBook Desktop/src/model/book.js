export class Book {

    // Model data
    #Id
    #Cover
    #Title
    #Synopsis
    #Author
    #Year
    #Isbn
    #Publisher
    #Category
    
    #bestBool
    #favBool
    #returnBool
    #readBool


    constructor(book, favourite, toRead, toReturn, image = "./images/easter.jpg") {
        this.#Cover = image
        this.#Id = book.idLibro
        this.#Title = book.titulo
        this.#Synopsis = book.sinopsis
        this.#Publisher = book.editorial.nombre
        this.#Author = book.autor.nombre + " " + book.autor.apellidos
        this.#Year = book.agnoPublicacion
        this.#Isbn = book.isbn 
        this.#Category = book.categoria

        this.#bestBool = book.bestSeller
        this.#favBool = favourite
        this.#returnBool = toRead
        this.#readBool = toReturn
    }

    init() {
        
    }

    getCover(){
        return this.#Cover
    }
    getTitle(){
        return this.#Title
    }
    getSynopsis(){
        return this.#Synopsis
    }
    getAuthor(){
        return this.#Author
    }
    getYear(){
        return this.#Year
    }
    getIsbn(){
        return this.#Isbn
    }
    getPublisher(){
        return this.#Publisher
    }
    getCategory(){
        return this.#Category
    }
    getId(){
        return this.#Id
    }

    getBestBool(){
        return this.#bestBool
    }
    getFavBool(){
        return this.#favBool
    }
    getReturnBool(){
        return this.#returnBool
    }
    getReadBool(){
        return this.#readBool
    }

}
