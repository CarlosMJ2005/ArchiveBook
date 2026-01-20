package com.archivebook.archivebook.entities;


public class Libro {
    
    private int idLibro;
    private String titulo;
    private String isbn;
    private int idAutor;     // Relación con Autor
    private int idEditorial; // Relación con Editorial
    private String genero;
    private String portadaUrl;
    
}
