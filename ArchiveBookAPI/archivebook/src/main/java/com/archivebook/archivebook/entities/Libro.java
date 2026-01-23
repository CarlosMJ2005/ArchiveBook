package com.archivebook.archivebook.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLibro;
    
    @Column(name = "titulo")
    private String titulo;
    
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "agnoPublicacion")
    private int agnoPublicacion;
    
    @Column(name = "portadaLibro")
    private String portadaLibro;
    
    @ManyToOne
    @JoinColumn(name = "idAutor")
    private Autor autor; // Relación con Autor
    
    @ManyToOne
    @JoinColumn(name = "idEditorial")
    private Editorial editorial; // Relación con Editorial
    
    @Enumerated(EnumType.STRING)
    @Column(name = "idCategoria")
    private CategoriaLibro categoria;
    
    @Column(name="prestado")
    private boolean prestado = false;
    
    @Column(name="bestSeller")
    private boolean bestSeller = false;
    
    @Column(name="porLeer")
    private boolean porLeer = false;
    
    @Column(name="favorito")
    private boolean favorito = false;
    


    public Libro() {
    }

    public Libro(Long idLibro, String titulo, String isbn, int agnoPublicacion, String portadaLibro) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.agnoPublicacion = agnoPublicacion;
        this.portadaLibro = portadaLibro;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAgnoPublicacion() {
        return agnoPublicacion;
    }

    public void setAgnoPublicacion(int agnoPublicacion) {
        this.agnoPublicacion = agnoPublicacion;
    }

    public String getPortadaLibro() {
        return portadaLibro;
    }

    public void setPortadaLibro(String portadaLibro) {
        this.portadaLibro = portadaLibro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public CategoriaLibro getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaLibro categoria) {
        this.categoria = categoria;
    }

    public boolean isPrestado() {
        return prestado;
    }

    public void setPrestado(boolean prestado) {
        this.prestado = prestado;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public boolean isPorLeer() {
        return porLeer;
    }

    public void setPorLeer(boolean porLeer) {
        this.porLeer = porLeer;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

  
    
    }
    
    
    
    

