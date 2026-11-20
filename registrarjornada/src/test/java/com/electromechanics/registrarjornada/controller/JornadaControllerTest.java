package com.electromechanics.registrarjornada.controller;

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

import com.electromechanics.registrarjornada.entities.Jornada;
import com.electromechanics.registrarjornada.services.jornadaService;

@ExtendWith(MockitoExtension.class)
class JornadaControllerTest {

    @Mock
    private jornadaService service;

    private jornadaController controller;

    @BeforeEach
    void setUp() {
        controller = new jornadaController(service);
    }

    private Jornada jornada() {
        Jornada j = new Jornada();
        j.setIdEmpleado(UUID.randomUUID());
        j.setTipoMarcacion("ENTRADA");
        j.setLatitudMarcacion(-33.5123);
        j.setLongitudMarcacion(-70.7561);
        return j;
    }

    @Test
    void listarJornadasDevuelveLasJornadasDelServicio() {
        List<Jornada> lista = List.of(jornada(), jornada());
        when(service.obtenerTodas()).thenReturn(lista);

        assertSame(lista, controller.listarJornadas());
    }

    @Test
    void obtenerPorIdResponde200ConLaJornada() {
        UUID id = UUID.randomUUID();
        Jornada j = jornada();
        when(service.obtenerPorId(id)).thenReturn(Optional.of(j));

        ResponseEntity<Jornada> respuesta = controller.obtenerPorId(id);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(j, respuesta.getBody());
    }

    @Test
    void obtenerPorIdResponde404SiNoExiste() {
        UUID id = UUID.randomUUID();
        when(service.obtenerPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Jornada> respuesta = controller.obtenerPorId(id);

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }

    @Test
    void guardarJornadaResponde201ConLaJornadaCreada() {
        Jornada entrada = jornada();
        Jornada creada = jornada();
        creada.setIdRegistro(UUID.randomUUID());
        when(service.crear(entrada)).thenReturn(creada);

        ResponseEntity<Jornada> respuesta = controller.guardarJornada(entrada);

        assertEquals(201, respuesta.getStatusCode().value());
        assertSame(creada, respuesta.getBody());
        verify(service).crear(entrada);
    }
}
