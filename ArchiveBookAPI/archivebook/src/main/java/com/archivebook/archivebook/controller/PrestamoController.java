package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.dao.PrestamoDAO;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.repository.PrestamoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final PrestamoDAO prestamoDAO;
    private LibroRepository libroRepository;

    // Inyección por constructor coherente con tus otros controladores
    public PrestamoController(PrestamoRepository repository,
            PrestamoDAO prestamoDAO,
            LibroRepository libroRepository) {
        this.repository = repository;
        this.prestamoDAO = prestamoDAO;
        this.libroRepository = libroRepository;
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

    @PostMapping
    public ResponseEntity<?> crearPrestamo(@RequestBody Prestamo prestamo) {
        // 1. Validar que el préstamo traiga un libro asociado
        if (prestamo.getLibro() == null || prestamo.getLibro().getIdLibro() == null) {
            return ResponseEntity.badRequest().body("El préstamo debe estar vinculado a un libro válido.");
        }

        // 2. Buscar el libro en la base de datos para verificar su estado actual
        Optional<Libro> libroOpt = libroRepository.findById(prestamo.getLibro().getIdLibro());

        if (libroOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Libro libro = libroOpt.get();

        // 3. CONTROL DE SEGURIDAD: Verificar si el libro ya está prestado
        if (libro.isPrestado()) {
            return ResponseEntity.badRequest().body("Operación denegada: El libro '" + libro.getTitulo() + "' ya se encuentra prestado actualmente.");
        }

        // 4. Actualizar el estado del libro a prestado
        libro.setPrestado(true);
        libroRepository.save(libro); // Guardamos el cambio en la tabla de libros

        // 5. Guardar el nuevo préstamo
        Prestamo prestamoGuardado = repository.save(prestamo);
        return ResponseEntity.ok(prestamoGuardado);
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

    @GetMapping("/buscar/fecha/{fecha}")
    public List<Prestamo> buscarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return prestamoDAO.findByFechaPrestamo(fecha);
    }


    @PutMapping("/api/prestamos/{id}/devolver")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {
        // 1. Verificar si el préstamo existe
        Optional<Prestamo> prestamoOpt = repository.findById(id);
        if (prestamoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Prestamo prestamo = prestamoOpt.get();
        Libro libro = prestamo.getLibro();

        // 2. Cambiar el estado del libro a disponible
        if (libro != null) {
            libro.setPrestado(false);
            libroRepository.save(libro); // El libro vuelve a estar disponible para otros préstamos
        }

        // 3. Opcional: Actualizar datos del préstamo (ej. poner fecha de devolución real o cambiar estado)
        // prestamo.setEstado("Devuelto"); 
        repository.save(prestamo);

        return ResponseEntity.ok("El libro '" + libro.getTitulo() + "' ha sido devuelto correctamente.");
    }

}
