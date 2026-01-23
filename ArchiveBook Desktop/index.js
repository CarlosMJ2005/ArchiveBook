// 1.
const { app, BrowserWindow, ipcMain } = require('electron');

const path = require('path')

// 2.
let logwindow;
let appwindow;
let cerrar = true;

// 3.
app.on('ready', () => {
  // 4.
  logwindow = new BrowserWindow({
    resizable : false,
    width: 1920 ,
    height: 890,
    webPreferences: {
          preload: path.join(__dirname, 'preload.js')
        }
  });
  logwindow.loadFile('login.html');

  logwindow.on('close', function () {
    if(cerrar){
      cerrar = false;
      appwindow.close()
    }
  })

  appwindow = new BrowserWindow({
    resizable : false,
    width: 1920 ,
    height: 890
  });
  appwindow.loadFile('app.html');
  appwindow.hide()

  appwindow.on('close', function () {
    if(cerrar){
      cerrar = false;
      logwindow.close()
    }
  })


  ipcMain.on('open-window', () => {
    console.log("entro en open window")
    appwindow.show()
    logwindow.hide()
  })
    
})