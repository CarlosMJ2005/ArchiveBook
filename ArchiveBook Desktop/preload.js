const  { ipcRenderer, contextBridge } = require('electron'); //obligatorio

contextBridge.exposeInMainWorld('app', { //app se puede llamar como quieras
    windowOpen : () => ipcRenderer.send('open-window'), //llama a la obtención de usuarios
    saveUser : (email, password, state) => ipcRenderer.invoke('save-user',email, password, state), //llama a la obtención de usuarios
    loadUser : () => ipcRenderer.invoke('load-user'),
    getToken: (callback) => {
    ipcRenderer.on('sentToken', (event, token) => {
      callback(token);
    });
  }
});