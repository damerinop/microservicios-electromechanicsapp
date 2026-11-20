package com.electromechanics.actividades.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.electromechanics.actividades.entities.OrdenTrabajo;

public interface ordentrabajoService {

    List<OrdenTrabajo> obtenerTodas();
    Optional<OrdenTrabajo> obtenerPorId(UUID id);
    OrdenTrabajo crear(OrdenTrabajo trabajo);
    OrdenTrabajo actualizarEstado(UUID id, String estado);
    
}