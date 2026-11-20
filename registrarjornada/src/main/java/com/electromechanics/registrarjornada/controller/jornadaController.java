package com.electromechanics.registrarjornada.controller;

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
import com.electromechanics.registrarjornada.entities.Jornada;
import com.electromechanics.registrarjornada.services.jornadaService;

@RestController
@RequestMapping("/api/registrarjornadas")
public class jornadaController {

    private final jornadaService jornadaService;

    public jornadaController(jornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public List<Jornada> listarJornadas() {
        return jornadaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jornada> obtenerPorId(@PathVariable UUID id) {
        return jornadaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Jornada> guardarJornada(@RequestBody Jornada jornada) {
        Jornada guardada = jornadaService.crear(jornada);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

}