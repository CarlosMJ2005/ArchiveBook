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

        this.#baseAll = document.getElementById("all-list")
        this.#baseFav = document.getElementById("favourites-list")
        this.#baseBest = document.getElementById("best-sellers-list")
        this.#baseRead = document.getElementById("to-read-list")
        this.#baseReturn = document.getElementById("to-return-list")
    }

    init() {
    }
    closePopUp() {
        console.log("cierro detalle")
        this.#detail.classList.add('d-none')
    }
    openFilterPopUp() {
        console.log("abro filtros")
        if (this.#filters.classList.contains('d-none')) {
            this.#filters.classList.remove('d-none')
        }
        else {
            this.#filters.classList.add('d-none')
        }
    }
    createBook(book, where, that) {
        //console.log(that)

        let div = document.createElement('div');
        div.id = "book"

        //creación de la estructura interna
        let img = document.createElement('img');
        img.id = "cover"
        img.src = "./images/easter.jpg" //cambiar


        let title = document.createElement('h3');
        title.textContent = book.titulo

        /*
        let author = document.createElement('p');
        author.textContent = book.autor.nombre + " " + book.autor.apellidos
        author.classList.add('d-none')

        let publisher = document.createElement('p');
        publisher.textContent = book.editorial.nombre
        publisher.classList.add('d-none')

        let year = document.createElement('p');
        year.textContent = book.agnoPublicacion
        year.classList.add('d-none')

        let isbn = document.createElement('p');
        isbn.textContent = book.isbn 
        isbn.classList.add('d-none')

        let synopsis = document.createElement('p');
        synopsis.textContent = book.sinopsis
        synopsis.classList.add('d-none')

        let category = document.createElement('p');
        category.textContent = book.categoria
        category.classList.add('d-none')
        */

        img.addEventListener('click', () => this.openDescriptionPopUp(
            img, book.titulo, book.autor.nombre + " " + book.autor.apellidos, book.sinopsis, book.isbn, book.agnoPublicacion))

        //Botones

        let divButtons = document.createElement('div');
        divButtons.id = "justified"
        divButtons.classList.add('d-flex', 'gap-2')

        let best = document.createElement('img');

        best.classList.add('minibutton')

        let favButton = document.createElement('button');
        favButton.classList.add('icon-btn');
        favButton.addEventListener('click', () => that.tapFavourite(favButton));

        let favIcon = document.createElement('img');
        favIcon.src = "./images/heart.png"
        favIcon.classList.add('minibutton')

        let readButton = document.createElement('button');
        readButton.classList.add('icon-btn');
        readButton.addEventListener('click', () => that.tapToRead(readButton));

        let readIcon = document.createElement('img');
        readIcon.src = "./images/bookmark.png"
        readIcon.classList.add('minibutton')

        let returnButton = document.createElement('button');
        returnButton.classList.add('icon-btn');
        returnButton.addEventListener('click', () => that.tapToReturn(returnButton));

        let returnIcon = document.createElement('img');
        returnIcon.src = "./images/notification.png"
        returnIcon.classList.add('minibutton')

        favButton.appendChild(favIcon);
        readButton.appendChild(readIcon);
        returnButton.appendChild(returnIcon);

        divButtons.appendChild(best);
        divButtons.appendChild(favButton);
        divButtons.appendChild(readButton);
        divButtons.appendChild(returnButton);

        div.appendChild(img);
        div.appendChild(title);
        /*
        div.appendChild(year);
        div.appendChild(author);
        div.appendChild(isbn);
        div.appendChild(synopsis);
        div.appendChild(publisher);
        div.appendChild(category);
        */
        div.appendChild(divButtons);


        switch (where) {
            case "all":
                if (book.bestSeller) {
                    best.src = "./images/star-bold.png"

                    const clone = div.cloneNode(true);

                    // Buscar elementos dentro del CLON
                    const cloneImg = clone.querySelector('#cover');
                    const cloneFavButton = clone.querySelectorAll('.icon-btn')[0];
                    const cloneReadButton = clone.querySelectorAll('.icon-btn')[1];
                    const cloneReturnButton = clone.querySelectorAll('.icon-btn')[2];

                    // Reasignar eventos
                    cloneImg.addEventListener('click', () =>
                        this.openDescriptionPopUp(
                            cloneImg,
                            book.titulo,
                            book.autor.nombre + " " + book.autor.apellidos,
                            book.sinopsis,
                            book.isbn,
                            book.agnoPublicacion
                        )
                    );

                    cloneFavButton.addEventListener('click', () => that.tapFavourite(cloneFavButton));
                    cloneReadButton.addEventListener('click', () => that.tapToRead(cloneReadButton));
                    cloneReturnButton.addEventListener('click', () => that.tapToReturn(cloneReturnButton));

                    this.#baseBest.appendChild(clone);
                } else {
                    best.src = "./images/star.png"
                }
                this.#baseAll.appendChild(div);
                break;
            case "fav":
                this.#baseFav.appendChild(div);
                break;
            case "read":
                this.#baseRead.appendChild(div);
                break;
            case "return":
                this.#baseReturn.appendChild(div);
                break;
        }

    }
    eraseAllList() {
        this.#baseAll.innerHTML = ""
    }
    eraseBestList() {
        this.#baseBest.innerHTML = ""
    }
    eraseFavList() {
        this.#baseFav.innerHTML = ""
    }
    eraseReadList() {
        this.#baseRead.innerHTML = ""
    }
    eraseReturnList() {
        this.#baseReturn.innerHTML = ""
    }

    openDescriptionPopUp(cover, title, author, synopsis, isbn, year) {
        console.log("abro detalle")
        console.log()
        this.#detail.classList.remove('d-none')
        this.#detailCover.src = cover.src;
        this.#detailTitle.textContent = title
        this.#detailSynopsis.textContent = synopsis
        this.#detailAuthor.textContent = author
        this.#detailIsbn.textContent = isbn
        this.#detailYear.textContent = year
    }

    tapFavourite(button) {
        if (button.querySelector('img').src.endsWith('heart.png')) {
            button.querySelector('img').src = './images/heart-bold.png';
        }
        else {
            button.querySelector('img').src = './images/heart.png'
        }
    }
    tapToReturn(button) {
        if (button.querySelector('img').src.endsWith('notification.png')) {
            button.querySelector('img').src = './images/notification-bold.png';
            console.log("añado bold")
        }
        else {
            button.querySelector('img').src = './images/notification.png'
            console.log("quito bold")
        }
    }
    tapToRead(button) {
        if (button.querySelector('img').src.endsWith('bookmark.png')) {
            button.querySelector('img').src = './images/bookmark-bold.png';
            console.log("añado bold")
        }
        else {
            button.querySelector('img').src = './images/bookmark.png'
            console.log("quito bold")
        }
    }
}