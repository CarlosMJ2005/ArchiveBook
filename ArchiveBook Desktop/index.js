// 1.
const { app, BrowserWindow, ipcMain } = require('electron');
const fs = require('fs');
const { readFile } = fs.promises;
const path = require('path');

const ficheroUsuario = "./usuario.json"
let myToken

const apiUrl = "http://192.168.207.83:8080/" // Israel "http://192.168.207.76:8080/" //Steven "http://192.168.207.38:8080/" //Carlos   
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

//---------------------------------------------------------------------------------------------------------------//
//---------------------------------------------------UTILITIES---------------------------------------------------//
//---------------------------------------------------------------------------------------------------------------//

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

//-----------------------------------------------------------------------------------------------------------------------//
//---------------------------------------------------GET FROM DATABASE---------------------------------------------------//
//-----------------------------------------------------------------------------------------------------------------------//

//---------------------------------------------------GET BOOKS---------------------------------------------------//
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
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------GET FAVORITES---------------------------------------------------//

  ipcMain.handle('get-favorites', async () => {
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
    console.log("favorites")
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en get-favorite:', error);
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------GET TO READ---------------------------------------------------//

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
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------GET TO RETURN---------------------------------------------------//

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
    appwindow.close();
    throw error;
  }
});



//---------------------------------------------------------------------------------------------------------------------//
//---------------------------------------------------ADD TO DATABASE---------------------------------------------------//
//---------------------------------------------------------------------------------------------------------------------//

//----------------------------------------------------ADD FAVORITE-----------------------------------------------------//

ipcMain.handle('add-favorite', async (event, id) => {
  try {
    const url = apiUrl +"api/favoritos/" + id;

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    return "marked as favorite complete";
  } catch (error) {
    console.error('Error en add-favorite:', error);
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------ADD TO READ---------------------------------------------------//

ipcMain.handle('add-toRead', async (event, id) => {
  try {
    const url = apiUrl +"api/porLeer/" + id;

    const response = await fetch(url, {
      method: 'POST',
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
    console.error('Error en add-toRead:', error);
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------ADD TO RETURN---------------------------------------------------//

ipcMain.handle('add-toReturn', async (event, id) => {
  try {
    const url = apiUrl +"api/prestamos/" + id;

    const response = await fetch(url, {
      method: 'POST',
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
    console.error('Error en add-toReturn:', error);
    appwindow.close();
    throw error;
  }
});

//-----------------------------------------------------------------------------------------------------------------------//
//---------------------------------------------UPDATE/REMOVE FROMDATABASE------------------------------------------------//
//-----------------------------------------------------------------------------------------------------------------------//

//----------------------------------------------------REMOVE FAVORITE-----------------------------------------------------//

ipcMain.handle('remove-favorite', async (event, id) => {
  try {
    const url = apiUrl +"api/favoritos/" + id;

    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    return "unmarked as favorite complete";
  } catch (error) {
    console.error('Error en remove-favorite:', error);
    appwindow.close();
    throw error;
  }
});

//---------------------------------------------------REMOVE TO READ---------------------------------------------------//

ipcMain.handle('remove-toRead', async (event, id) => {
  try {
    const url = apiUrl +"api/porLeer/" + id;

    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    console.log(response)
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    return "unmarked as toRead complete";
  } catch (error) {
    console.error('Error en remove-toRead:', error);
    appwindow.close();
    throw error;
  }
});


//---------------------------------------------------REMOVE TO RETURN---------------------------------------------------//

ipcMain.handle('remove-toReturn', async (event, id) => {
  try {
    const url = apiUrl +"api/prestamos/devolver/" + id;

    const response = await fetch(url, {
      method: 'PATCH',
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
    
    const data = await response.text();
    return data;
  } catch (error) {
    console.error('Error en remove-toReturn:', error);
    appwindow.close();
    throw error;
  }
});


//-----------------------------------------------------------------------------------------------------------------------//
//-----------------------------------------------------USER MANAGER------------------------------------------------------//
//-----------------------------------------------------------------------------------------------------------------------//


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
    console.log("inicia")
    appwindow.webContents.send('load', email, password, state)
    console.log("acaba")
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