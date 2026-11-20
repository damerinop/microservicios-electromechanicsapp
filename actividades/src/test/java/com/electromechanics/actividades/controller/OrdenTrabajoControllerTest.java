package com.electromechanics.actividades.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.electromechanics.actividades.entities.OrdenTrabajo;
import com.electromechanics.actividades.services.ordentrabajoService;

@ExtendWith(MockitoExtension.class)
class OrdenTrabajoControllerTest {

    @Mock
    private ordentrabajoService service;

    private ordentrabajoController controller;

    @BeforeEach
    void setUp() {
        controller = new ordentrabajoController(service);
    }

    private OrdenTrabajo orden() {
        OrdenTrabajo o = new OrdenTrabajo();
        o.setIdSupervisor(UUID.randomUUID());
        o.setIdTecnicoAsignado(UUID.randomUUID());
        o.setCliente("Metalurgica del Maipo");
        o.setTarea("Reparacion de motor 5HP");
        return o;
    }

    @Test
    void listarOrdenesTrabajoDevuelveLasOrdenesDelServicio() {
        List<OrdenTrabajo> lista = List.of(orden(), orden());
        when(service.obtenerTodas()).thenReturn(lista);

        assertSame(lista, controller.listarOrdenesTrabajo());
    }

    @Test
    void obtenerPorIdResponde200ConLaOrden() {
        UUID id = UUID.randomUUID();
        OrdenTrabajo o = orden();
        when(service.obtenerPorId(id)).thenReturn(Optional.of(o));

        ResponseEntity<OrdenTrabajo> respuesta = controller.obtenerPorId(id);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(o, respuesta.getBody());
    }

    @Test
    void obtenerPorIdResponde404SiNoExiste() {
        UUID id = UUID.randomUUID();
        when(service.obtenerPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<OrdenTrabajo> respuesta = controller.obtenerPorId(id);

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }

    @Test
    void crearResponde201ConLaOrdenCreada() {
        OrdenTrabajo entrada = orden();
        OrdenTrabajo creada = orden();
        creada.setIdOrden(UUID.randomUUID());
        when(service.crear(entrada)).thenReturn(creada);

        ResponseEntity<OrdenTrabajo> respuesta = controller.crear(entrada);

        assertEquals(201, respuesta.getStatusCode().value());
        assertSame(creada, respuesta.getBody());
        verify(service).crear(entrada);
    }

    @Test
    void actualizarEstadoResponde200ConLaOrdenActualizada() {
        UUID id = UUID.randomUUID();
        OrdenTrabajo actualizada = orden();
        actualizada.setEstado("Finalizado");
        when(service.actualizarEstado(id, "Finalizado")).thenReturn(actualizada);

        ResponseEntity<OrdenTrabajo> respuesta = controller.actualizarEstado(id, Map.of("estado", "Finalizado"));

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(actualizada, respuesta.getBody());
    }

    @Test
    void actualizarEstadoResponde400SiNoViajaElEstado() {
        UUID id = UUID.randomUUID();

        ResponseEntity<OrdenTrabajo> respuesta = controller.actualizarEstado(id, Map.of());

        assertEquals(400, respuesta.getStatusCode().value());
        verifyNoInteractions(service);
    }

    @Test
    void actualizarEstadoResponde400SiElEstadoEstaEnBlanco() {
        UUID id = UUID.randomUUID();

        ResponseEntity<OrdenTrabajo> respuesta = controller.actualizarEstado(id, Map.of("estado", "  "));

        assertEquals(400, respuesta.getStatusCode().value());
        verifyNoInteractions(service);
    }

    @Test
    void actualizarEstadoResponde404SiLaOrdenNoExiste() {
        UUID id = UUID.randomUUID();
        when(service.actualizarEstado(id, "En Proceso")).thenThrow(new RuntimeException("no encontrada"));

        ResponseEntity<OrdenTrabajo> respuesta = controller.actualizarEstado(id, Map.of("estado", "En Proceso"));

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }
}
