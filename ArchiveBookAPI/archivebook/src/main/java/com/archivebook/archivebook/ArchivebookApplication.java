package com.archivebook.archivebook;

import com.archivebook.archivebook.entities.Autor;
import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Editorial;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.Prestamo;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.AutorRepository;
import com.archivebook.archivebook.repository.EditorialRepository;
import com.archivebook.archivebook.repository.LibroRepository;
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
        // Levantamos el contexto de la aplicación
        ApplicationContext context = SpringApplication.run(ArchivebookApplication.class, args);

        // Obtenemos los beans de los repositorios
        AutorRepository autorRepository = context.getBean(AutorRepository.class);
        EditorialRepository editorialRepository = context.getBean(EditorialRepository.class);
        LibroRepository libroRepository = context.getBean(LibroRepository.class);
        UsuarioRepository usuarioRepository = context.getBean(UsuarioRepository.class);
        PrestamoRepository prestamoRepository = context.getBean(PrestamoRepository.class);
        // cifrar password
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class); // hay que crear bean, hecho en WebSecurityConfig
        //Usuario user1 = new Usuario(null, "daw", encoder.encode("1234"), "USER");
        //usuarioRepository.save(user1);
        
        if (libroRepository.count() == 0) {
            System.out.println(">>> Base de datos vacía. Cargando datos iniciales...");

            Usuario admin = usuarioRepository.save(new Usuario(null, "admin", encoder.encode("admin123"), "ADMIN"));
            Usuario lector1 = usuarioRepository.save(new Usuario(null, "juan_lector", encoder.encode("pass123"), "USER"));
            Usuario lector2 = usuarioRepository.save(new Usuario(null, "maria_libros", encoder.encode("pass123"), "USER"));

            // 1. Crear y guardar Autores
            Autor autor1 = new Autor(null, "Miguel", "de Cervantes", "Española");
            Autor autor2 = new Autor(null, "Isaac", "Asimov", "Estadounidense");
            Autor autor3 = new Autor(null, "J.K.", "Rowling", "Británica");
            autorRepository.save(autor1);
            autorRepository.save(autor2);
            autorRepository.save(autor3);

            // 2. Crear y guardar Editoriales
            Editorial editorial1 = new Editorial(null, "Planeta", "Calle A", "España", "www.planeta.es");
            Editorial editorial2 = new Editorial(null, "Minotauro", "Calle B", "España", "www.minotauro.com");
            Editorial editorial3 = new Editorial(null, "Pearson", "Calle C", "Reino Unido", "www.pearson.com");
            editorialRepository.save(editorial1);
            editorialRepository.save(editorial2);
            editorialRepository.save(editorial3);

            // 3. Crear Libros
            Libro libro1 = new Libro(null, "Don Quijote de la Mancha", "978-84-1", 1605, "portada_quijote.jpg");
            Libro libro2 = new Libro(null, "Fundación", "978-84-2", 1951, "portada_fundacion.jpg");
            Libro libro3 = new Libro(null, "Yo, Robot", "978-84-3", 1950, "portada_robot.jpg");
            Libro libro4 = new Libro(null, "Harry Potter y la piedra filosofal", "978-84-4", 1997, "portada_harry.jpg");

            // 4. Establecer relaciones (Asociaciones)
            // Relación Libro-Autor
            libro1.setAutor(autor1);
            libro2.setAutor(autor2);
            libro3.setAutor(autor2);
            libro4.setAutor(autor3);

            // Relación Libro-Editorial
            libro1.setEditorial(editorial1);
            libro2.setEditorial(editorial2);
            libro3.setEditorial(editorial2);
            libro4.setEditorial(editorial3);

            // Establecer Categorías (Enums)
            libro1.setCategoria(CategoriaLibro.FICCION);
            libro2.setCategoria(CategoriaLibro.CIENCIA);
            libro3.setCategoria(CategoriaLibro.CIENCIA);
            libro4.setCategoria(CategoriaLibro.FICCION);

            //Establecemos libros favoritos, best sellers
            //y por leer
            libro1.setFavorito(true);
            libro1.setBestSeller(true);
            libro2.setFavorito(true);
            libro3.setPorLeer(true);
            libro4.setFavorito(true);
            libro4.setBestSeller(true);

            // 5. Guardar Libros en la base de datos
            libroRepository.save(libro1);
            libroRepository.save(libro2);
            libroRepository.save(libro3);
            libroRepository.save(libro4);

            //Relación Usuario Libro
            Prestamo prestamoActivo = new Prestamo();
            prestamoActivo.setLibro(libro4);
            prestamoActivo.setUsuario(lector1);
            prestamoActivo.setFechaPrestamo(LocalDate.now().minusDays(3)); // Hace 3 días
            prestamoActivo.setEstado("Activo");
            prestamoRepository.save(prestamoActivo);

            // 6. Verificación por consola
            System.out.println("\n--- PRUEBA DE CARGA DE DATOS ---");
            System.out.println("Total de libros en BD: " + libroRepository.count());

            List<Libro> todosLosLibros = libroRepository.findAll();
            for (Libro l : todosLosLibros) {
                System.out.println("Libro: " + l.getTitulo()
                        + " | Autor: " + l.getAutor().getNombre()
                        + " | Editorial: " + l.getEditorial().getNombre()
                        + " | Categoria: " + l.getCategoria());
            }
            System.out.println("--------------------------------\n");

        } else {
            System.out.println(">>> La base de datos ya contiene " + libroRepository.count() + " libros. Saltando carga.");
        }

    }
}
