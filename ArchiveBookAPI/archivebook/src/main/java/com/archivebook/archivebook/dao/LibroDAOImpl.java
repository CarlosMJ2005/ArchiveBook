package com.archivebook.archivebook.dao;

import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Libro;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class LibroDAOImpl implements LibroDAO {

    private Session session;

    public LibroDAOImpl(Session session) {
        this.session = session;
    }

    @Override
    public List<Libro> findByTitulo(String titulo) {
        // HQL con parámetro para evitar inyecciones y buscar coincidencias parciales
        Query<Libro> query = session.createQuery("from Libro where titulo like :t", Libro.class);
        query.setParameter("t", "%" + titulo + "%");
        return query.list();
    }

    @Override
    public List<Libro> findByCategoria(CategoriaLibro categoria) {
        // Filtro directo por el enumerado de categoría
        Query<Libro> query = session.createQuery("from Libro where categoria = :cat", Libro.class);
        query.setParameter("cat", categoria);
        return query.list();
    }

    //Buscamos libros favoritos que a su vez esten marcados por leer
    @Override
    public List<Libro> findFavoritosPendientes() {
        // Consulta con varios filtros booleanos (útil para tu lógica de "por leer")
        return session.createQuery("from Libro where favorito = true and porLeer = true", Libro.class).list();
    }

    @Override
    public List<Libro> findByAutorNombre(String nombre) {
        // HQL permite hacer Joins de forma muy natural usando los atributos de la clase
        String hql = "select l from Libro l join l.autor a where a.nombre like :n";
        Query<Libro> query = session.createQuery(hql, Libro.class);
        query.setParameter("n", "%" + nombre + "%");
        return query.list();
    }

    @Override
    public List<Libro> findByPrestado(boolean prestado) {
        Query<Libro> query = session.createQuery("from Libro where prestado = :p", Libro.class);
        query.setParameter("p", prestado);
        return query.list();
    }

    @Override
    public List<Libro> findLibrosDisponibles() {
        return session.createQuery("from Libro where prestado = false", Libro.class).list();
    }

    @Override
    public List<Libro> findByBestSeller(boolean bestSeller) {
        Query<Libro> query = session.createQuery("from Libro where bestSeller = :best", Libro.class);
        query.setParameter("best", bestSeller);
        return query.list();
    }

    @Override
    public List<Libro> findByPorLeer(boolean porLeer) {
        Query<Libro> query = session.createQuery("from Libro where porLeer = :leer", Libro.class);
        query.setParameter("leer", porLeer);
        return query.list();
    }

    @Override
    public List<Libro> findByFavorito(boolean favorito) {
        Query<Libro> query = session.createQuery("from Libro where favorito = :fav", Libro.class);
        query.setParameter("fav", favorito);
        return query.list();
    }
}
