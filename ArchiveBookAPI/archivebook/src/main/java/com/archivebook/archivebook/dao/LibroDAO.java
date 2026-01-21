/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.archivebook.archivebook.dao;

import com.archivebook.archivebook.entities.Libro;
import java.util.List;


public interface LibroDAO {
    
    List<Libro> findAll();
    
    Libro findById(Long id);
    
    Libro save(Libro libro);
    
    void deleteById(Long id);
    
}
