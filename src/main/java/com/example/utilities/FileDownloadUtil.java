package com.example.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.example.entities.Empleado;
import com.example.services.EmpleadoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileDownloadUtil {

    private Path foundFile;
    private final EmpleadoService empleadoService;

    @SuppressWarnings("UnnecessaryReturnStatement")
    public Resource getFileAsResource(String fileCode) throws IOException {
        
        Path dirPath = Paths.get("Files-Upload");

        try {
            foundFile = Files.list(dirPath)
                .filter(file -> file.getFileName().toString().startsWith(fileCode))
                .findFirst().get();
        } catch (IOException e) {
            throw new IOException("Error fatal buscando el fichero", e);
        }

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        } 

        return null;
    }

    public Resource getFileAsResourceByIdEmpleado(int idEmpleado) throws IOException {
        
        Empleado empleado = empleadoService.findById(idEmpleado);

        String imagenProducto = empleado.getImagenEmpleado();

        String fileCode = imagenProducto.split("-")[0];

        Path dirPath = Paths.get("Files-Upload");

        try {
            foundFile = Files.list(dirPath)
            .filter(file -> file.getFileName().toString().startsWith(fileCode))
            .findFirst().get();
        } catch (IOException e) {
            throw new IOException("Error fatal buscando el fichero", e);
        }

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        } 

        return null;
    }
}
