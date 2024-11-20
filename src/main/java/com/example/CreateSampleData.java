package com.example;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Empleado;
import com.example.entities.Telefono;
import com.example.models.Genero;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.EmpleadoService;
import com.example.services.TelefonoService;

@Configuration
public class CreateSampleData {

    @Bean
    public CommandLineRunner samplesData(EmpleadoService empleadoService, DepartamentoService departamentoService,
    TelefonoService telefonoService, CorreoService correoService){
        return args -> {

            departamentoService.save(Departamento.builder()
            .id(1)
            .nombre("RRHH")
            .build());

            departamentoService.save(Departamento.builder()
            .id(2)
            .nombre("INFORMATICA")
            .build());

            empleadoService.save(Empleado.builder()
            .id(1)
            .nombre("Ruben")
            .primerAp("Gomez")
            .segundoAp("Sabio")
            .salario(3400.2)
            .genero(Genero.HOMBRE)
            .fechaAlta(LocalDate.of(2017, Month.APRIL, 2))
            .departamento(departamentoService.findById(1))
            .imagenEmpleado("test.jpeg")
            .build());

            empleadoService.save(Empleado.builder()
            .id(2)
            .nombre("Juan")
            .primerAp("Malo")
            .segundoAp("Gusto")
            .salario(1200)
            .genero(Genero.HOMBRE)
            .fechaAlta(LocalDate.of(2019, Month.AUGUST, 2))
            .departamento(departamentoService.findById(2))
            .imagenEmpleado("test.jpeg")
            .build());

            telefonoService.save(Telefono.builder()
            .numero("987654321")
            .empleado(empleadoService.findById(1))
            .build());

            telefonoService.save(Telefono.builder()
            .numero("123456789")
            .empleado(empleadoService.findById(1))
            .build());

            telefonoService.save(Telefono.builder()
            .numero("111111111")
            .empleado(empleadoService.findById(2))
            .build());

            correoService.save(Correo.builder()
            .direccion("rgomez@fmdkf.com")
            .empleado(empleadoService.findById(1))
            .build());

            correoService.save(Correo.builder()
            .direccion("gdfgf@fmdkf.com")
            .empleado(empleadoService.findById(1))
            .build());

            correoService.save(Correo.builder()
            .direccion("jguerrero@fmdkf.com")
            .empleado(empleadoService.findById(2))
            .build());
        };
    }

}
