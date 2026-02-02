export class appView {

    constructor() {
    }

    init() {
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