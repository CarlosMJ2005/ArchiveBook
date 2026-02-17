package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsByCorreo(String correo);
    
}
