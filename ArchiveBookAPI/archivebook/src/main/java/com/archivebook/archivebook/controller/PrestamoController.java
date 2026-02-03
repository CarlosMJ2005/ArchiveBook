package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.dao.PrestamoDAO;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.repository.PrestamoRepository;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    private UsuarioRepository usuarioRepository;
    

    // Inyección por constructor coherente con tus otros controladores
    public PrestamoController(PrestamoRepository repository,
            PrestamoDAO prestamoDAO,
            LibroRepository libroRepository,
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.prestamoDAO = prestamoDAO;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los préstamos
   /* @GetMapping("/api/prestamos")
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

@PostMapping("/api/prestamos")
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
        libroRepository.save(libro); 

        // --- CAMBIO AQUÍ: Inicializar el estado del préstamo como NO devuelto ---
        prestamo.setDevuelto(false); 

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
        
        // --- CAMBIO AQUÍ: Verificar si ya estaba devuelto usando el booleano ---
        if (prestamo.getDevuelto()) {
            return ResponseEntity.badRequest().body("Este libro ya ha sido devuelto anteriormente.");
        }

        Libro libro = prestamo.getLibro();

        // 2. Cambiar el estado del libro a disponible
        if (libro != null) {
            libro.setPrestado(false);
            libroRepository.save(libro); 
        }

        // --- CAMBIO AQUÍ: Usar booleano en lugar de String "DEVUELTO" ---
        prestamo.setDevuelto(true); 
        prestamo.setFechaDevolucionReal(LocalDate.now());
        repository.save(prestamo);

        return ResponseEntity.ok("El libro '" + (libro != null ? libro.getTitulo() : "desconocido") + "' ha sido devuelto correctamente.");
    }*/
    //AQUI EMPIEZA
    // LISTAR: Solo los préstamos del usuario logueado
    /*@GetMapping("/api/prestamos")
    public ResponseEntity<List<Prestamo>> verPrestamos() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(repository.findByUsuario(usuario)))
                .orElse(ResponseEntity.status(401).build());
    }*/

    @PostMapping("/api/prestamos")
    public ResponseEntity<?> crearPrestamo(@RequestBody Prestamo prestamo) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) return ResponseEntity.status(401).build();

        if (prestamo.getLibro() == null || prestamo.getLibro().getIdLibro() == null) {
            return ResponseEntity.badRequest().body("El préstamo debe estar vinculado a un libro válido.");
        }

        Optional<Libro> libroOpt = libroRepository.findById(prestamo.getLibro().getIdLibro());
        if (libroOpt.isEmpty()) return ResponseEntity.notFound().build();

        Libro libro = libroOpt.get();
        if (libro.isPrestado()) {
            return ResponseEntity.badRequest().body("El libro '" + libro.getTitulo() + "' ya está prestado.");
        }

        // Configuración automática del préstamo
        libro.setPrestado(true);
        libroRepository.save(libro);

        prestamo.setUsuario(usuarioOpt.get()); // Asignamos el usuario del token
        prestamo.setDevuelto(false);
        return ResponseEntity.ok(repository.save(prestamo));
    }

    @PutMapping("/api/prestamos/{id}/devolver")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        Optional<Prestamo> prestamoOpt = repository.findById(id);
        if (prestamoOpt.isEmpty()) return ResponseEntity.notFound().build();

        Prestamo prestamo = prestamoOpt.get();

        // SEGURIDAD: Verificar que el préstamo pertenece al usuario del token
        if (!prestamo.getUsuario().getCorreo().equals(correo)) {
            return ResponseEntity.status(403).body("No tienes permiso para devolver un libro que no prestaste tú.");
        }

        if (prestamo.getDevuelto()) {
            return ResponseEntity.badRequest().body("Este libro ya ha sido devuelto.");
        }

        Libro libro = prestamo.getLibro();
        if (libro != null) {
            libro.setPrestado(false);
            libroRepository.save(libro);
        }

        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        repository.save(prestamo);

        return ResponseEntity.ok("Devolución completada.");
    }

    @DeleteMapping("/api/prestamos")
    public ResponseEntity<Void> eliminarPrestamo(@RequestBody Prestamo prestamoRequest) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo).map(usuario -> {
            return repository.findById(prestamoRequest.getIdPrestamo())
                .filter(p -> p.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .map(p -> {
                    repository.delete(p);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.status(401).build());
    }
}
