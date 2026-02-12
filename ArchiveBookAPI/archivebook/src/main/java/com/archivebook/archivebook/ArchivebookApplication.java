package com.archivebook.archivebook;

import com.archivebook.archivebook.entities.Autor;
import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Editorial;
import com.archivebook.archivebook.entities.Favoritos;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.PorLeer;
import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.AutorRepository;
import com.archivebook.archivebook.repository.EditorialRepository;
import com.archivebook.archivebook.repository.FavoritosRepository;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.repository.PorLeerRepository;
import com.archivebook.archivebook.repository.PrestamoRepository;
import com.archivebook.archivebook.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ArchivebookApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ArchivebookApplication.class, args);

        AutorRepository autorRepository = context.getBean(AutorRepository.class);
        EditorialRepository editorialRepository = context.getBean(EditorialRepository.class);
        LibroRepository libroRepository = context.getBean(LibroRepository.class);
        UsuarioRepository usuarioRepository = context.getBean(UsuarioRepository.class);
        PrestamoRepository prestamoRepository = context.getBean(PrestamoRepository.class);
        FavoritosRepository favoritosRepository = context.getBean(FavoritosRepository.class);
        PorLeerRepository porLeerRepository = context.getBean(PorLeerRepository.class);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        // CONDICIÓN: Solo cargamos datos si no hay usuarios registrados
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Base de datos vacía. Cargando datos iniciales...");

            Usuario admin = usuarioRepository.save(new Usuario(null, "admin", encoder.encode("admin123"), "ADMIN"));
            Usuario juan_lector = usuarioRepository.save(new Usuario(null, "juan_lector", encoder.encode("pass123"), "USER"));
            Usuario maria_libros = usuarioRepository.save(new Usuario(null, "maria_libros", encoder.encode("pass123"), "USER"));

            Autor autor1 = autorRepository.save(new Autor(null, "Miguel", "de Cervantes", "Española"));
            Autor autor2 = autorRepository.save(new Autor(null, "Isaac", "Asimov", "Estadounidense"));
            Autor autor3 = autorRepository.save(new Autor(null, "J.K.", "Rowling", "Británica"));

            Editorial editorial1 = editorialRepository.save(new Editorial(null, "Planeta", "Calle A", "España", "www.planeta.es"));
            Editorial editorial2 = editorialRepository.save(new Editorial(null, "Minotauro", "Calle B", "España", "www.minotauro.com"));
            Editorial editorial3 = editorialRepository.save(new Editorial(null, "Pearson", "Calle C", "Reino Unido", "www.pearson.com"));

            Libro libro1 = new Libro(null, "Don Quijote de la Mancha", "978-84-1", 1605, "portada_quijote.jpg",
                    "Las aventuras de un caballero que pierde la cordura por leer libros de caballería.");
            Libro libro2 = new Libro(null, "Fundación", "978-84-2", 1951, "portada_fundacion.jpg",
                    "Un científico predice la caída de un imperio galáctico y crea un plan para salvar el conocimiento humano.");
            Libro libro3 = new Libro(null, "Yo, Robot", "978-84-3", 1950, "portada_robot.jpg",
                    "Una serie de relatos que exploran las interacciones entre humanos y robots bajo las tres leyes de la robótica.");
            Libro libro4 = new Libro(null, "Harry Potter y la piedra filosofal", "978-84-4", 1997, "portada_harry.jpg",
                    "Un niño huérfano descubre que es un mago y comienza su formación en una escuela de magia.");

            libro1.setAutor(autor1);
            libro2.setAutor(autor2);
            libro3.setAutor(autor2);
            libro4.setAutor(autor3);

            libro1.setEditorial(editorial1);
            libro2.setEditorial(editorial2);
            libro3.setEditorial(editorial2);
            libro4.setEditorial(editorial3);

            libro1.setCategoria(CategoriaLibro.FICCION);
            libro2.setCategoria(CategoriaLibro.CIENCIA);
            libro3.setCategoria(CategoriaLibro.CIENCIA);
            libro4.setCategoria(CategoriaLibro.FICCION);

            libro1.setBestSeller(true);
            libro4.setBestSeller(true);
            libro4.setPrestado(true);

            libroRepository.save(libro1);
            libroRepository.save(libro2);
            libroRepository.save(libro3);
            libroRepository.save(libro4);

            //favoritos
            Favoritos favoritosAdmin = new Favoritos();
            favoritosAdmin.setLibro(libro1);
            favoritosAdmin.setUsuario(admin);
            favoritosRepository.save(favoritosAdmin);

            Favoritos favoritosJuan = new Favoritos();
            favoritosJuan.setLibro(libro1);
            favoritosJuan.setUsuario(juan_lector);
            favoritosRepository.save(favoritosJuan);

            Favoritos favoritosMaria = new Favoritos();
            favoritosMaria.setLibro(libro1);
            favoritosMaria.setUsuario(maria_libros);
            favoritosRepository.save(favoritosMaria);

            PorLeer porLeer1 = new PorLeer();
            porLeer1.setLibro(libro2);
            porLeer1.setUsuario(juan_lector);
            porLeerRepository.save(porLeer1);
            
            PorLeer porLeer2 = new PorLeer();
            porLeer2.setLibro(libro2);
            porLeer2.setUsuario(maria_libros);
            porLeerRepository.save(porLeer2);

            Prestamo prestamoActivo = new Prestamo();
            prestamoActivo.setLibro(libro4);
            prestamoActivo.setUsuario(juan_lector);
            prestamoActivo.setFechaPrestamo(LocalDate.now().minusDays(3));
            prestamoActivo.setDevuelto(false);
            prestamoRepository.save(prestamoActivo);
            
            
            Prestamo prestamoActivo2 = new Prestamo();
            prestamoActivo2.setLibro(libro4);
            prestamoActivo2.setUsuario(maria_libros);
            prestamoActivo2.setFechaPrestamo(LocalDate.now().minusDays(3));
            prestamoActivo2.setDevuelto(false);
            prestamoRepository.save(prestamoActivo2);

            System.out.println(">>> Carga inicial completada con éxito.");
        } else {
            System.out.println(">>> La base de datos ya contiene informacion. Omitiendo carga inicial.");
        }

        // Verificación por consola (siempre se ejecuta)
        System.out.println("\n--- ESTADO ACTUAL DE LA BASE DE DATOS ---");
        System.out.println("Total de libros en BD: " + libroRepository.count());
        List<Libro> todosLosLibros = libroRepository.findAll();
        for (Libro l : todosLosLibros) {
            String autorNombre = (l.getAutor() != null) ? l.getAutor().getNombre() : "Sin autor";
            System.out.println("Libro: " + l.getTitulo() + " | Autor: " + autorNombre);
        }
        System.out.println("--------------------------------\n");
    }
}
