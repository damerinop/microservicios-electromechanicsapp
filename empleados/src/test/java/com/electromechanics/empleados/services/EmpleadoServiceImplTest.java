package com.electromechanics.empleados.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.electromechanics.empleados.entities.Empleado;
import com.electromechanics.empleados.repository.empleadoRepository;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private empleadoRepository repository;

    private empleadoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new empleadoServiceImpl(repository);
    }

    private Empleado empleado(String rol) {
        Empleado e = new Empleado();
        e.setRutTrabajador("12345678-5");
        e.setNombreCompleto("Juan Perez");
        e.setCorreoCorporativo("juan@electromechanics.cl");
        e.setContrasena("clave-segura-123");
        e.setRol(rol);
        return e;
    }

    @Test
    void obtenerTodosDevuelveLaListaDelRepositorio() {
        List<Empleado> lista = List.of(empleado("TECNICO"), empleado("SUPERVISOR"));
        when(repository.findAll()).thenReturn(lista);

        List<Empleado> resultado = service.obtenerTodos();

        assertEquals(2, resultado.size());
        assertSame(lista, resultado);
    }

    @Test
    void obtenerTodosDevuelveListaVaciaSiNoHayEmpleados() {
        when(repository.findAll()).thenReturn(List.of());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    void obtenerPorIdDevuelveElEmpleadoExistente() {
        UUID id = UUID.randomUUID();
        Empleado e = empleado("TECNICO");
        when(repository.findById(id)).thenReturn(Optional.of(e));

        Optional<Empleado> resultado = service.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertSame(e, resultado.get());
    }

    @Test
    void obtenerPorIdDevuelveVacioSiNoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertTrue(service.obtenerPorId(id).isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void crearAsignaRolTecnicoCuandoElRolEstaVacio(String rolVacio) {
        when(repository.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        Empleado guardado = service.crear(empleado(rolVacio));

        assertEquals("TECNICO", guardado.getRol());
    }

    @Test
    void crearConservaElRolSupervisor() {
        when(repository.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        Empleado guardado = service.crear(empleado("SUPERVISOR"));

        assertEquals("SUPERVISOR", guardado.getRol());
    }

    @Test
    void crearConservaElRolTecnicoExplicito() {
        when(repository.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        Empleado guardado = service.crear(empleado("TECNICO"));

        assertEquals("TECNICO", guardado.getRol());
    }

    @Test
    void crearDevuelveLoQueEntregaElRepositorio() {
        UUID id = UUID.randomUUID();
        Empleado entrada = empleado("TECNICO");
        Empleado persistido = empleado("TECNICO");
        persistido.setIdEmpleado(id);
        when(repository.save(entrada)).thenReturn(persistido);

        Empleado resultado = service.crear(entrada);

        assertEquals(id, resultado.getIdEmpleado());
        verify(repository).save(entrada);
    }
}
