package com.electromechanics.registrarjornada.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.electromechanics.registrarjornada.entities.Jornada;

public interface jornadaService {

    List<Jornada> obtenerTodas();
    Optional<Jornada> obtenerPorId(UUID id);
    Jornada crear(Jornada jornada);

}