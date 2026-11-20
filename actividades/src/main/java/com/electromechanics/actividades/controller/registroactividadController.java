package com.electromechanics.actividades.controller;

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
import com.electromechanics.actividades.entities.RegistroActividad;
import com.electromechanics.actividades.services.registroactividadService;

@RestController
@RequestMapping("/api/actividades")
public class registroactividadController {

    private final registroactividadService registroactividadService;

    public registroactividadController(registroactividadService registroactividadService) {
        this.registroactividadService = registroactividadService;
    }

    @GetMapping
    public List<RegistroActividad> listarActividades() {
        return registroactividadService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroActividad> obtenerPorId(@PathVariable UUID id) {
        return registroactividadService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegistroActividad> crear(@RequestBody RegistroActividad actividad) {
        RegistroActividad guardada = registroactividadService.crear(actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

}