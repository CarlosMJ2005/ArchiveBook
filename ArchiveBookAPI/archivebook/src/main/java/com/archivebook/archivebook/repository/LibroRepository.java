package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long>{

    List<Libro> findByCategoria(CategoriaLibro categoria);
}
