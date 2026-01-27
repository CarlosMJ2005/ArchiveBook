export class loginView {
    #emailLog
    #passwordlog
    #emailSign
    #passwordSign
    #confirmationSign
    #structureLog
    #structureSign
    #stateCheckbox

    constructor() {
        this.#emailLog = document.getElementById("email-log")
        this.#passwordlog = document.getElementById("password-log")

        this.#emailSign = document.getElementById("email-sign")
        this.#passwordSign = document.getElementById("password-sign")
        this.#confirmationSign = document.getElementById("confirmation-sign")

        this.#structureLog = document.getElementById('log')
        this.#structureSign = document.getElementById('sign')

        this.#stateCheckbox = document.getElementById('state')
    }

    init() {
    }
    showError(){
        console.log("Bua, la has cagado")
    }
    reset(){
        this.#emailLog.value = ""
        this.#passwordlog.value = ""
    }
    fulfill(email, password, state){
        this.#emailLog.value = email
        this.#passwordlog.value = password
        console.log("por defecto" + this.#stateCheckbox.value)
        console.log("externo" + state)
        this.#stateCheckbox.checked = state
        console.log("tras cambio" + this.#stateCheckbox.value)
    }
    change(bool = false) {
        console.log("cambio entre tipos")
        if (bool) {
            console.log("muestro sign")
            this.#structureSign.classList.remove("d-none")
            this.#structureLog.classList.add("d-none")
        }
        else {
            console.log("muestro log")
            this.#structureLog.classList.remove("d-none")
            this.#structureSign.classList.add("d-none")
        }
    }
    getEmailLog(){
        return this.#emailLog.value
    }
    getPasswordLog(){
        return this.#passwordlog.value
    }
    getEmailSign(){
        return this.#emailSign.value
    }
    getPasswordSign(){
        return this.#passwordSign.value
    }
    getConfirmationSign(){
        return this.#confirmationSign.value
    }
    getStateCheckbox(){
        return this.#stateCheckbox.checked
    }
}