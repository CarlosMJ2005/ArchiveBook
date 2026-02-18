export class loginView {
    #emailLog
    #passwordlog
    #emailSign
    #passwordSign
    #confirmationSign
    #structureLog
    #structureSign
    #stateCheckbox
    #errorLabelLogin

    constructor() {
        this.#emailLog = document.getElementById("email-log")
        this.#passwordlog = document.getElementById("password-log")

        this.#emailSign = document.getElementById("email-sign")
        this.#passwordSign = document.getElementById("password-sign")
        this.#confirmationSign = document.getElementById("confirmation-sign")

        this.#structureLog = document.getElementById('log')
        this.#structureSign = document.getElementById('sign')

        this.#stateCheckbox = document.getElementById('state')

        this.#errorLabelLogin = document.getElementById('error-label-login')
    }

    init() {
    }
    showError(error){
        this.#errorLabelLogin.innerText = error
        this.#errorLabelLogin.classList.remove("d-none")
        this.reset()
    }
    reset(trueReset){
        if (trueReset){
            this.#emailLog.value = ""
        }
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
            this.reset()
        }
    }
    showPassword(icon,input){
        if(input.type == "password"){
            console.log("cambio")
            input.type = "text"
            icon.src = "./images/eye.png"
        }
        else{
            console.log("descambio")
            input.type = "password"
            icon.src = "./images/closed_eye.png"
        }
    }
    getEmailLog(){
        return this.#emailLog.value
    }
    setEmailLog(email){
        this.#emailLog.value = email
    }
    getEmailSign(){
        return this.#emailSign.value
    }


    getPasswordLog(){
        return this.#passwordlog.value
    }
    setPasswordLog(password){
        this.#passwordlog.value = password
    }


    getStateCheckbox(){
        return this.#stateCheckbox.checked
    }
    setStateCheckbox(value){
        this.#stateCheckbox.checked = value
    }

    
    getPasswordSign(){
        return this.#passwordSign.value
    }
    getConfirmationSign(){
        return this.#confirmationSign.value
    }
    
}