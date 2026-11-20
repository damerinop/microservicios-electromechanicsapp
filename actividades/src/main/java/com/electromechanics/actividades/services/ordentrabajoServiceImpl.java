package com.electromechanics.actividades.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.electromechanics.actividades.entities.OrdenTrabajo;
import com.electromechanics.actividades.repository.ordentrabajoRepository;

@Service
public class ordentrabajoServiceImpl implements ordentrabajoService {

    private final ordentrabajoRepository trabajoRepo;

    public ordentrabajoServiceImpl(ordentrabajoRepository ordentrabajoRepository) {
        this.trabajoRepo = ordentrabajoRepository;
    }

    @Override
    public List<OrdenTrabajo> obtenerTodas() {
        return trabajoRepo.findAll();
    }

    @Override
    public Optional<OrdenTrabajo> obtenerPorId(UUID id) {
        return trabajoRepo.findById(id);
    }

    @Override
    public OrdenTrabajo crear(OrdenTrabajo orden) {
        if (orden.getEstado() == null || orden.getEstado().isBlank()) {
            orden.setEstado("Pendiente");
        }
        if (orden.getFechaCreacion() == null) {
            orden.setFechaCreacion(LocalDateTime.now());
        }
        return trabajoRepo.save(orden);
    }

    @Override
    public OrdenTrabajo actualizarEstado(UUID id, String nuevoEstado) {
        OrdenTrabajo orden = trabajoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada con ID: " + id));
        orden.setEstado(nuevoEstado);
        return trabajoRepo.save(orden);
    }

}