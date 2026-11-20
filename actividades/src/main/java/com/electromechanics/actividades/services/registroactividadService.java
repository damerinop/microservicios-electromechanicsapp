package com.electromechanics.actividades.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.electromechanics.actividades.entities.RegistroActividad;

public interface registroactividadService {

    List<RegistroActividad> obtenerTodas();
    Optional<RegistroActividad> obtenerPorId(UUID id);
    RegistroActividad crear(RegistroActividad registro);

}
