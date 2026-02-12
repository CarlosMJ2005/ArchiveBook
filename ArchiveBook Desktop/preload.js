const { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    loadUser: () => ipcRenderer.invoke('load-user'),
    goToLog: () => ipcRenderer.invoke('go-to-log'),
    verify: (email, password, state) => ipcRenderer.invoke('verify-user', email, password, state),
    verify: (email, password, state) => ipcRenderer.invoke('verify-user', email, password, state),
    getAllBooks: () => ipcRenderer.invoke('get-books'),
    getFavourites: () => ipcRenderer.invoke('get-favourites'),
    getBest: () => ipcRenderer.invoke('get-best'),
    getToRead: () => ipcRenderer.invoke('get-toRead'),
    getToReturn: () => ipcRenderer.invoke('get-toReturn'),
    getFavourites: () => ipcRenderer.invoke('get-favourites'),
    load: () => {
        return new Promise(resolve => {
            ipcRenderer.once('load', () => resolve());
        });
    }
});