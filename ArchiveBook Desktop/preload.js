const  { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    windowopen : () => ipcRenderer.send('open-window'), //llama a la obtención de usuarios
});