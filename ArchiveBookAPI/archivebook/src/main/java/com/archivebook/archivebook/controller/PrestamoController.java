
package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.repository.PrestamoRepository;
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
public class PrestamoController {
    private PrestamoRepository repository;
    
    // Inyección por constructor coherente con tus otros controladores
    public PrestamoController(PrestamoRepository repository) {
        this.repository = repository;
    }
    
    // Listar todos los préstamos
    @GetMapping("/api/prestamos")
    public List<Prestamo> verPrestamos() {
        return repository.findAll();
    }
    
    // Buscar un préstamo específico por su ID
    @GetMapping("/api/prestamos/{id}")
    public ResponseEntity<Prestamo> findById(@PathVariable Long id) {
        Optional<Prestamo> prestamoOpt = repository.findById(id);

        if (prestamoOpt.isPresent()) {
            return ResponseEntity.ok(prestamoOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Registrar un nuevo préstamo
    @PostMapping("/api/prestamos")
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        // Verificamos que no tenga ID para asegurar que es una creación
        if (prestamo.getIdPrestamo() != null) {
            return ResponseEntity.badRequest().build();
        }
        Prestamo prestamoSaved = repository.save(prestamo);
        return ResponseEntity.ok(prestamoSaved);
    }
    
    // Actualizar un préstamo (ej. para registrar una devolución)
    @PutMapping("/api/prestamos/{id}")
    public ResponseEntity<Prestamo> modificarPrestamo(@RequestBody Prestamo prestamo, @PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        prestamo.setId(id); // Usamos el setter de ID definido en tu entidad Prestamo
        Prestamo prestamoActualizado = repository.save(prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }
    
    // Eliminar el registro de un préstamo
    @DeleteMapping("/api/prestamos/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
