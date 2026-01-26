package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.repository.LibroRepository;
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
public class LibroController {
    
    private LibroRepository repository;
    
    public LibroController(LibroRepository repository) {
        this.repository = repository;
    }
    
<<<<<<< HEAD
     @GetMapping("/api/libros")
=======
    @GetMapping("/api/libros")
>>>>>>> origin/main
    public List<Libro> verLibros() {
        return repository.findAll();
    }
    
    @GetMapping("/api/libros/{id}")
    public ResponseEntity<Libro> findById(@PathVariable Long id) {
        Optional<Libro> libroOpt = repository.findById(id);

        if (libroOpt.isPresent()) {
            return ResponseEntity.ok(libroOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/api/libros")
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        // Si el libro ya tiene ID, es una operación de modificación, no de creación
        if (libro.getIdLibro() != null) {
            return ResponseEntity.badRequest().build();
        }
        Libro libroSaved = repository.save(libro);
        return ResponseEntity.ok(libroSaved);
    }
    
    @PutMapping("/api/libros/{id}")
    public ResponseEntity<Libro> modificarLibro(@RequestBody Libro libro, @PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        libro.setIdLibro(id);
        Libro libroActualizado = repository.save(libro);
        return ResponseEntity.ok(libroActualizado);
    }
    
    @DeleteMapping("/api/libros/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    
    
}
