package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Prestamo;
import java.util.List;


public interface PrestamoRepository {
    
    //Buscar prestamos de un usuario
    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, String estado);
    
    // Buscar si un libro específico está prestado actualmente
    List<Prestamo> findByLibroIdAndEstado(Long libroId, String estado);
}
