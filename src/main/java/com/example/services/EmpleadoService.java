package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Empleado;

public interface EmpleadoService {
    public Page<Empleado> findAll(Pageable pageable);
    public List<Empleado> findAll(Sort sort);
    public Empleado findById(int id);
    public Empleado save(Empleado empleado);
    public void delete(Empleado empleado);
    public List<Empleado> findAll();
}
