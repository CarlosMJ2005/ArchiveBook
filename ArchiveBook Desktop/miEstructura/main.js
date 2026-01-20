// Modules to control application life and create native browser window
const { app, BrowserWindow, ipcMain, dialog } = require('electron');
const path = require('path');
 

function createWindow() {
  // Create the browser window.
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }

    //win.webContents.openDevTools()
  })

  mainWindow.loadFile('index.html');
  //mainWindow.setMenu(null);
}

// Esto carga la connexión desde el principio
app.whenReady().then(() => {

  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0){
      createWindow()
      console.log("he entrado")
    } 
  })

  createWindow()


  // methods to be exported from the main process
  // all methods access the database directly without an API
  // all return a promise

  ipcMain.handle('get-usuarios', async () => {
    return new Promise(async (resolve, reject) => {
      try {
        const [results, fields] = await connection.query('SELECT * FROM `usuarios`')
        resolve(results);
      } catch (err) {
        reject(new Error(err));
      }

    });
  });


  ipcMain.handle('add-usuario', async (event, usuario) => {
    return new Promise(async (resolve, reject) => {
      try {
        const [results, fields] = await connection.query('INSERT INTO `usuarios` (nombre, email) VALUES (?,?)', [usuario.nombre, usuario.email]);
        resolve(results);
      } catch (err) {
        reject(new Error(err));
      }
    })
  });


  ipcMain.handle('edit-usuario', async (event, id, usuario) => {
    return new Promise(async (resolve, reject) => {
      try {
        const [results, fields] = await connection.query('UPDATE `usuarios` SET nombre = ?, email = ? WHERE id = ?', [usuario.nombre, usuario.email, id]);
        resolve(results);
      } catch (err) {
        reject(new Error(err));
      }
    })
  })


  ipcMain.handle('delete-usuario', async (event, id) => {
    return new Promise(async (resolve, reject) => {
      try {
        const [results, fields] = await connection.query('DELETE FROM `usuarios` WHERE id = ?', [id]);
        resolve(results);
      } catch (err) {
        reject(new Error(err));
      }
    })
  })
})


// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit()
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.