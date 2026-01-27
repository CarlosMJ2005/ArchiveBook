// 1.
const { app, BrowserWindow, ipcMain } = require('electron');

const fs = require('fs')
import { readFile } from "fs/promises";
const ficheroUsuario = "./usuario.json"

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

  ipcMain.handle('save-user', (event, email,password) => {
    fs.writeFileSync(ficheroUsuario, JSON.stringify({email,password}), null, 2)
  })

  ipcMain.handle('load-user', async () => {
    try {
    const results = JSON.parse(await readFile("user.json", "utf8"));
    return results;
  } catch (err) {
    throw new Error("Error loading user file");
  }
  });

})