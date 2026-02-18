package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    // Inyección por constructor coherente con LibroController
    public UsuarioController(UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        // 1. Validar si el usuario ya tiene ID (evitar actualizaciones vía POST)
        if (usuario.getIdUsuario() != null) {
            return ResponseEntity.badRequest().body("No se puede crear un usuario que ya tiene un ID.");
        }

        // 2. Comprobar si el correo electrónico ya existe en la base de datos
        if (repository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El correo electrónico ya está registrado.");
        }

        // 3. Cifrar la contraseña antes de guardar (Seguridad adicional)
        String passwordCifrada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(passwordCifrada);
        usuario.setRole("USER");

        // 4. Guardar el nuevo usuario
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

    @PostMapping("/api/usuarios/admin")
    public ResponseEntity<?> crearAdmin(@RequestBody Usuario usuario) {
        if (repository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya existe.");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setRole("ADMIN"); // Asignación forzada de ADMIN
        return ResponseEntity.ok(repository.save(usuario));
    }
}
