package com.archivebook.archivebook.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


        @Configuration
       public class JpaConfig {
           @Autowired
           private EntityManagerFactory entityManagerFactory; // es un objeto de JPA, equivalente al SessionFactory de Hibernate
           
           @Bean
           public Session getSession() {
               System.out.println("Creando bean session");
               // obtenemos el objeto SessionFactory
               SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
               return sessionFactory.openSession();
           }
       } 
