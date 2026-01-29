package com.archivebook.archivebook.dao;

import com.archivebook.archivebook.entities.Prestamo;
import java.time.LocalDate;
import java.util.List;

public interface PrestamoDAO {
    
        List<Prestamo> findByFechaPrestamo( LocalDate fechaPrestamo);
        
}
