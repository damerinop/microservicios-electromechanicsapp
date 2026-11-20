package com.electromechanics.empleados.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.electromechanics.empleados.entities.Empleado;

public interface empleadoRepository extends JpaRepository<Empleado, UUID> {

}