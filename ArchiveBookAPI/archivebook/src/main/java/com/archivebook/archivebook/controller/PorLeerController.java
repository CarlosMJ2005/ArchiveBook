package com.archivebook.archivebook.controller;


import com.archivebook.archivebook.entities.PorLeer;
import com.archivebook.archivebook.entities.Usuario;
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
    private final UsuarioRepository usuarioRepository;

    public PorLeerController(PorLeerRepository repository, 
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }
    
     // LISTAR: Ver los porLeer de un usuario
   /* @GetMapping("api/porLeer/usuario/{idUsuario}")
    public List<PorLeer> listarPorLeer(@PathVariable Long idUsuario) {
        Usuario usuario = new Usuario(); 
        usuario.setIdUsuario(idUsuario);
        return repository.findByUsuario(usuario);
    }*/

    // AÑADIR: Marcar un libro porLeer
    /*@PostMapping("api/porLeer")
    public ResponseEntity<?> añadirAPorLeer(@RequestBody PorLeer porLeer) {
        // Guardamos el objeto que relaciona Usuario y Libro
        PorLeer guardado = repository.save(porLeer);
        return ResponseEntity.ok(guardado);
    }*/

    // ELIMINAR: Desmarcar porLeer por el ID de la relación
    /*@DeleteMapping("api/porLeer/{id}")
    public ResponseEntity<Void> eliminarPorLeer(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }*/
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
       @PostMapping("api/porLeer")
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
    } 
    
    // ELIMINAR: Elimina el registro validando que pertenezca al usuario logueado
    @DeleteMapping("api/porLeer")
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
    }
    
}
