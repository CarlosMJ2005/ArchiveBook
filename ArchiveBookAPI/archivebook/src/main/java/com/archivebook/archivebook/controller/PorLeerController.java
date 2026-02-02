package com.archivebook.archivebook.controller;


import com.archivebook.archivebook.entities.PorLeer;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.PorLeerRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PorLeerController {
    
    private PorLeerRepository repository;

    public PorLeerController(PorLeerRepository repository) {
        this.repository = repository;
    }
    
     // LISTAR: Ver los porLeer de un usuario
    @GetMapping("api/porLeer/usuario/{idUsuario}")
    public List<PorLeer> listarPorLeer(@PathVariable Long idUsuario) {
        Usuario usuario = new Usuario(); 
        usuario.setIdUsuario(idUsuario);
        return repository.findByUsuario(usuario);
    }

    // AÑADIR: Marcar un libro porLeer
    @PostMapping("api/porLeer")
    public ResponseEntity<?> añadirAPorLeer(@RequestBody PorLeer porLeer) {
        // Guardamos el objeto que relaciona Usuario y Libro
        PorLeer guardado = repository.save(porLeer);
        return ResponseEntity.ok(guardado);
    }

    // ELIMINAR: Desmarcar porLeer por el ID de la relación
    @DeleteMapping("api/porLeer/{id}")
    public ResponseEntity<Void> eliminarPorLeer(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
        
    
}
