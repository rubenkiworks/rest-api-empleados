package com.example.services;

import java.util.List;

import com.example.entities.Telefono;

public interface TelefonoService {
    public List<Telefono> findAll();
    public void save(Telefono telefono);
    public Telefono findById(int id);
}
