package com.electromechanics.empleados.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.electromechanics.empleados.entities.Empleado;
import com.electromechanics.empleados.services.empleadoService;

@RestController
@RequestMapping("/api/empleados")
public class empleadoController {

    private final empleadoService empleadoService;

    public empleadoController(empleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public List<Empleado> listarEmpleados() {
        return empleadoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable UUID id) {
        return empleadoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empleado> guardarEmpleado(@RequestBody Empleado empleado) {
        Empleado guardado = empleadoService.crear(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
    
}