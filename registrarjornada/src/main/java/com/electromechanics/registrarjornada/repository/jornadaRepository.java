package com.electromechanics.registrarjornada.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.electromechanics.registrarjornada.entities.Jornada;

public interface jornadaRepository extends JpaRepository<Jornada, UUID> {

}