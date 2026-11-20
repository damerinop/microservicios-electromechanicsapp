package com.electromechanics.actividades.entities;

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
@Table(name = "orden_trabajo")
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_orden", updatable = false, nullable = false)
    private UUID idOrden;

    @Column(name = "id_supervisor", nullable = false)
    private UUID idSupervisor;

    @Column(name = "id_tecnico_asignado")
    private UUID idTecnicoAsignado;

    @Column(name = "cliente", nullable = false, length = 100)
    private String cliente;

    @Column(name = "tarea", nullable = false, length = 100)
    private String tarea;

    @Column(name = "estado", nullable = false, length = 25)
    private String estado = "Pendiente";

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
}