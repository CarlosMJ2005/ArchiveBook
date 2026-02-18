export class Library {

    // Model data
    #bestList = []
    #favList = []
    #returnList = []
    #readList = []
    #allList = []


    constructor() {

    }

    init() {
        
    }

    getBestList(){
        return this.#bestList
    }
    setBestList(books){
        this.#bestList = books
    }
    pushBestList(book){
        this.#bestList.push(book)
    }
    filterBestList(book){
        this.#bestList = this.#bestList.filter(i => i !== book)
    }

    getFavList(){
        return this.#favList
    }
    setFavList(books){
        this.#favList = books
    }
    pushFavList(book){
        this.#favList.push(book)
    }
    filterFavList(book){
        this.#favList = this.#favList.filter(i => i !== book)
    }

    getReturnList(){
        return this.#returnList
    }
    setReturnList(books){
        this.#returnList = books
    }
    pushReturnList(book){
        this.#returnList.push(book)
    }
    filterReturnList(book){
        this.#returnList = this.#returnList.filter(i => i !== book)
    }

    getReadList(){
        return this.#readList
    }
    setReadList(books){
        this.#readList = books
    }
    pushReadList(book){
        this.#readList.push(book)
    }
    filterReadList(book){
        this.#readList = this.#readList.filter(i => i !== book)
    }

    getAllList(){
        return this.#allList
    }
    setAllList(books){
        this.#allList = books
    }
    pushAllList(book){
        this.#allList.push(book)
    }
    filterAllList(book){
        this.#allList = this.#allList.filter(i => i !== book)
    }

    eraseAll(){
        this.#bestList = []
        this.#favList = []
        this.#returnList = []
        this.#readList = []
        this.#allList = []
    }
}
