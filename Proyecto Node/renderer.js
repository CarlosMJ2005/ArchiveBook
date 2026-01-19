/**
 * This file is loaded via the <script> tag in the index.html file and will
 * be executed in the renderer process for that window. No Node.js APIs are
 * available in this process because `nodeIntegration` is turned off and
 * `contextIsolation` is turned on. Use the contextBridge API in `preload.js`
 * to expose Node.js functionality from the main process.
 */

document.addEventListener('DOMContentLoaded', () => { // esto te evita tener que crear una clase en la que meter las cosas

    document.getElementById('submit-btn').addEventListener('click', handleSubmit); // añade un sensor de eventos al botón de de envio
    document.getElementById('cancel-btn').addEventListener('click', resetForm); // añade un sensor de eventos al botón de de cancelar para que reinice el formulario

    cargarUsuarios();

    async function handleSubmit() { //manejador de envio de datos sincrono, trabaja manejando las promesas de respuesta de datos con con try y catch
        //crea variables para trabajar más cómodo
        const id = document.getElementById('user-id').value; 
        const nombre = document.getElementById('nombre').value;
        const email = document.getElementById('email').value;

        if ((!nombre) || (!email)) { //combrueba que ambos campos hayan sido introducidos
            console.log("Todos los campos son obligatorios")
            return;
        }

        if (id) { // para distinguir entre  crear y editar comprueba que haya id, en caso de que haya sabe que es editar
            //maneja ambas posibilidades con try porque es una funcion asincrona
            try {
                await app.editUser(id, { nombre, email }); // llama al ipc renderer con await, mete entre llaves las claves básicas del usuairio para trabajar con ellas de forma mas cómoda
                console.log("Usuario actualizado con éxito")
            } catch (error) {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
            }
        } else {
            try {
                await app.addUser({ nombre, email }); // llama al ipc renderer con await, mete entre llaves las claves básicas del usuairio para trabajar con ellas de forma mas cómoda
                console.log("Usuario añadido con éxito")
            } catch (error) {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
            }
        }

        resetForm() //llama al reseteo de formulario
        cargarUsuarios(); //llama a cargar usuarios
    }

    function fillForm(usuario) { //se llama cuando se presiona el botón de edit
        //se aplican los valores de usuario a las cajas de texto
        document.getElementById('user-id').value = usuario.id;
        document.getElementById('nombre').value = usuario.nombre;
        document.getElementById('email').value = usuario.email;

        //se cambia 
        document.getElementById('form-title').textContent = "Editar Usuario";
        document.getElementById('submit-btn').textContent = "Ok";
        document.getElementById('submit-btn').classList.replace("btn-success", "btn-primary");

        document.getElementById('cancel-btn').classList.remove("d-none");
    }

    function resetForm() { // se llama tras añadir o editar un usuario
        //se vacian las cajas de texto
        document.getElementById('user-id').value = '';
        document.getElementById('nombre').value = '';
        document.getElementById('email').value = '';

        //se aplican los valores de usuario a las cajas de texto
        document.getElementById('form-title').textContent = "Añadir Usuario";
        document.getElementById('submit-btn').textContent = "Añadir";
        document.getElementById('submit-btn').classList.replace("btn-primary", "btn-success");

        document.getElementById('cancel-btn').classList.add("d-none");
    }

    function cargarUsuarios() {

        const listaUsuarios = document.getElementById('list-usuarios'); //crea una variable para trabajar mas cómodo
        listaUsuarios.innerHTML = ''; // vacia todo su interior


        app.loadUsers() //trabaja manejando las promesas de respuesta  de datos con con .then y .catch
            .then((usuarios) => {

                usuarios.forEach((usuario) => { //cuando encuentra la lista de usuarios empieza a crear la estructura

                    let li = document.createElement('li'); //crea el objeto sobre el que se va a apoyar el usuario
                    li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');

                    //creación de la estructura interna
                    let userText = document.createElement('span');
                    userText.textContent = `${usuario.id} - ${usuario.nombre} - ${usuario.email}`;

                    let buttonContainer = document.createElement('div');

                    let editButton = document.createElement('button');
                    editButton.classList.add('btn', 'btn-sm', 'btn-warning', 'me-2');
                    editButton.textContent = 'Editar';
                    editButton.addEventListener('click', () => fillForm(usuario)); // llama al cambio de 

                    let deleteButton = document.createElement('button');
                    deleteButton.classList.add('btn', 'btn-sm', 'btn-warning', 'me-2');
                    deleteButton.textContent = 'Eliminar';
                    deleteButton.addEventListener('click', () => deleteUsuario(usuario.id));

                    buttonContainer.appendChild(editButton);
                    buttonContainer.appendChild(deleteButton);

                    li.appendChild(userText);
                    li.appendChild(buttonContainer);

                    listaUsuarios.appendChild(li);
                });
            })
            .catch((error) => {
                console.log(error.message.substring(error.message.lastIndexOf("Error")))
            })
    }

    async function deleteUsuario(id) {
        try {
            await app.deleteUser(id);
            console.log("Usuario eliminado con éxito")
            cargarUsuarios();
        } catch (error) {
            console.log(error.message.substring(error.message.lastIndexOf("Error")))
        }
    }
})
