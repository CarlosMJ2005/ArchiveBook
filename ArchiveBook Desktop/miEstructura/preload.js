/**
 * The preload script runs before `index.html` is loaded
 * in the renderer. It has access to web APIs as well as
 * Electron's renderer process modules and some polyfilled
 * Node.js functions.
 *
 * https://www.electronjs.org/docs/latest/tutorial/sandbox
 */

const  { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    loadUsers : () => ipcRenderer.invoke('get-usuarios'), //llama a la obtención de usuarios
    editUser : (id, usuario) => ipcRenderer.invoke('edit-usuario', id, usuario), //llama a la edición de usuarios
    addUser : (usuario) => ipcRenderer.invoke('add-usuario', usuario), //llama a la creacion de usuarios
    deleteUser : (id) => ipcRenderer.invoke('delete-usuario', id) //llama a la eliminacion de usuarios
});


