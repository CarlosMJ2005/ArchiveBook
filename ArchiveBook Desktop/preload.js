const { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    loadUser: () => ipcRenderer.invoke('load-user'),
    verify: (email, password, state) => ipcRenderer.invoke('verify-user', email, password, state),
    addUser: (email, password) => ipcRenderer.invoke('add-user', email, password),
    getAllBooks: () => ipcRenderer.invoke('get-books'),
    getFavourites: () => ipcRenderer.invoke('get-favourites'),
    getToRead: () => ipcRenderer.invoke('get-toRead'),
    getToReturn: () => ipcRenderer.invoke('get-toReturn'),
    load: () => {
        return new Promise(resolve => {
            ipcRenderer.once('load', (event, email, password, state) => resolve({ email, password, state }));
        });
    }
});