package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Correo;

@Repository
public interface CorreoDao extends JpaRepository<Correo, Integer>{

}
