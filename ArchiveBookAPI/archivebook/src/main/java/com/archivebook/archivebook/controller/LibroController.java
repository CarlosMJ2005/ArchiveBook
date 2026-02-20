package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.dao.LibroDAO;
import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.repository.AutorRepository;
import com.archivebook.archivebook.repository.EditorialRepository;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.service.FileSystemStorageService;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class LibroController {

    private LibroRepository repository;
    private final LibroDAO libroDAO;
    private final FileSystemStorageService storageService;
    private AutorRepository autorRepository;
    private EditorialRepository editorialRepository;

    public LibroController(LibroRepository repository, LibroDAO libroDAO,
            FileSystemStorageService storageService,
            AutorRepository autorRepository,
            EditorialRepository editorialRepository) {
        this.repository = repository;
        this.libroDAO = libroDAO;
        this.storageService = storageService;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
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

    // 1) Endpoint para subir la imagen
    @PostMapping("/api/libros/{id}/upload")
    public ResponseEntity<?> subirPortada(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Libro> libroOpt = repository.findById(id);
        if (libroOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Guardar el archivo físicamente
        String nombreFichero = storageService.store(file);

        // Asociar el nombre del fichero a la entidad en la BDD
        Libro libro = libroOpt.get();
        libro.setPortadaLibro(nombreFichero);
        repository.save(libro);

        return ResponseEntity.ok().body("Imagen subida correctamente: " + nombreFichero);
    }

// 3) Endpoint para servir la imagen
    @GetMapping("/api/libros/{id}/portada")
    public ResponseEntity<org.springframework.core.io.Resource> cargarPortada(@PathVariable Long id) {
         Libro libro = repository.findById(id)
            .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Israel, no apareece la id"
            ));
    
        // Si no hay foto asignada, devolvemos 404
        if (libro.getPortadaLibro() == null) {
            return ResponseEntity.notFound().build();
        }

        Path file = storageService.load(libro.getPortadaLibro());

        try {
            // Esta línea es la que genera el error MalformedURLException
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (java.net.MalformedURLException e) {
            // Capturamos el error que detiene la compilación
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/libros/{idLibro}/autor/{idAutor}")
    public ResponseEntity<?> asignarAutor(@PathVariable Long idLibro, @PathVariable Long idAutor) {
        return repository.findById(idLibro).map(libro -> {
            return autorRepository.findById(idAutor).map(autor -> {
                libro.setAutor(autor);
                repository.save(libro);
                return ResponseEntity.ok("Autor '" + autor.getNombre() + "' asociado al libro '" + libro.getTitulo() + "'");
            }).orElse(ResponseEntity.status(404).body("Autor no encontrado con ID: " + idAutor));
        }).orElse(ResponseEntity.status(404).body("Libro no encontrado con ID: " + idLibro));
    }

    @PutMapping("/api/libros/{idLibro}/editorial/{idEditorial}")
    public ResponseEntity<?> asignarEditorial(@PathVariable Long idLibro, @PathVariable Long idEditorial) {
        return repository.findById(idLibro).map(libro -> {
            return editorialRepository.findById(idEditorial).map(editorial -> {
                libro.setEditorial(editorial);
                repository.save(libro);
                return ResponseEntity.ok("Editorial '" + editorial.getNombre() + "' asociada al libro '" + libro.getTitulo() + "'");
            }).orElse(ResponseEntity.status(404).body("Editorial no encontrada con ID: " + idEditorial));
        }).orElse(ResponseEntity.status(404).body("Libro no encontrado con ID: " + idLibro));
    }

    @GetMapping("/api/libros/buscar/titulo/{titulo}")
    public List<Libro> findByTitulo(@PathVariable String titulo) {
        return libroDAO.findByTitulo(titulo);
    }

    @GetMapping("/api/libros/categoria/{categoria}")
    public ResponseEntity<List<Libro>> buscarPorCategoria(@PathVariable CategoriaLibro categoria) {
        List<Libro> libros = repository.findByCategoria(categoria);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/api/libros/buscar/autor/{nombre}")
    public List<Libro> findByAutorNombre(@PathVariable String nombre) {
        return libroDAO.findByAutorNombre(nombre);
    }

    @GetMapping("/api/libros/estado/prestado/{prestado}")
    public List<Libro> findByPrestado(@PathVariable boolean prestado) {
        return libroDAO.findByPrestado(prestado);
    }

    @GetMapping("/api/libros/buscar/disponibles")
    public List<Libro> findLibrosDisponibles() {
        return libroDAO.findLibrosDisponibles();
    }

    @GetMapping("/api/libros/estado/porLeer/{porLeer}")
    public List<Libro> findByPorLeer(@PathVariable boolean porLeer) {
        return libroDAO.findByPorLeer(porLeer);
    }

    @GetMapping("/api/libros/estado/bestseller/{isBestseller}")
    public List<Libro> findByBestSeller(@PathVariable boolean isBestseller) {
        return libroDAO.findByBestSeller(isBestseller);
    }

    @GetMapping("/api/libros/estado/favorito/{favorito}")
    public List<Libro> findByFavorito(@PathVariable boolean favorito) {
        return libroDAO.findByFavorito(favorito);
    }
}
