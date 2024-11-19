package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.example.models.Genero;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="empleados")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Empleado implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String primerAp;
    private String segundoAp;
    private LocalDate fechaAlta;
    private double salario;
    
    @Enumerated(EnumType.STRING)
    private Genero genero;
    
    @NotNull(message="El departamento es requerido")
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Departamento departamento;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="empleado", cascade=CascadeType.PERSIST)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Telefono> telefonos;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="empleado", cascade=CascadeType.PERSIST)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Correo> correos;
}