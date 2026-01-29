package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.dao.LibroDAO;
import com.archivebook.archivebook.entities.CategoriaLibro;
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
    private final LibroDAO libroDAO;

    public LibroController(LibroRepository repository, LibroDAO libroDAO) {
        this.repository = repository;
        this.libroDAO = libroDAO;
    }

    @GetMapping("/api/libros")
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
    public ResponseEntity<?> crearLibro(@RequestBody Libro libro) {
        // 1. Validar si ya tiene ID
        if (libro.getIdLibro() != null) {
            return ResponseEntity.badRequest().body("No se puede crear un libro que ya tiene ID.");
        }

        // 2. Comprobar si ya existe un libro con el mismo título (usando el DAO que ya tienes)
        // findByTitulo devuelve una lista, si no está vacía, el libro ya existe
        List<Libro> existentes = libroDAO.findByTitulo(libro.getTitulo());

        if (!existentes.isEmpty()) {
            return ResponseEntity.status(409)
                    .body("El libro con título '" + libro.getTitulo() + "' ya existe en la base de datos.");
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

    @GetMapping("/buscar/titulo/{titulo}")
    public List<Libro> findByTitulo(@PathVariable String titulo) {
        return libroDAO.findByTitulo(titulo);
    }

    @GetMapping("/buscar/categoria/{categoria}")
    public List<Libro> findByCategoria(@PathVariable CategoriaLibro categoria) {
        return libroDAO.findByCategoria(categoria);
    }

    @GetMapping("/buscar/autor/{nombre}")
    public List<Libro> findByAutorNombre(@PathVariable String nombre) {
        return libroDAO.findByAutorNombre(nombre);
    }

    @GetMapping("/estado/prestado/{prestado}")
    public List<Libro> findByPrestado(@PathVariable boolean prestado) {
        return libroDAO.findByPrestado(prestado);
    }

    @GetMapping("/disponibles")
    public List<Libro> findLibrosDisponibles() {
        return libroDAO.findLibrosDisponibles();
    }

    @GetMapping("/estado/porLeer/{porLeer}")
    public List<Libro> findByPorLeer(@PathVariable boolean porLeer) {
        return libroDAO.findByPorLeer(porLeer);
    }

    @GetMapping("/estado/bestseller/{isBestseller}")
    public List<Libro> findByBestSeller(@PathVariable boolean isBestseller) {
        return libroDAO.findByBestSeller(isBestseller);
    }

    @GetMapping("/estado/favorito/{favorito}")
    public List<Libro> findByFavorito(@PathVariable boolean favorito) {
        return libroDAO.findByFavorito(favorito);
    }

}
