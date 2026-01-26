package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
