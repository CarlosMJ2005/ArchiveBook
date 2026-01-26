package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    
}
