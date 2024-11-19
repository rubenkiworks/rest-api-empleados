package com.example.services;

import java.util.List;

import com.example.entities.Correo;

public interface CorreoService {
    public List<Correo> findAll();
    public void save(Correo correo);
    public Correo findById(int id);
}
