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

        if (libroRepository.count() == 0) {
            System.out.println(">>> Base de datos vacía. Cargando datos iniciales...");

            // 1. USUARIOS
            Usuario admin = usuarioRepository.save(new Usuario(null, "admin", encoder.encode("admin123"), "ADMIN"));
            Usuario juan_lector = usuarioRepository.save(new Usuario(null, "juan_lector", encoder.encode("pass123"), "USER"));
            Usuario maria_libros = usuarioRepository.save(new Usuario(null, "maria_libros", encoder.encode("pass123"), "USER"));

            // 2. AUTORES REALES
            Autor autorCervantes = autorRepository.save(new Autor(null, "Miguel", "de Cervantes", "Española"));
            Autor autorAsimov = autorRepository.save(new Autor(null, "Isaac", "Asimov", "Estadounidense"));
            Autor autorRowling = autorRepository.save(new Autor(null, "J.K.", "Rowling", "Británica"));
            Autor autorKing = autorRepository.save(new Autor(null, "Stephen", "King", "Estadounidense"));
            Autor autorAgatha = autorRepository.save(new Autor(null, "Agatha", "Christie", "Británica"));
            Autor autorZafon = autorRepository.save(new Autor(null, "Carlos", "Ruiz Zafón", "Española"));
            Autor autorClear = autorRepository.save(new Autor(null, "James", "Clear", "Estadounidense"));
            Autor autorGoleman = autorRepository.save(new Autor(null, "Daniel", "Goleman", "Estadounidense"));

            // 3. EDITORIALES REALES
            Editorial edPlaneta = editorialRepository.save(new Editorial(null, "Planeta", "Barcelona", "España", "www.planeta.es"));
            Editorial edMinotauro = editorialRepository.save(new Editorial(null, "Minotauro", "Barcelona", "España", "www.minotauro.com"));
            Editorial edSalamandra = editorialRepository.save(new Editorial(null, "Salamandra", "Madrid", "España", "www.salamandra.info"));
            Editorial edDebolsillo = editorialRepository.save(new Editorial(null, "Debolsillo", "Barcelona", "España", "www.penguinrandomhouse.com"));

            // 4. CREACIÓN DE 24 LIBROS REALES

            // --- FICCION ---
            Libro l1 = new Libro(null, "El ingenioso hidalgo Don Quijote de la Mancha", "978-84-01", 1605, "quijote.jpg", "Las aventuras de un caballero andante.");
            l1.setAutor(autorCervantes); l1.setEditorial(edPlaneta); l1.setCategoria(CategoriaLibro.FICCION); l1.setBestSeller(true);
            libroRepository.save(l1);

            Libro l2 = new Libro(null, "La sombra del viento", "978-84-02", 2001, "sombra_viento.jpg", "Un misterio en la Barcelona de posguerra.");
            l2.setAutor(autorZafon); l2.setEditorial(edPlaneta); l2.setCategoria(CategoriaLibro.FICCION);
            libroRepository.save(l2);

            Libro l3 = new Libro(null, "Harry Potter y las reliquias de la muerte", "978-84-03", 2007, "hp7.jpg", "El enfrentamiento final contra Voldemort.");
            l3.setAutor(autorRowling); l3.setEditorial(edSalamandra); l3.setCategoria(CategoriaLibro.FICCION); l3.setBestSeller(true);
            libroRepository.save(l3);

            // --- CIENCIA ---
            Libro l4 = new Libro(null, "Fundación e Imperio", "978-84-04", 1952, "fundacion_imperio.jpg", "La lucha de la Fundación contra el Imperio.");
            l4.setAutor(autorAsimov); l4.setEditorial(edMinotauro); l4.setCategoria(CategoriaLibro.CIENCIA);
            libroRepository.save(l4);

            Libro l5 = new Libro(null, "Los propios dioses", "978-84-05", 1972, "propios_dioses.jpg", "Contacto con una civilización paralela.");
            l5.setAutor(autorAsimov); l5.setEditorial(edMinotauro); l5.setCategoria(CategoriaLibro.CIENCIA);
            libroRepository.save(l5);

            Libro l6 = new Libro(null, "Bóvedas de acero", "978-84-06", 1954, "bovedas_acero.jpg", "Investigación criminal entre humanos y robots.");
            l6.setAutor(autorAsimov); l6.setEditorial(edMinotauro); l6.setCategoria(CategoriaLibro.CIENCIA);
            libroRepository.save(l6);

            // --- MISTERIO ---
            Libro l7 = new Libro(null, "Diez negritos", "978-84-07", 1939, "diez_negritos.jpg", "Diez personas atrapadas en una isla.");
            l7.setAutor(autorAgatha); l7.setEditorial(edDebolsillo); l7.setCategoria(CategoriaLibro.MISTERIO);
            libroRepository.save(l7);

            Libro l8 = new Libro(null, "El resplandor", "978-84-08", 1977, "resplandor.jpg", "Terror psicológico en el Hotel Overlook.");
            l8.setAutor(autorKing); l8.setEditorial(edDebolsillo); l8.setCategoria(CategoriaLibro.MISTERIO);
            libroRepository.save(l8);

            Libro l9 = new Libro(null, "Asesinato en el Orient Express", "978-84-09", 1934, "orient_express.jpg", "Un crimen en el tren más famoso del mundo.");
            l9.setAutor(autorAgatha); l9.setEditorial(edDebolsillo); l9.setCategoria(CategoriaLibro.MISTERIO);
            libroRepository.save(l9);

            // --- DIVULGACION ---
            Libro l10 = new Libro(null, "Breve historia de la humanidad", "978-84-10", 2014, "sapiens.jpg", "Evolución del Homo sapiens.");
            l10.setAutor(autorAsimov); l10.setEditorial(edDebolsillo); l10.setCategoria(CategoriaLibro.DIVULGACION);
            libroRepository.save(l10);

            Libro l11 = new Libro(null, "Cosmos", "978-84-11", 1980, "cosmos.jpg", "La historia de la ciencia y el universo.");
            l11.setAutor(autorAsimov); l11.setEditorial(edPlaneta); l11.setCategoria(CategoriaLibro.DIVULGACION);
            libroRepository.save(l11);

            Libro l12 = new Libro(null, "El mundo y sus demonios", "978-84-12", 1995, "demonios.jpg", "Defensa del pensamiento científico.");
            l12.setAutor(autorAsimov); l12.setEditorial(edPlaneta); l12.setCategoria(CategoriaLibro.DIVULGACION);
            libroRepository.save(l12);

            // --- BIOGRAFIA ---
            Libro l13 = new Libro(null, "Steve Jobs", "978-84-13", 2011, "jobs.jpg", "La vida del cofundador de Apple.");
            l13.setAutor(autorClear); l13.setEditorial(edDebolsillo); l13.setCategoria(CategoriaLibro.BIOGRAFIA);
            libroRepository.save(l13);

            Libro l14 = new Libro(null, "Diario de Ana Frank", "978-84-14", 1947, "ana_frank.jpg", "Relato de una joven en la II Guerra Mundial.");
            l14.setAutor(autorCervantes); l14.setEditorial(edDebolsillo); l14.setCategoria(CategoriaLibro.BIOGRAFIA);
            libroRepository.save(l14);

            Libro l15 = new Libro(null, "Elon Musk", "978-84-15", 2015, "musk.jpg", "Biografía del creador de Tesla.");
            l15.setAutor(autorClear); l15.setEditorial(edDebolsillo); l15.setCategoria(CategoriaLibro.BIOGRAFIA);
            libroRepository.save(l15);

            // --- AUTOAYUDA ---
            Libro l16 = new Libro(null, "Hábitos Atómicos", "978-84-16", 2018, "habitos.jpg", "Cambios pequeños, resultados grandes.");
            l16.setAutor(autorClear); l16.setEditorial(edPlaneta); l16.setCategoria(CategoriaLibro.AUTOAYUDA);
            libroRepository.save(l16);

            Libro l17 = new Libro(null, "Inteligencia Emocional", "978-84-17", 1995, "emocional.jpg", "Importancia de las emociones.");
            l17.setAutor(autorGoleman); l17.setEditorial(edDebolsillo); l17.setCategoria(CategoriaLibro.AUTOAYUDA);
            libroRepository.save(l17);

            Libro l18 = new Libro(null, "El poder del ahora", "978-84-18", 1997, "ahora.jpg", "Guía para la iluminación espiritual.");
            l18.setAutor(autorClear); l18.setEditorial(edPlaneta); l18.setCategoria(CategoriaLibro.AUTOAYUDA);
            libroRepository.save(l18);

            // --- INFANTILES ---
            Libro l19 = new Libro(null, "El Principito", "978-84-19", 1943, "principito.jpg", "Fábula sobre la amistad.");
            l19.setAutor(autorCervantes); l19.setEditorial(edSalamandra); l19.setCategoria(CategoriaLibro.INFANTILES);
            libroRepository.save(l19);

            Libro l20 = new Libro(null, "Charlie y la fábrica de chocolate", "978-84-20", 1964, "charlie.jpg", "Aventuras en una fábrica de dulces.");
            l20.setAutor(autorRowling); l20.setEditorial(edDebolsillo); l20.setCategoria(CategoriaLibro.INFANTILES);
            libroRepository.save(l20);

            Libro l21 = new Libro(null, "Matilda", "978-84-21", 1988, "matilda.jpg", "Una niña con poderes asombrosos.");
            l21.setAutor(autorRowling); l21.setEditorial(edDebolsillo); l21.setCategoria(CategoriaLibro.INFANTILES);
            libroRepository.save(l21);

            // --- CONSULTA ---
            Libro l22 = new Libro(null, "Diccionario de la RAE", "978-84-22", 1713, "rae.jpg", "Referencia oficial del español.");
            l22.setAutor(autorCervantes); l22.setEditorial(edPlaneta); l22.setCategoria(CategoriaLibro.CONSULTA);
            libroRepository.save(l22);

            Libro l23 = new Libro(null, "Atlas Mundial", "978-84-23", 2023, "atlas.jpg", "Cartografía global actualizada.");
            l23.setAutor(autorAsimov); l23.setEditorial(edPlaneta); l23.setCategoria(CategoriaLibro.CONSULTA);
            libroRepository.save(l23);

            Libro l24 = new Libro(null, "Enciclopedia de la Ciencia", "978-84-24", 2022, "enciclopedia.jpg", "Compendio científico integral.");
            l24.setAutor(autorAsimov); l24.setEditorial(edPlaneta); l24.setCategoria(CategoriaLibro.CONSULTA);
            libroRepository.save(l24);

            // 5. FAVORITOS (Usando libros creados l1, l3, etc.)
            Favoritos favoritosAdmin = new Favoritos();
            favoritosAdmin.setLibro(l1);
            favoritosAdmin.setUsuario(admin);
            favoritosRepository.save(favoritosAdmin);

            Favoritos favoritosJuan = new Favoritos();
            favoritosJuan.setLibro(l3);
            favoritosJuan.setUsuario(juan_lector);
            favoritosRepository.save(favoritosJuan);

            Favoritos favoritosMaria = new Favoritos();
            favoritosMaria.setLibro(l2);
            favoritosMaria.setUsuario(maria_libros);
            favoritosRepository.save(favoritosMaria);

            // 6. POR LEER
            PorLeer porLeer1 = new PorLeer();
            porLeer1.setLibro(l4);
            porLeer1.setUsuario(juan_lector);
            porLeerRepository.save(porLeer1);

            PorLeer porLeer2 = new PorLeer();
            porLeer2.setLibro(l8);
            porLeer2.setUsuario(maria_libros);
            porLeerRepository.save(porLeer2);

            // 7. PRÉSTAMOS (Marcando el libro como prestado para coherencia)
            l3.setPrestado(true);
            libroRepository.save(l3);

            Prestamo prestamoActivo = new Prestamo();
            prestamoActivo.setLibro(l3);
            prestamoActivo.setUsuario(juan_lector);
            prestamoActivo.setFechaPrestamo(LocalDate.now().minusDays(3));
            prestamoActivo.setDevuelto(false);
            prestamoRepository.save(prestamoActivo);

            System.out.println(">>> Carga inicial completada con éxito.");
        } else {
            System.out.println(">>> La base de datos ya contiene información. Omitiendo carga inicial.");
        }

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