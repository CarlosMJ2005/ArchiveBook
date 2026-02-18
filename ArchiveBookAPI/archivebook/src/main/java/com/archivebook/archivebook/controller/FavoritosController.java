package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Favoritos;
import com.archivebook.archivebook.repository.FavoritosRepository;
import com.archivebook.archivebook.repository.LibroRepository;
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
public class FavoritosController {

    private FavoritosRepository repository;
    private UsuarioRepository usuarioRepository;
    private LibroRepository libroRepository;

    public FavoritosController(FavoritosRepository repository,
            UsuarioRepository usuariorepository,
            LibroRepository libroRepository) {
        this.repository = repository;
        this.usuarioRepository = usuariorepository;
        this.libroRepository = libroRepository;
    }

    // LISTAR: Obtiene el usuario desde el SecurityContextHolder
    @GetMapping("api/favoritos")
    public ResponseEntity<List<Favoritos>> listarFavoritos() {
        // Lógica solicitada para obtener el username del JWT
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // Buscamos al usuario por su username/email para filtrar sus favoritos
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> ResponseEntity.ok(repository.findByUsuario(usuario)))
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("api/favoritos/{idLibro}")
    public ResponseEntity<?> añadirAFavoritos(@PathVariable Long idLibro) {
        // 1. Obtener el usuario del token
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // 2. Buscar al usuario logueado
        return usuarioRepository.findByCorreo(correo).map(usuario -> {

            // 3. Buscar el libro existente en la base de datos
            return libroRepository.findById(idLibro).map(libro -> {

                // 4. Crear la relación de favorito
                Favoritos favorito = new Favoritos();
                favorito.setUsuario(usuario);
                favorito.setLibro(libro); // Asociamos el libro real encontrado

                return ResponseEntity.ok(repository.save(favorito));

            }).orElse(ResponseEntity.notFound().build()); // Error 404 si el libro no existe

        }).orElse(ResponseEntity.status(401).build()); // Error 401 si el usuario no es válido
    }

    // ELIMINAR: Se recibe el ID del libro por la URL para identificar qué favorito borrar
    @DeleteMapping("api/favoritos/{idLibro}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long idLibro) {
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
