
package com.archivebook.archivebook.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;


@Entity
@Table(name = "editoriales")
public class Editorial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEditorial;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "pais")
    private String pais;
    
    @Column(name = "sitioWeb")
    private String sitioWeb;
    
    @OneToMany(mappedBy = "editorial")
    //@JsonIgnore // Evita bucles infinitos al serializar a JSON
    private List<Libro> libros;

    public Editorial() {
    }

    public Editorial(Long idEditorial, String nombre, String direccion, String pais, String sitioWeb) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
        this.direccion = direccion;
        this.pais = pais;
        this.sitioWeb = sitioWeb;
    }

    
   
    public Long getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(Long idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
    
}