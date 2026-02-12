export class book {

    // Model data
    #Cover
    #Title
    #Synopsis
    #Author
    #Year
    #Isbn
    #Publisher
    #Category
    
    #favBool
    #returnBool
    #readBool

    #favList
    #allList
    #readList
    #returnList
    #bestList


    constructor(book, image = "") {
        this.#Cover = image
        this.#Title = book.titulo
        this.#Synopsis = book.sinopsis
        this.#Publisher = book.editorial.nombre
        this.#Author = book.autor.nombre + " " + book.autor.apellidos
        this.#Year = book.agnoPublicacion
        this.#Isbn = book.isbn 
        this.#Category = book.categoria
    }

    init() {
        
    }

}
