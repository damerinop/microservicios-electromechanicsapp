package com.electromechanics.actividades.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.electromechanics.actividades.entities.RegistroActividad;
import com.electromechanics.actividades.services.registroactividadService;

@ExtendWith(MockitoExtension.class)
class RegistroActividadControllerTest {

    @Mock
    private registroactividadService service;

    private registroactividadController controller;

    @BeforeEach
    void setUp() {
        controller = new registroactividadController(service);
    }

    private RegistroActividad actividad() {
        RegistroActividad a = new RegistroActividad();
        a.setIdOrden(UUID.randomUUID());
        a.setIdTecnico(UUID.randomUUID());
        a.setComentario("Revision de rodamientos");
        return a;
    }

    @Test
    void listarActividadesDevuelveLasActividadesDelServicio() {
        List<RegistroActividad> lista = List.of(actividad(), actividad());
        when(service.obtenerTodas()).thenReturn(lista);

        assertSame(lista, controller.listarActividades());
    }

    @Test
    void obtenerPorIdResponde200ConLaActividad() {
        UUID id = UUID.randomUUID();
        RegistroActividad a = actividad();
        when(service.obtenerPorId(id)).thenReturn(Optional.of(a));

        ResponseEntity<RegistroActividad> respuesta = controller.obtenerPorId(id);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(a, respuesta.getBody());
    }

    @Test
    void obtenerPorIdResponde404SiNoExiste() {
        UUID id = UUID.randomUUID();
        when(service.obtenerPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<RegistroActividad> respuesta = controller.obtenerPorId(id);

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }

    @Test
    void crearResponde201ConLaActividadGuardada() {
        RegistroActividad entrada = actividad();
        RegistroActividad guardada = actividad();
        guardada.setIdActividad(UUID.randomUUID());
        when(service.crear(entrada)).thenReturn(guardada);

        ResponseEntity<RegistroActividad> respuesta = controller.crear(entrada);

        assertEquals(201, respuesta.getStatusCode().value());
        assertSame(guardada, respuesta.getBody());
        verify(service).crear(entrada);
    }
}
