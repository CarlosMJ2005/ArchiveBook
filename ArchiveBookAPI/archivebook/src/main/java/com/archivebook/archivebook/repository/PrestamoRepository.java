package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
    
     List<Prestamo> findByUsuario(Usuario usuario);
     
     List<Prestamo> findByUsuarioAndDevueltoFalse(Usuario usuario);
     
     Optional<Prestamo> findByLibroIdLibroAndUsuarioIdUsuarioAndDevueltoFalse(Long idLibro, Long idUsuario);
}
