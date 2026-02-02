package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.PorLeer;
import com.archivebook.archivebook.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PorLeerRepository extends JpaRepository<PorLeer, Long>{
    
    // Esto nos servirá para saber si un usuario ya tiene ese libro marcado porLeer
    Optional<PorLeer> findByUsuarioAndLibro(Usuario usuario, Libro libro);
    
    // Para listar todos los porLeer de un usuario concreto
    List<PorLeer> findByUsuario(Usuario usuario);
}
