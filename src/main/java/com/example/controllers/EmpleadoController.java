package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Empleado;
import com.example.services.EmpleadoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll(@RequestParam(name="page", required=false) Integer page,
    @RequestParam(name="size", required=false) Integer size){

        List<Empleado> empleados;
        //Ordenacion por nombre (la del ejercicio anterior)
        //Sort sort = Sort.by("nombre");
        //Ordenacion nueva que me ha pedido Victor
        Sort sort = Sort.by(Direction.ASC, "fechaAlta");
        
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Empleado> pageEmpleados = empleadoService.findAll(pageable);
            empleados = pageEmpleados.getContent();
        }else{
            empleados = empleadoService.findAll(sort);
        }
        
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> saveEmpleado(@Valid @RequestBody Empleado empleado, BindingResult results){
        
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> responseAsMap = new HashMap<>();

        if (results.hasErrors()) {
            List<String> mensajesError = new ArrayList<>();

            List<ObjectError> objectErrors =  results.getAllErrors();

            objectErrors.stream().forEach(o -> mensajesError.add(o.getDefaultMessage()));

            responseAsMap.put("errores", mensajesError);
            responseAsMap.put("Empleado", empleado);
            
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;
        }
        
        try {
            Empleado empleadoGuardado = empleadoService.save(empleado);
            String message = "El Empleado se ha creado exitoxamente";
            
            responseAsMap.put("mensaje", message);
            responseAsMap.put("Empleado", empleadoGuardado);

            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Throwable error = e.getMostSpecificCause();

            if (error != null) {
                String errorMessage = "No ha podido ser registrado el Empleado cuyo id es: , y la causa mas probable es: " + error;
                responseAsMap.put("mensaje", errorMessage);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        

        return responseEntity;
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateProduct(@Valid @RequestBody Empleado empleado, BindingResult results,
    @PathVariable Integer id){

        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> responseAsMap = new HashMap<>();

        if (results.hasErrors()) {
            List<String> mensajesError = new ArrayList<>();

            List<ObjectError> objectErrors =  results.getAllErrors();

            objectErrors.stream().forEach(o -> mensajesError.add(o.getDefaultMessage()));

            responseAsMap.put("errores", mensajesError);
            responseAsMap.put("empleado", empleado);
            
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;
        }
        
        try {
            empleado.setId(id);
            Empleado empleadoGuardado = empleadoService.save(empleado);
            String message = "El Empleado se ha actualizado exitoxamente";
            
            responseAsMap.put("mensaje", message);
            responseAsMap.put("Empleado", empleadoGuardado);

            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Throwable error = e.getMostSpecificCause();

            if (error != null) {
                String errorMessage = "No ha podido ser actualizado el Empleado cuyo id es: " + id
                + ", y la causa mas probable es: " + error;
                responseAsMap.put("mensaje", errorMessage);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        
        return responseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findByIdEmpleado(@PathVariable Integer id){
        ResponseEntity<Map<String, Object>> responseEntity = null;

        var responseAsMap = new HashMap<String, Object>();

        try {
            Empleado empleado = empleadoService.findById(id);
            if(empleado != null){
                String successMessage = "El Empleado se ha encontrado";
                responseAsMap.put("mensaje", successMessage);
                responseAsMap.put("Empleado", empleado);

                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
            }else{
                String notFoundMessage = "El Empleado con id " + id + " no se ha encontrado";
                responseAsMap.put("mensaje", notFoundMessage);

                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            Throwable error = e.getMostSpecificCause();

            if (error != null) {
                String errorMessage = "No ha podido ser encontrado el Empleado cuyo id es: " + id
                + ", y la causa mas probable es: " + error;
                responseAsMap.put("mensaje", errorMessage);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteEmpleado(@PathVariable Integer id){
        ResponseEntity<Map<String, Object>> responseEntity = null;

        var responseAsMap = new HashMap<String, Object>();

        try {
            empleadoService.delete(empleadoService.findById(id));
            String successMessage = "El Empleado con id " + id + " ha sido eliminado";
            responseAsMap.put("mensaje", successMessage);

            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.OK);
        } catch (DataAccessException e) {
            Throwable error = e.getMostSpecificCause();

            if (error != null) {
                String errorMessage = "No ha podido ser eliminado el Empleado cuyo id es: " + id
                + ", y la causa mas probable es: " + error;
                responseAsMap.put("mensaje", errorMessage);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return responseEntity;
    }
}
