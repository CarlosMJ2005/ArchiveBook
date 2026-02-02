package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Favoritos;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long>{
    // Esto nos servirá para saber si un usuario ya tiene ese libro en favoritos
    Optional<Favoritos> findByUsuarioAndLibro(Usuario usuario, Libro libro);
    
    // Para listar todos los favoritos de un usuario concreto
    List<Favoritos> findByUsuario(Usuario usuario);
}
