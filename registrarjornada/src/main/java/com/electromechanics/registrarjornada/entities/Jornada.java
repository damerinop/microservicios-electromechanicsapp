package com.electromechanics.registrarjornada.entities;

import java.time.LocalDateTime;
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
@Table(name = "jornada")
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_registro", updatable = false, nullable = false)
    private UUID idRegistro;

    @Column(name = "id_empleado", nullable = false)
    private UUID idEmpleado;

    @Column(name = "tipo_marcacion", nullable = false, length = 7)
    private String tipoMarcacion; // "ENTRADA" o "SALIDA"

    @Column(name = "fecha_hora_marcacion", nullable = false)
    private LocalDateTime fechaHoraMarcacion;

    @Column(name = "latitud_marcacion", nullable = false)
    private Double latitudMarcacion;

    @Column(name = "longitud_marcacion", nullable = false)
    private Double longitudMarcacion;

}