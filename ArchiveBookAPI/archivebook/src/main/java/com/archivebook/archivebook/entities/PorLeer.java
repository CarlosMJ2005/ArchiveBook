package com.archivebook.archivebook.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "porLeer")
public class PorLeer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPorLeer;
    
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idLibro")
    private Libro libro;

    public PorLeer() {
    }

    public PorLeer(Long idPorLeer, Usuario usuario, Libro libro) {
        this.idPorLeer = idPorLeer;
        this.usuario = usuario;
        this.libro = libro;
    }

    public Long getIdPorLeer() {
        return idPorLeer;
    }

    public void setPorLeer(Long idPorLeer) {
        this.idPorLeer = idPorLeer;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
}
