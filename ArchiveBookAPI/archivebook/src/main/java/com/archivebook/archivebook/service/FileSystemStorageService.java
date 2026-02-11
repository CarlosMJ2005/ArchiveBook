package com.archivebook.archivebook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService {

    private final Path rootLocation = Paths.get("src/main/resources/static/uploads");

    public FileSystemStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de carga", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) throw new RuntimeException("Fichero vacío");
            
            // Generar nombre aleatorio manteniendo la extensión original
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String nombreFichero = UUID.randomUUID().toString() + extension;
            
            Files.copy(file.getInputStream(), this.rootLocation.resolve(nombreFichero),
                    StandardCopyOption.REPLACE_EXISTING);
            return nombreFichero;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar fichero", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}