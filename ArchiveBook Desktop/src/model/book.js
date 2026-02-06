export class book {

    // Model data
    #Cover
    #Title
    #Synopsis
    #Author
    #Year
    #Isbn


    constructor(title,author,synopsis,isbn,year, cover = "./images/easter.jpg") {
        this.#Cover = cover
        this.#Title = title
        this.#Synopsis = synopsis
        this.#Author = author
        this.#Year = year
        this.#Isbn = isbn
    }

    init() {
        
    }

}
