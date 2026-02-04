// 1.
const { app, BrowserWindow, ipcMain } = require('electron');
const fs = require('fs');
const { readFile } = fs.promises;
const path = require('path');

const ficheroUsuario = "./usuario.json"
let myToken

const apiUrl = "http://192.168.207.38:8080/"

// 2.
let logwindow;
let appwindow;
let cerrar = true;

// 3.
app.on('ready', () => {
  // 4.
  logwindow = new BrowserWindow({
    resizable: false,
    width: 1920,
    height: 890,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  });
  logwindow.loadFile('login.html');

  logwindow.on('close', function () {
    if (cerrar) {
      cerrar = false;
      appwindow.close()
    }
  })

  appwindow = new BrowserWindow({
    resizable: false,
    width: 1920,
    height: 890,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  });
  appwindow.loadFile('app.html');
  appwindow.hide()


  appwindow.on('close', function () {
    if (cerrar) {
      cerrar = false;
      logwindow.close()
    }
  })


  const openApp = () => {
    console.log("entro en open window")
    appwindow.show()
    logwindow.hide()
  }
  const openLog = () => {
    console.log("entro en open window")
    logwindow.show()
    appwindow.hide()
  }

  const saveUser = (email, password, state, token) => {
    fs.writeFileSync(ficheroUsuario, JSON.stringify({ email, password, state, token}), null, 2)
  }

  ipcMain.handle('load-user', async () => {
    try {
      const results = JSON.parse(await readFile(ficheroUsuario, "utf8"));
      //myToken = results.token
      return results;
    } catch (err) {
      throw new Error("Error loading user file: " + err);
    }
  });

  ipcMain.handle('get-books', async () => {
  try {
    const url = apiUrl +"api/libros";
    console.log(url)

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-books:', error);
    openLog()
    throw error;
  }
});

  ipcMain.handle('get-favourites', async () => {
  try {
    const url = apiUrl +"api/libros/favoritos/";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-books:', error);
    openLog()
    throw error;
  }
});

ipcMain.handle('get-toRead', async () => {
  try {
    const url = apiUrl +"api/favoritos/";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-books:', error);
    openLog()
    throw error;
  }
});

  ipcMain.handle('get-best', async () => {
  try {
    const url = apiUrl +"api/libros/favoritos/";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-books:', error);
    openLog()
    throw error;
  }
});

ipcMain.handle('get-toReturn', async () => {
  try {
    const url = apiUrl +"api/favoritos/";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-books:', error);
    openLog()
    throw error;
  }
});

  ipcMain.handle('verify-user', async (event, email,password, state) => {
  try {
    let url = apiUrl +"token";

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(email + ":" + password)
      }
    })
    console.log(response)

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    let token = await response.text();
    myToken = token
    console.log(myToken)
    saveUser(email, password, state, token);
    openApp();
    appwindow.webContents.send('load')
    return "Usuario iniciado con Éxito"


  } catch (error) {
    console.error('Error en get-books:', error);
    throw error;
  }
});
})