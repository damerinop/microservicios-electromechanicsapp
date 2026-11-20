package com.electromechanics.empleados.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.electromechanics.empleados.entities.Empleado;
import com.electromechanics.empleados.repository.empleadoRepository;

@Service
public class empleadoServiceImpl implements empleadoService {

    private final empleadoRepository empleadoRepo;

    public empleadoServiceImpl(empleadoRepository empleadoRepository) {
        this.empleadoRepo = empleadoRepository;
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepo.findAll();
    }

    @Override
    public Optional<Empleado> obtenerPorId(UUID id) {
        return empleadoRepo.findById(id);
    }

    @Override
    public Empleado crear(Empleado empleado) {
        if (empleado.getRol() == null || empleado.getRol().isBlank()) {
            empleado.setRol("TECNICO");
        }
        return empleadoRepo.save(empleado);
    }
    
}