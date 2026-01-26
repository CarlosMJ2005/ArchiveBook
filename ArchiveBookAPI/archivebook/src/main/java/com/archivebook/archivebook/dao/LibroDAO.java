/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.archivebook.archivebook.dao;

import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Libro;
import java.util.List;


public interface LibroDAO {
        
    // Buscar por título (HQL normal)
    List<Libro> findByTitulo(String titulo);
    
    // Buscar por categoría (HQL normal)
    List<Libro> findByCategoria(CategoriaLibro categoria);
    
    // Libros favoritos que aún no se han leído
    List<Libro> findFavoritosPendientes();
    
    // Buscar libros por el nombre del autor (HQL con Join)
    List<Libro> findByAutorNombre(String nombre);
    
    //Buscar libros que esta prestado a un usuario
    List<Libro> findByPrestado(boolean prestado);
    
    //Buscar libros que estan disponibles ya que no los tiene ningun usuario
    List<Libro> findLibrosDisponibles();
    
    //Buscar libros que estan marcados como bestseller
    List<Libro> findByBestSeller(boolean bestSeller);
    
    //Buscar libros que estan marcados por leer 
    List<Libro> findByPorLeer(boolean porLeer);
    
    //Buscar libros que estan amrcados como favoritos
    List<Libro> findByFavorito(boolean favorito);
    
}
