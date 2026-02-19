const { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    loadUser: () => ipcRenderer.invoke('load-user'),
    verify: (email, password, state) => ipcRenderer.invoke('verify-user', email, password, state),
    addUser: (email, password) => ipcRenderer.invoke('add-user', email, password),

    getAllBooks: () => ipcRenderer.invoke('get-books'),
    getFavorites: () => ipcRenderer.invoke('get-favorites'),
    getToRead: () => ipcRenderer.invoke('get-toRead'),
    getToReturn: () => ipcRenderer.invoke('get-toReturn'),

    addFavorite: (id) => ipcRenderer.invoke('add-favorite', id),
    addToRead: (id) => ipcRenderer.invoke('add-toRead', id),
    addToReturn: (id) => ipcRenderer.invoke('add-toReturn', id),

    removeFavorite: (id) => ipcRenderer.invoke('remove-favorite', id),
    removeToRead: (id) => ipcRenderer.invoke('remove-toRead', id),
    removeToReturn: (id) => ipcRenderer.invoke('remove-toReturn', id),

    load: () => {
        return new Promise(resolve => {
            ipcRenderer.on('load', (event, email, password, state) => resolve({ email, password, state }));
        });
    }
});