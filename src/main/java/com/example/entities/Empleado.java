package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedBy;

import com.example.models.Genero;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    //@Column(name="empleado_id")
    private int id;

    @NotNull(message="El nombre del empleado no puede ser nulo")
    @NotEmpty(message="El nombre del empleado no puede estar vacio")
    @Size(min=4, max=25, message="El nombre del empleado tiene que estar entre 4 y 25 caracteres")
    private String nombre;

    @NotNull(message="El apellido del empleado no puede ser nulo")
    @NotEmpty(message="El apellido del empleado no puede estar vacio")
    @Size(min=4, max=25, message="El apellido del empleado tiene que estar entre 4 y 25 caracteres")
    private String primerAp;
    private String segundoAp;

    //@Pattern(regexp="\\d{4}-\\d{2}-\\d{2}", message="La fecha requiere un formato YYYY-MM-DD")
    private LocalDate fechaAlta;
    
    @Min(value=0, message="El salario no puede ser negativo")
    private double salario;
    
    
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotNull(message="La imagen del empleado es requerida")
    private String imagenEmpleado;
    
    @NotNull(message="El departamento es requerido")
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Departamento departamento;
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="empleado_id", referencedColumnName="id")
    private List<Telefono> telefonos;
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="empleado_id", referencedColumnName="id")
    private List<Correo> correos;
}
