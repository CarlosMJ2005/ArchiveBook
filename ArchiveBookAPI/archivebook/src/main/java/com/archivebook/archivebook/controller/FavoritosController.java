package com.archivebook.archivebook.controller;

import com.archivebook.archivebook.entities.Favoritos;
import com.archivebook.archivebook.repository.FavoritosRepository;
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

    public FavoritosController(FavoritosRepository repository,
            UsuarioRepository usuariorepository) {
        this.repository = repository;
        this.usuarioRepository = usuariorepository;
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
    
    
    // AÑADIR: Asocia automáticamente el favorito al usuario logueado
    @PostMapping("api/favoritos")
    public ResponseEntity<?> añadirAFavoritos(@RequestBody Favoritos favorito) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> {
                    favorito.setUsuario(usuario);
                    Favoritos guardado = repository.save(favorito);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    
    // ELIMINAR: Sin ID en la URL. Se busca la relación usando el usuario del token y el objeto enviado.
    @DeleteMapping("api/favoritos")
    public ResponseEntity<Void> eliminarFavorito(@RequestBody Favoritos favoritoRequest) {
        // 1. Obtener el usuario del token
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String correo = (String) jwt.getSubject();

        // 2. Buscar al usuario logueado
        return usuarioRepository.findByCorreo(correo).map(usuario -> {
            // 3. Buscar el registro de favorito que coincida con este usuario y el ID enviado en el body
            // Esto asegura que el usuario SOLO pueda borrar sus propios favoritos
            return repository.findById(favoritoRequest.getIdFavorito())
                .filter(f -> f.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .map(f -> {
                    repository.delete(f);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
        })
                .orElse(ResponseEntity.status(401).build());
    }
        
    
}
