package com.archivebook.archivebook.controller;


import com.archivebook.archivebook.entities.PorLeer;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.repository.PorLeerRepository;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PorLeerController {
    
    private PorLeerRepository repository;
    private UsuarioRepository usuarioRepository;
    private LibroRepository libroRepository;

    public PorLeerController(PorLeerRepository repository, 
            UsuarioRepository usuarioRepository,
            LibroRepository libroRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }
    
    // LISTAR: Obtiene la lista "Por Leer" del usuario autenticado
    @GetMapping("api/porLeer")
    public ResponseEntity<List<PorLeer>> listarPorLeer() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(repository.findByUsuario(usuario)))
                .orElse(ResponseEntity.status(401).build());
    }
    
    // AÑADIR: Guarda un libro en la lista vinculándolo al usuario del token
   /*    @PostMapping("api/porLeer")
    public ResponseEntity<?> añadirAPorLeer(@RequestBody PorLeer porLeer) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> {
                    porLeer.setUsuario(usuario);
                    PorLeer guardado = repository.save(porLeer);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.status(401).build());
    } */
    
        @PostMapping("api/porLeer/{idLibro}")
    public ResponseEntity<?> añadirAPorLeer(@PathVariable Long idLibro) {
        // 1. Obtener el usuario del token
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // 2. Buscar al usuario logueado
        return usuarioRepository.findByCorreo(correo).map(usuario -> {

            // 3. Buscar el libro existente en la base de datos
            return libroRepository.findById(idLibro).map(libro -> {

                // 4. Crear la relación de favorito
                PorLeer porLeer = new PorLeer();
                porLeer.setUsuario(usuario);
                porLeer.setLibro(libro); // Asociamos el libro real encontrado

                return ResponseEntity.ok(repository.save(porLeer));

            }).orElse(ResponseEntity.notFound().build()); // Error 404 si el libro no existe

        }).orElse(ResponseEntity.status(401).build()); // Error 401 si el usuario no es válido
    }
    
    // ELIMINAR: Elimina el registro validando que pertenezca al usuario logueado
   /* @DeleteMapping("api/porLeer")
    public ResponseEntity<Void> eliminarPorLeer(@RequestBody PorLeer porLeerRequest) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo).map(usuario -> {
            // Se busca el registro por ID (asegúrate de que PorLeer tenga el getter getIdPorLeer)
            return repository.findById(porLeerRequest.getIdPorLeer())
                .filter(p -> p.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .map(p -> {
                    repository.delete(p);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.status(401).build());
    }*/
    
        // ELIMINAR: Se recibe el ID del libro por la URL para identificar qué favorito borrar
    @DeleteMapping("api/porLeer/{idLibro}")
    public ResponseEntity<Void> eliminarPorLeer(@PathVariable Long idLibro) {
        // 1. Obtener el usuario del token
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // 2. Buscar al usuario logueado
        return usuarioRepository.findByCorreo(correo).map(usuario -> {

            // 3. Buscar el libro para asegurar que existe
            return libroRepository.findById(idLibro).map(libro -> {

                // 4. Buscar el registro en la tabla 'favoritos' que coincida con este usuario y este libro
                // Usamos un método del repositorio para encontrar la relación específica
                return repository.findByUsuario(usuario).stream()
                        .filter(f -> f.getLibro().getIdLibro().equals(libro.getIdLibro()))
                        .findFirst()
                        .map(f -> {
                            repository.delete(f);
                            return ResponseEntity.noContent().<Void>build();
                        })
                        .orElse(ResponseEntity.notFound().build()); // No existe esa relación de favorito

            }).orElse(ResponseEntity.notFound().build()); // El libro no existe

        }).orElse(ResponseEntity.status(401).build());
    }
    
}
