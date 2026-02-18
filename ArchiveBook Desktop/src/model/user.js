export class User {

    // Model data
    #email
    #password
    
    #state


    constructor(email, password, state = false) {
        this.#email = email
        this.#password = password
        this.#state = state
    }

    init() {
        
    }

    getEmail(){
        return this.#email
    }
    getPassword(){
        return this.#password
    }
    getState(){
        return this.#state
    }
}
