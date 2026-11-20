package com.electromechanics.actividades.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.electromechanics.actividades.entities.RegistroActividad;

public interface registroactividadRepository extends JpaRepository<RegistroActividad, UUID> {

}