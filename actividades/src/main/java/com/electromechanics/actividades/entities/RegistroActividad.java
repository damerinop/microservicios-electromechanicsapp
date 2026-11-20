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
@Table(name = "registro_actividad")
public class RegistroActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_actividad", updatable = false, nullable = false)
    private UUID idActividad;

    @Column(name = "id_orden", nullable = false)
    private UUID idOrden;

    @Column(name = "id_tecnico", nullable = false)
    private UUID idTecnico;

    @Column(name = "comentario", length = 250)
    private String comentario;

    @Column(name = "evidencia_fotografica", columnDefinition = "TEXT")
    private String evidenciaFotografica;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaHoraRegistro;
    
}