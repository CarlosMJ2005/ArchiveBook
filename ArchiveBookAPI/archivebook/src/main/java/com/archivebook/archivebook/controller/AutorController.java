
package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Autor;
import com.archivebook.archivebook.repository.AutorRepository;
import java.util.List;
import java.util.Optional;
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
public class AutorController {
    
    
    private AutorRepository repository;

    
    public AutorController(AutorRepository repository) {
        this.repository = repository;

    }
    
    @GetMapping("/api/autores")
    public List<Autor> verAutores() {
        return repository.findAll();
    }
    
    @GetMapping("/api/autores/{id}")
    public ResponseEntity<Autor> findById(@PathVariable Long id) {
        Optional<Autor> autorOpt = repository.findById(id);

        if (autorOpt.isPresent()) {
            return ResponseEntity.ok(autorOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear un nuevo autor
    @PostMapping("/api/autores")
    public ResponseEntity<Autor> crearAutor(@RequestBody Autor autor) {
        // Si el autor ya tiene ID, es una modificación, no una creación
        if (autor.getIdAutor() != null) {
            return ResponseEntity.badRequest().build();
        }
        Autor autorSaved = repository.save(autor);
        return ResponseEntity.ok(autorSaved);
    }
    
    //Modificar autor existente
    @PutMapping("/api/autores/{id}")
    public ResponseEntity<Autor> modificarAutor(@RequestBody Autor autor, @PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        autor.setIdAutor(id);
        Autor autorActualizado = repository.save(autor);
        return ResponseEntity.ok(autorActualizado);
    }
    
    // Eliminar un autor
    @DeleteMapping("/api/autores/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
