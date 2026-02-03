export class appView {
    #filters
    #detail
    #detailCover
    #detailTittle
    #detailSynopsis
    #detailAuthor
    #detailYear
    #detailIsbn

    constructor() {
        this.#filters = document.getElementById("right-popup")
        this.#detail = document.getElementById("center-popup")

        this.#detailCover = document.getElementById("detailCover")
        this.#detailTittle = document.getElementById("detailTittle")
        this.#detailSynopsis = document.getElementById("detailSynopsis")
        this.#detailAuthor = document.getElementById("detailAuthor")
        this.#detailYear = document.getElementById("detailYear")
        this.#detailIsbn = document.getElementById("detailIsbn")
    }

    init() {
    }
    closePopUp(){
        console.log("cierro detalle")
        this.#detail.classList.add('d-none')
    }
    openFilterPopUp(){
        console.log("abro filtros")
        if (this.#filters.classList.contains('d-none')) {
            this.#filters.classList.remove('d-none')
        }
        else{
            this.#filters.classList.remove('d-none')
        }
    }
    openDescriptionPopUp(cover,tittle,author,synopsis,isbn,year){
        console.log("abro detalle")
        this.#detail.classList.remove('d-none')
        this.#detailCover.src = cover.src;
        this.#detailTittle.value = tittle.value
        this.#detailSynopsis.value = author.value
        this.#detailAuthor.value = synopsis.value
        this.#detailYear.value = isbn.value
        this.#detailIsbn.value = year.value
    }

    tapFavourite(button){
        if(button.querySelector('img').src.endsWith('heart.png')){
            button.querySelector('img').src = './images/heart-bold.png';
        }
        else{
            button.querySelector('img').src = './images/heart.png'
        }
    }
    tapToReturn(button){
        if(button.querySelector('img').src.endsWith('notification.png')){
            button.querySelector('img').src = './images/notification-bold.png';
            console.log("añado bold")
        }
        else{
            button.querySelector('img').src = './images/notification.png'
            console.log("quito bold")
        }
    }
    tapToRead(button){
        if(button.querySelector('img').src.endsWith('bookmark.png')){
            button.querySelector('img').src = './images/bookmark-bold.png';
            console.log("añado bold")
        }
        else{
            button.querySelector('img').src = './images/bookmark.png'
            console.log("quito bold")
        }
    }
}