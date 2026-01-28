package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long>{
    
    
}
