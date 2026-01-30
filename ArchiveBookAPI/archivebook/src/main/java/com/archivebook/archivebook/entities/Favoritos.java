package com.archivebook.archivebook.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "favoritos")
public class Favoritos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavorito;
    
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idLibro")
    private Libro libro;

    public Favoritos() {
    }

    public Favoritos(Long idFavorito, Usuario usuario, Libro libro) {
        this.idFavorito = idFavorito;
        this.usuario = usuario;
        this.libro = libro;
    }

    public Long getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(Long idFavorito) {
        this.idFavorito = idFavorito;
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
