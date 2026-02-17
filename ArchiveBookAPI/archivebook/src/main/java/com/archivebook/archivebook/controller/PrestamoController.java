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
import org.springframework.web.bind.annotation.PatchMapping;
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

    // LISTAR: Solo los préstamos del usuario logueado
    @GetMapping("/api/prestamos")
    public ResponseEntity<List<Prestamo>> verPrestamos() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(repository.findByUsuario(usuario)))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/api/prestamos/pendientes")
    public ResponseEntity<List<Prestamo>> verPrestamosPendientes() {
        // 1. Extraer el correo del token JWT
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // 2. Buscar al usuario y filtrar sus préstamos no devueltos
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(repository.findByUsuarioAndDevueltoFalse(usuario)))
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/api/prestamos")
    public ResponseEntity<?> crearPrestamo(@RequestBody Prestamo prestamo) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        if (prestamo.getLibro() == null || prestamo.getLibro().getIdLibro() == null) {
            return ResponseEntity.badRequest().body("El préstamo debe estar vinculado a un libro válido.");
        }

        Optional<Libro> libroOpt = libroRepository.findById(prestamo.getLibro().getIdLibro());
        if (libroOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

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

    @PatchMapping("/api/prestamos/devolver-libro/{idLibro}")
    public ResponseEntity<?> devolverPorLibro(@PathVariable Long idLibro) {
        // 1. Obtener información del usuario autenticado desde el Token
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        Usuario usuario = usuarioOpt.get();

        // 2. Buscar el préstamo activo para este libro y este usuario
        // Usamos 'devuelto = false' para asegurar que cerramos el préstamo correcto
        Optional<Prestamo> prestamoOpt = repository.findByLibroIdLibroAndUsuarioIdUsuarioAndDevueltoFalse(idLibro, usuario.getIdUsuario());

        if (prestamoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No se encontró un préstamo activo de este usuario para el libro con ID: " + idLibro);
        }

        Prestamo prestamo = prestamoOpt.get();
        Libro libro = prestamo.getLibro();

        // 3. PATCH: Cambiar el estado del libro a disponible
        if (libro != null) {
            libro.setPrestado(false);
            libroRepository.save(libro);
        }

        // 4. PATCH: Cerrar el préstamo
        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        repository.save(prestamo);

        return ResponseEntity.ok("Libro '" + libro.getTitulo() + "' devuelto con éxito.");
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
