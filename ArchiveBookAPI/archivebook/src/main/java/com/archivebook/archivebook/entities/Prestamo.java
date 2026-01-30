
package com.archivebook.archivebook.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo;
    
    @Column(name = "fechaPrestamo")
    private LocalDate fechaPrestamo;

    @Column(name = "fechaDevolucionPrevista")
    private LocalDate fechaDevolucionPrevista;

    @Column(name="fechaDevolucionReal")
    private LocalDate fechaDevolucionReal;

    @Column(nullable = false,name="devuelto")
    private boolean devuelto; 

    @ManyToOne
    @JoinColumn(name = "idLibro")
    private Libro libro;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    public Prestamo() {
        this.fechaPrestamo = LocalDate.now();
        this.devuelto = true;
    }

    public Prestamo(Long idPrestamo, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, boolean devuelto) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.devuelto = devuelto;
    }

    public Prestamo(Long idPrestamo, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, boolean devuelto, Libro libro, Usuario usuario) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.devuelto = devuelto;
        this.libro = libro;
        this.usuario = usuario;
    }

    public Prestamo(Long idPrestamo, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, boolean devuelto, Libro libro, Usuario usuario) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.devuelto = devuelto;
        this.libro = libro;
        this.usuario = usuario;
    }
    

    public Long getIdPrestamo() {
        return idPrestamo;
    }

    public void setId(Long idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
