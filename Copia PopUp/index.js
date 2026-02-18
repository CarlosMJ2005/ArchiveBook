// 1.
const { app, BrowserWindow, ipcMain } = require('electron');
const fs = require('fs');
const { readFile } = fs.promises;
const path = require('path');

const ficheroUsuario = "./usuario.json"
let myToken

const apiUrl = "http://localhost:8080/";
//const apiUrl = "http://192.168.207.83:8080/" // Israel "http://192.168.207.76:8080/" //Steven "http://192.168.207.38:8080/" //Carlos   
// portatil

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
    //console.log("entro en open window")
    appwindow.show()
    logwindow.hide()
  }
  const openLog = () => {
    //console.log("entro en open window")
    logwindow.show()
    appwindow.hide()
  }

  const saveUser = (email, password, state, token) => {
    fs.writeFileSync(ficheroUsuario, JSON.stringify({ email, password, state, token}), null, 2)
  }

  ipcMain.handle('load-user', async () => {
    try {
      const results = JSON.parse(await readFile(ficheroUsuario, "utf8"));
      myToken = results.token
      return results;
    } catch (err) {
      throw new Error("Error loading user file: " + err);
    }
  });



  ipcMain.handle('get-books', async () => {
  try {
    const url = apiUrl +"api/libros";
    //console.log(url)

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
    console.log("TOKEN fav:", myToken)
    const url = apiUrl +"api/favoritos";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    console.log("favourites")
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-favourite:', error);
    openLog()
    throw error;
  }
});

ipcMain.handle('get-toRead', async () => {
  try {
    const url = apiUrl +"api/porLeer";

    

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });

    //console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-toRead:', error);
    openLog()
    throw error;
  }
});

ipcMain.handle('get-toReturn', async () => {
  try {

    console.log("TOKEN:", myToken)
    const url = apiUrl +"api/prestamos";

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    console.log("return")
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-toReturn:', error);
    openLog()
    throw error;
  }
});

  ipcMain.handle('verify-user', async (event, email, password, state) => {
  try {
    let url = apiUrl +"token";

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(email + ":" + password)
      }
    })
    console.log("log in")
    console.log(response)

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    let token = await response.text();
    myToken = token
    console.log("Tocken recibido por el usuario al conectarse:" +myToken)
    saveUser(email, password, state, token);
    openApp();
    appwindow.webContents.send('load', email, password, state)
    return "Usuario iniciado con Éxito"


  } catch (error) {
    console.error('Error en log-in:', error);
    throw error;
  }
});

ipcMain.handle('add-user', async (event, email, password) => {
  try {
    let url = apiUrl +"api/usuarios";

    console.log("email: " + email)
    console.log("password: " + password)

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        correo: email,
        contrasena: password,
        role: "USER"
      })
    });
    console.log("Sign in")
    console.log(response)

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    let user = await response.text()
    console.log(user)
    //console.log(myToken)
    return "Usuario iniciado con Éxito"


  } catch (error) {
    console.error('Error en sign-in:', error);
    throw error;
  }
});
})