package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    private UsuarioRepository repository;
    
    // Inyección por constructor coherente con LibroController
    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }
    
    // Obtener todos los usuarios
    @GetMapping("/api/usuarios")
    public List<Usuario> verUsuarios() {
        return repository.findAll();
    }
    
    // Obtener un usuario por ID
    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = repository.findById(id);

        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(usuarioOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear un nuevo usuario (Registro)
    @PostMapping("/api/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // Si ya tiene ID, rechazamos la petición por ser creación
        if (usuario.getIdUsuario() != null) {
            return ResponseEntity.badRequest().build();
        }
        Usuario usuarioSaved = repository.save(usuario);
        return ResponseEntity.ok(usuarioSaved);
    }
    
    // Modificar datos de un usuario
    @PutMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@RequestBody Usuario usuario, @PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        usuario.setIdUsuario(id);
        Usuario usuarioActualizado = repository.save(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }
    
    // Eliminar un usuario
    @DeleteMapping("/api/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
