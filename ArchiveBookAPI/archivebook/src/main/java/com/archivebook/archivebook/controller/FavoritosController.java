package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Favoritos;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.FavoritosRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoritosController {
    
        private FavoritosRepository repository;

    public FavoritosController(FavoritosRepository repository) {
        this.repository = repository;
    }
      
    // LISTAR: Ver los favoritos de un usuario
    @GetMapping("api/favoritos/usuario/{idUsuario}")
    public List<Favoritos> listarFavoritos(@PathVariable Long idUsuario) {
        Usuario usuario = new Usuario(); 
        usuario.setIdUsuario(idUsuario);
        return repository.findByUsuario(usuario);
    }

    // AÑADIR: Guardar un libro como favorito
    @PostMapping("api/favoritos")
    public ResponseEntity<?> añadirAFavoritos(@RequestBody Favoritos favorito) {
        // Guardamos el objeto que relaciona Usuario y Libro
        Favoritos guardado = repository.save(favorito);
        return ResponseEntity.ok(guardado);
    }

    // ELIMINAR: Quitar de favoritos por el ID de la relación
    @DeleteMapping("api/favoritos/{id}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
        
    
}
