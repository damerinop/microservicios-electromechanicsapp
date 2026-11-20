package com.electromechanics.empleados.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.electromechanics.empleados.entities.Empleado;

public interface empleadoService {

    List<Empleado> obtenerTodos();
    Optional<Empleado> obtenerPorId(UUID id);
    Empleado crear(Empleado empleado);

}
