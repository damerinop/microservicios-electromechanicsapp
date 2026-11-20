package com.electromechanics.actividades.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.electromechanics.actividades.entities.OrdenTrabajo;
import com.electromechanics.actividades.services.ordentrabajoService;

@RestController
@RequestMapping("/api/ordenes-trabajo")
public class ordentrabajoController {

    private final ordentrabajoService ordentrabajoService;

    public ordentrabajoController(ordentrabajoService ordentrabajoService) {
        this.ordentrabajoService = ordentrabajoService;
    }

    @GetMapping
    public List<OrdenTrabajo> listarOrdenesTrabajo() {
        return ordentrabajoService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajo> obtenerPorId(@PathVariable UUID id) {
        return ordentrabajoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdenTrabajo> crear(@RequestBody OrdenTrabajo orden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordentrabajoService.crear(orden));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenTrabajo> actualizarEstado(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null || estado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            OrdenTrabajo actualizada = ordentrabajoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}