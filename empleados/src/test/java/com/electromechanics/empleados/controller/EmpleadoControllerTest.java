package com.electromechanics.empleados.controller;

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

import com.electromechanics.empleados.entities.Empleado;
import com.electromechanics.empleados.services.empleadoService;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    @Mock
    private empleadoService service;

    private empleadoController controller;

    @BeforeEach
    void setUp() {
        controller = new empleadoController(service);
    }

    private Empleado empleado() {
        Empleado e = new Empleado();
        e.setRutTrabajador("12345678-5");
        e.setNombreCompleto("Juan Perez");
        e.setCorreoCorporativo("juan@electromechanics.cl");
        e.setContrasena("clave-segura-123");
        return e;
    }

    @Test
    void listarEmpleadosDevuelveLosEmpleadosDelServicio() {
        List<Empleado> lista = List.of(empleado(), empleado());
        when(service.obtenerTodos()).thenReturn(lista);

        assertSame(lista, controller.listarEmpleados());
    }

    @Test
    void obtenerPorIdResponde200ConElEmpleado() {
        UUID id = UUID.randomUUID();
        Empleado e = empleado();
        when(service.obtenerPorId(id)).thenReturn(Optional.of(e));

        ResponseEntity<Empleado> respuesta = controller.obtenerPorId(id);

        assertEquals(200, respuesta.getStatusCode().value());
        assertSame(e, respuesta.getBody());
    }

    @Test
    void obtenerPorIdResponde404SiNoExiste() {
        UUID id = UUID.randomUUID();
        when(service.obtenerPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<Empleado> respuesta = controller.obtenerPorId(id);

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }

    @Test
    void guardarEmpleadoResponde201ConElEmpleadoCreado() {
        Empleado entrada = empleado();
        Empleado creado = empleado();
        creado.setIdEmpleado(UUID.randomUUID());
        when(service.crear(entrada)).thenReturn(creado);

        ResponseEntity<Empleado> respuesta = controller.guardarEmpleado(entrada);

        assertEquals(201, respuesta.getStatusCode().value());
        assertSame(creado, respuesta.getBody());
        verify(service).crear(entrada);
    }
}
