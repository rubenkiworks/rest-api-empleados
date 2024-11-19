package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Empleado;

@Repository
public interface EmpleadoDao extends JpaRepository<Empleado, Integer>{
    @Query(value="select e from Empleado e left join e.departamento left join e.telefonos left join e.correos", 
        countQuery = "select count(e) from Empleado e left join e.departamento")
    @Override
    public Page<Empleado> findAll(Pageable pageable);

    @Override
    @Query(value="select e from Empleado e left join e.departamento left join e.telefonos left join e.correos")
    public List<Empleado> findAll(Sort sort);

    @Query(value="select e from Empleado e left join e.departamento left join e.telefonos left join e.correos where e.id = :id")
    public Empleado findById(int id);
}
