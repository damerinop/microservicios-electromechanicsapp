package com.electromechanics.empleados.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_empleado", updatable = false, nullable = false)
    private UUID idEmpleado;

    @Column(name = "rut_trabajador", nullable = false, unique = true, length = 12)
    private String rutTrabajador;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "rol", nullable = false, length = 13)
    private String rol = "TECNICO"; // "TECNICO", "SUPERVISOR", "ADMINISTRADOR"
    // Por defecto es TECNICO

    @Column(name = "especialidad_tecnica", length = 100)
    private String especialidadTecnica;

    @Column(name = "correo_corporativo", nullable = false, unique = true, length = 100)
    private String correoCorporativo;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
    
}