const  { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    loadUser : () => ipcRenderer.invoke('load-user'),
    goToLog : () => ipcRenderer.invoke('go-to-log'),
    verify : (email, password, state) => ipcRenderer.invoke('verify-user',email, password, state),
    getAllBooks : () =>ipcRenderer.invoke('get-books')
});