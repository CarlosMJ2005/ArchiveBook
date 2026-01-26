package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Editorial;
import com.archivebook.archivebook.repository.EditorialRepository;
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
public class EditorialController {
    private EditorialRepository repository;
    
    // Inyección por constructor
    public EditorialController(EditorialRepository repository) {
        this.repository = repository;
    }
    
    // Obtener todas las editoriales
    @GetMapping("/api/editoriales")
    public List<Editorial> verEditoriales() {
        return repository.findAll();
    }
    
    // Obtener una editorial por ID
    @GetMapping("/api/editoriales/{id}")
    public ResponseEntity<Editorial> findById(@PathVariable Long id) {
        Optional<Editorial> editorialOpt = repository.findById(id);

        if (editorialOpt.isPresent()) {
            return ResponseEntity.ok(editorialOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Crear una nueva editorial
    @PostMapping("/api/editoriales")
    public ResponseEntity<Editorial> crearEditorial(@RequestBody Editorial editorial) {
        // Evitamos crear si ya viene con un ID asignado
        if (editorial.getIdEditorial() != null) {
            return ResponseEntity.badRequest().build();
        }
        Editorial editorialSaved = repository.save(editorial);
        return ResponseEntity.ok(editorialSaved);
    }
    
    // Modificar una editorial existente
    @PutMapping("/api/editoriales/{id}")
    public ResponseEntity<Editorial> modificarEditorial(@RequestBody Editorial editorial, @PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        editorial.setIdEditorial(id);
        Editorial editorialActualizada = repository.save(editorial);
        return ResponseEntity.ok(editorialActualizada);
    }
    
    // Eliminar una editorial
    @DeleteMapping("/api/editoriales/{id}")
    public ResponseEntity<Void> eliminarEditorial(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
