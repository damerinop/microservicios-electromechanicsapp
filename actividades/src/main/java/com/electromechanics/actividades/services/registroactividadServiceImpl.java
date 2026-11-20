package com.electromechanics.actividades.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.electromechanics.actividades.entities.RegistroActividad;
import com.electromechanics.actividades.repository.registroactividadRepository;

@Service
public class registroactividadServiceImpl implements registroactividadService {

    private final registroactividadRepository actividadRepo;

    public registroactividadServiceImpl(registroactividadRepository registroactividadRepository) {
        this.actividadRepo = registroactividadRepository;
    }

    public List<RegistroActividad> obtenerTodas() {
        return actividadRepo.findAll();
    }

    public Optional<RegistroActividad> obtenerPorId(UUID id) {
        return actividadRepo.findById(id);
    }

    public RegistroActividad crear(RegistroActividad actividad) {
        if (actividad.getFechaHoraRegistro() == null) {
            actividad.setFechaHoraRegistro(LocalDateTime.now());
        }
        return actividadRepo.save(actividad);
    }

}