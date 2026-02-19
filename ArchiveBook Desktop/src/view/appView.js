export class appView {
    #filters
    #detail

    #detailCover
    #detailTitle
    #detailSynopsis
    #detailAuthor
    #detailYear
    #detailIsbn
    #detailPublisher
    #detailCategories
    #searcherInput

    #baseFav
    #baseAll
    #baseRead
    #baseReturn
    #baseBest

    constructor() {
        this.#filters = document.getElementById("right-popup")
        this.#detail = document.getElementById("center-popup")
        this.#searcherInput = document.getElementById("searcher")

        this.#detailCover = document.getElementById("detailCover")
        this.#detailTitle = document.getElementById("detailTittle")
        this.#detailSynopsis = document.getElementById("detailSynopsis")
        this.#detailAuthor = document.getElementById("detailAuthor")
        this.#detailPublisher = document.getElementById("detailPublisher")
        this.#detailCategories = document.getElementById("detailCategories")
        this.#detailYear = document.getElementById("detailYear")
        this.#detailIsbn = document.getElementById("detailIsbn")

        this.#baseAll = document.getElementById("all-list")
        this.#baseFav = document.getElementById("favorites-list")
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
    createBook(idGet,coverGet,titleGet,authorGet,publisherGet,synopsisGet,categoryGet,yearGet,isbnGet,bestGet,favGet,returnGet,readGet, that) {
        //console.log(that)

        let div = document.createElement('div');
        div.id = "book"

        //creación de la estructura interna
        let img = document.createElement('img');
        img.id = "cover"
        img.src = coverGet


        let title = document.createElement('h3');
        title.textContent = titleGet

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
            img,
            titleGet, 
            authorGet, 
            synopsisGet, 
            isbnGet, 
            yearGet,
            categoryGet,
            publisherGet,
        ))

        //Botones

        let divButtons = document.createElement('div');
        divButtons.id = "justified"
        divButtons.classList.add('d-flex', 'gap-2')

        let best = document.createElement('img');
        best.src = "./images/star.png"
        best.classList.add('minibutton')

        let favButton = document.createElement('button');
        favButton.classList.add('icon-btn');
        favButton.addEventListener('click', () => that.tapFavorite(favButton,idGet));

        let favIcon = document.createElement('img');
        favIcon.src = "./images/heart.png"
        favIcon.classList.add('minibutton')

        let readButton = document.createElement('button');
        readButton.classList.add('icon-btn');
        readButton.addEventListener('click', () => that.tapToRead(readButton,idGet));

        let readIcon = document.createElement('img');
        readIcon.src = "./images/bookmark.png"
        readIcon.classList.add('minibutton')

        let returnButton = document.createElement('button');
        returnButton.classList.add('icon-btn');
        returnButton.addEventListener('click', () => that.tapToReturn(returnButton,idGet));

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

        if(favGet){
            favIcon.src= "./images/heart-bold.png"
        }
        if(readGet){ // cambiar por readGet
            readIcon.src= "./images/bookmark-bold.png"
        }
        if(returnGet){ // cambiar por returnGet
            returnIcon.src= "./images/notification-bold.png"
        }
        if(bestGet){
            best.src = "./images/star-bold.png"
        }



        if (bestGet) {
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
                    titleGet,
                    authorGet,
                    synopsisGet,
                    isbnGet,
                    yearGet,
                    categoryGet,
                    publisherGet
                )
            );

            cloneFavButton.addEventListener('click', () => that.tapFavorite(cloneFavButton,idGet));
            cloneReadButton.addEventListener('click', () => that.tapToRead(cloneReadButton,idGet));
            cloneReturnButton.addEventListener('click', () => that.tapToReturn(cloneReturnButton,idGet));

            this.#baseBest.appendChild(clone);
        }

        if (readGet) { //cambiar por read

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
                    titleGet,
                    authorGet,
                    synopsisGet,
                    isbnGet,
                    yearGet,
                    categoryGet,
                    publisherGet,
                )
            );

            cloneFavButton.addEventListener('click', () => that.tapFavorite(cloneFavButton,idGet));
            cloneReadButton.addEventListener('click', () => that.tapToRead(cloneReadButton,idGet));
            cloneReturnButton.addEventListener('click', () => that.tapToReturn(cloneReturnButton,idGet));

            this.#baseRead.appendChild(clone);
        }

        if (returnGet) { //cambiar por retun

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
                    titleGet,
                    authorGet,
                    synopsisGet,
                    isbnGet,
                    yearGet,
                    categoryGet,
                    publisherGet,
                )
            );

            cloneFavButton.addEventListener('click', () => that.tapFavorite(cloneFavButton,idGet));
            cloneReadButton.addEventListener('click', () => that.tapToRead(cloneReadButton,idGet));
            cloneReturnButton.addEventListener('click', () => that.tapToReturn(cloneReturnButton,idGet));

            this.#baseReturn.appendChild(clone);
        }

        if (favGet) {

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
                    titleGet,
                    authorGet,
                    synopsisGet,
                    isbnGet,
                    yearGet,
                    categoryGet,
                    publisherGet,
                )
            );

            cloneFavButton.addEventListener('click', () => that.tapFavorite(cloneFavButton,idGet));
            cloneReadButton.addEventListener('click', () => that.tapToRead(cloneReadButton,idGet));
            cloneReturnButton.addEventListener('click', () => that.tapToReturn(cloneReturnButton,idGet));

            this.#baseFav.appendChild(clone);
        }


        this.#baseAll.appendChild(div);


    }

    openDescriptionPopUp(cover, title, author, synopsis, isbn, year, categories, publisher) {
        console.log("abro detalle")
        console.log()
        this.#detail.classList.remove('d-none')
        this.#detailCover.src = cover.src;
        this.#detailTitle.textContent = title
        this.#detailSynopsis.textContent = synopsis
        this.#detailAuthor.textContent = author
        this.#detailCategories.textContent = categories
        this.#detailPublisher.textContent = publisher
        this.#detailIsbn.textContent = isbn
        this.#detailYear.textContent = year
    }

    tapFavorite(button, bool) {
        if (!bool) {
            button.querySelector('img').src = './images/heart-bold.png';
        }
        else {
            button.querySelector('img').src = './images/heart.png'
        }
    }

    tapToReturn(button, bool) {
        if (!bool) {
            button.querySelector('img').src = './images/notification-bold.png';
        }
        else {
            button.querySelector('img').src = './images/notification.png'
        }
    }
    
    tapToRead(button, bool) {
        if (!bool) {
            button.querySelector('img').src = './images/bookmark-bold.png';
        }
        else {
            button.querySelector('img').src = './images/bookmark.png'
        }
    }
    //Steven
    updateSearchInterface(placeholderText) {
        this.#searcherInput.placeholder = placeholderText;
        this.#searcherInput.value = "";
    }

    getSearchValue() {
        return this.#searcherInput.value.toLowerCase().trim();
    }

    clearAllLists() {
        this.#baseAll.innerHTML = "";
        this.#baseFav.innerHTML = "";
        this.#baseBest.innerHTML = "";
        this.#baseRead.innerHTML = "";
        this.#baseReturn.innerHTML = "";
    }
}