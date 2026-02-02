package com.archivebook.archivebook.dao;

import com.archivebook.archivebook.entities.Prestamo;


import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PrestamoDAOImpl implements PrestamoDAO{
    
    private Session session;
    
        public PrestamoDAOImpl(Session session) {
        this.session = session;
    }
    
    @Override
    public List<Prestamo> findByFechaPrestamo(LocalDate fecha) {
        // HQL con parámetro para evitar inyecciones y buscar coincidencias parciales
        Query<Prestamo> query = session.createQuery("from Prestamo where fechaPrestamo = :fecha", Prestamo.class);
        query.setParameter("fecha", fecha);
        return query.list();
    }
}
