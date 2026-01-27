package com.archivebook.archivebook;

import com.archivebook.archivebook.entities.Autor;
import com.archivebook.archivebook.entities.CategoriaLibro;
import com.archivebook.archivebook.entities.Editorial;
import com.archivebook.archivebook.entities.Libro;
import com.archivebook.archivebook.entities.Usuario;
import com.archivebook.archivebook.repository.AutorRepository;
import com.archivebook.archivebook.repository.EditorialRepository;
import com.archivebook.archivebook.repository.LibroRepository;
import com.archivebook.archivebook.repository.UsuarioRepository;
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

        // 1. Crear y guardar Autores
        Autor autor1 = new Autor(null, "Miguel", "de Cervantes", "Española");
        Autor autor2 = new Autor(null, "Isaac", "Asimov", "Estadounidense");
        autorRepository.save(autor1);
        autorRepository.save(autor2);

        // 2. Crear y guardar Editoriales
        Editorial editorial1 = new Editorial(null, "Planeta", "Calle A", "España", "www.planeta.es");
        Editorial editorial2 = new Editorial(null, "Minotauro", "Calle B", "España", "www.minotauro.com");
        editorialRepository.save(editorial1);
        editorialRepository.save(editorial2);

        // 3. Crear Libros
        Libro libro1 = new Libro(null, "Don Quijote de la Mancha", "978-84-1", 1605, "portada_quijote.jpg");
        Libro libro2 = new Libro(null, "Fundación", "978-84-2", 1951, "portada_fundacion.jpg");
        Libro libro3 = new Libro(null, "Yo, Robot", "978-84-3", 1950, "portada_robot.jpg");

        // 4. Establecer relaciones (Asociaciones)
        // Relación Libro-Autor
        libro1.setAutor(autor1);
        libro2.setAutor(autor2);
        libro3.setAutor(autor2);

        // Relación Libro-Editorial
        libro1.setEditorial(editorial1);
        libro2.setEditorial(editorial2);
        libro3.setEditorial(editorial2);

        // Establecer Categorías (Enums)
        libro1.setCategoria(CategoriaLibro.FICCION);
        libro2.setCategoria(CategoriaLibro.CIENCIA);
        libro3.setCategoria(CategoriaLibro.CIENCIA);

        // 5. Guardar Libros en la base de datos
        libroRepository.save(libro1);
        libroRepository.save(libro2);
        libroRepository.save(libro3);

        // 6. Verificación por consola
        System.out.println("\n--- PRUEBA DE CARGA DE DATOS ---");
        System.out.println("Total de libros en BD: " + libroRepository.count());
        
        List<Libro> todosLosLibros = libroRepository.findAll();
        for (Libro l : todosLosLibros) {
            System.out.println("Libro: " + l.getTitulo() + 
                               " | Autor: " + l.getAutor().getNombre() + 
                               " | Editorial: " + l.getEditorial().getNombre() +
                               " | Categoria: " + l.getCategoria());
        }
        System.out.println("--------------------------------\n");
        
        // cifrar password
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class); // hay que crear bean, hecho en WebSecurityConfig
        Usuario user1 = new Usuario(null, "daw", encoder.encode("1234"), "USER");
        usuarioRepository.save(user1);
    }
}
