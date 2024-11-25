package com.example.services;

import java.util.List;

import com.example.entities.Departamento;

public interface DepartamentoService {
    public List<Departamento> findAll();
    public void save(Departamento departamento);
    public Departamento findById(int id);
}
