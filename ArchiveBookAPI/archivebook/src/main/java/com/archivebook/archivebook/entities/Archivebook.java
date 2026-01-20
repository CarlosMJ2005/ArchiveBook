package com.archivebook.archivebook.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "biblioteca")
public class Archivebook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBiblioteca;
    
    @Column(name = "idLibro")
    private int idLibro;
    
    @Column(name = "fechaPrestamo")
    private Date fechaPrestamo;
    
    @Column(name = "fechaDevolucionPrevista")
    private Date fechaDevolucionPrevista;
    
    @Column(name = "fechaDevolucion")
    private Date fechaDevolución;
    
    @Column(name = "Estado del libro esto mejor ponlo en libros")
    private String estado; // Ejemplo: "Disponible", "Prestado", "Reservado"
    
    @Column (name = "Usuario")
    private String usuarioNombre; // Que usuario tiene el libro
    
}
