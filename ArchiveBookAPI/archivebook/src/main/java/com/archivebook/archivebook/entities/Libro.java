package com.archivebook.archivebook.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLibro;
    
    @Column(name = "titulo")
    private String titulo;
    
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "idAutor")
    private int idAutor;   // Relación con Autor
    
    @Column(name = "idEditorial")
    private int idEditorial; // Relación con Editorial
    
    @Column(name = "CategoriaLibro")
    private String genero;
    private String portadaUrl;
    
}
