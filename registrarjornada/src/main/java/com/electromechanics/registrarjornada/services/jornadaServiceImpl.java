package com.electromechanics.registrarjornada.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.electromechanics.registrarjornada.entities.Jornada;
import com.electromechanics.registrarjornada.repository.jornadaRepository;

@Service
public class jornadaServiceImpl implements jornadaService {

    private final jornadaRepository jornadaRepo;

    public jornadaServiceImpl(jornadaRepository jornadaRepository) {
        this.jornadaRepo = jornadaRepository;
    }

    @Override
    public List<Jornada> obtenerTodas() {
        return jornadaRepo.findAll();
    }

    @Override
    public Optional<Jornada> obtenerPorId(UUID id) {
        return jornadaRepo.findById(id);
    }

    @Override
    public Jornada crear(Jornada jornada) {
        if (jornada.getFechaHoraMarcacion() == null) {
            jornada.setFechaHoraMarcacion(java.time.LocalDateTime.now());
        }
        return jornadaRepo.save(jornada);
    }

}