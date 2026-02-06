export class appView {
    #filters
    #detail
    #detailCover
    #detailTitle
    #detailSynopsis
    #detailAuthor
    #detailYear
    #detailIsbn

    #baseFav
    #baseAll
    #baseRead
    #baseReturn
    #baseBest

    constructor() {
        this.#filters = document.getElementById("right-popup")
        this.#detail = document.getElementById("center-popup")

        this.#detailCover = document.getElementById("detailCover")
        this.#detailTitle = document.getElementById("detailTittle")
        this.#detailSynopsis = document.getElementById("detailSynopsis")
        this.#detailAuthor = document.getElementById("detailAuthor")
        this.#detailYear = document.getElementById("detailYear")
        this.#detailIsbn = document.getElementById("detailIsbn")

        this.#baseAll = document.getElementById("detailIsbn")
        this.#baseFav = document.getElementById("detailIsbn")
        this.#baseBest = document.getElementById("detailIsbn")
        this.#baseRead = document.getElementById("detailIsbn")
        this.#baseReturn = document.getElementById("detailIsbn")
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
            this.#filters.classList.add('d-none')
        }
    }
    createBook(book, where){
        let div = document.createElement('div');
        div.id = "book"

        //creación de la estructura interna
        let img = document.createElement('img');
        img.id = "cover"
        img.src = "./images/easter.jpg" //cambiar

        let title = document.createElement('h3');
        title.textContent = book.titulo
        title.classList.add('d-none')

        let author = document.createElement('p');
        author.textContent = book.autor
        title.classList.add('d-none')

        let publisher = document.createElement('p');
        publisher.textContent = book.editorial.nombre
        title.classList.add('d-none')

        let year = document.createElement('p');
        year.textContent = book.agnoPublicacion
        title.classList.add('d-none')

        let isbn = document.createElement('p');
        isbn.textContent = book.isbn 
        title.classList.add('d-none')

        let synopsis = document.createElement('p');
        synopsis.textContent = book.sinopsis
        title.classList.add('d-none')

        let category = document.createElement('p');
        category.textContent = book.categoria
        title.classList.add('d-none')

        let divButtons = document.createElement('div');
        divButtons.id = "justified"
        divButtons.classList.add('d-flex','gap-2')

        let best =  document.createElement('img');
        if(book.bestSeller){
            best.src = "./images/star-bold.png"
        }else{
            best.src = "./images/star.png"
        }
        best.classList.add('minibutton')

        let favButton = document.createElement('button');
        favButton.classList.add('icon-btn');
        favButton.addEventListener('click', () => fillForm(usuario)); // llama al cambio de 

        let readButton = document.createElement('button');
        readButton.classList.add('icon-btn');
        readButton.addEventListener('click', () => deleteUsuario(usuario.id));

        let returnButton = document.createElement('button');
        returnButton.classList.add('icon-btn');
        returnButton.addEventListener('click', () => deleteUsuario(usuario.id));


        divButtons.appendChild(best);
        divButtons.appendChild(favButton);
        divButtons.appendChild(readButton);
        divButtons.appendChild(returnButton);

        li.appendChild(userText);
        li.appendChild(buttonContainer);

        listaUsuarios.appendChild(li);

        switch (where){
            case "all":
                break;
            case "fav":
                break;
            case "best":
                break;
            case "read":
                break;
            case "return":
                break;
        }
    }
    openDescriptionPopUp(cover,title,author,synopsis,isbn,year){
        console.log("abro detalle")
        this.#detail.classList.remove('d-none')
        this.#detailCover.src = cover.src;
        this.#detailTitle.textContent = title
        this.#detailSynopsis.textContent = synopsis
        this.#detailAuthor.textContent = author
        this.#detailIsbn.textContent = isbn
        this.#detailYear.textContent = year
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