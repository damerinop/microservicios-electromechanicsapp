package com.electromechanics.actividades.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import com.electromechanics.actividades.entities.OrdenTrabajo;
import com.electromechanics.actividades.repository.ordentrabajoRepository;

@ExtendWith(MockitoExtension.class)
class OrdenTrabajoServiceImplTest {

    @Mock
    private ordentrabajoRepository repository;

    private ordentrabajoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ordentrabajoServiceImpl(repository);
    }

    private OrdenTrabajo orden(String estado, LocalDateTime fecha) {
        OrdenTrabajo o = new OrdenTrabajo();
        o.setIdSupervisor(UUID.randomUUID());
        o.setIdTecnicoAsignado(UUID.randomUUID());
        o.setCliente("Industria Manufacturera S.A.");
        o.setTarea("Mantencion preventiva tablero electrico");
        o.setEstado(estado);
        o.setFechaCreacion(fecha);
        return o;
    }

    @Test
    void obtenerTodasDevuelveLasOrdenesDelRepositorio() {
        List<OrdenTrabajo> lista = List.of(orden("Pendiente", null), orden("En Proceso", null));
        when(repository.findAll()).thenReturn(lista);

        assertSame(lista, service.obtenerTodas());
    }

    @Test
    void obtenerPorIdDevuelveLaOrdenExistente() {
        UUID id = UUID.randomUUID();
        OrdenTrabajo o = orden("Pendiente", null);
        when(repository.findById(id)).thenReturn(Optional.of(o));

        Optional<OrdenTrabajo> resultado = service.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertSame(o, resultado.get());
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
    void crearAsignaEstadoPendienteCuandoElEstadoEstaVacio(String estadoVacio) {
        when(repository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));

        OrdenTrabajo guardada = service.crear(orden(estadoVacio, null));

        assertEquals("Pendiente", guardada.getEstado());
    }

    @Test
    void crearConservaElEstadoCuandoViene() {
        when(repository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));

        OrdenTrabajo guardada = service.crear(orden("En Proceso", null));

        assertEquals("En Proceso", guardada.getEstado());
    }

    @Test
    void crearAsignaLaFechaActualSiNoViene() {
        when(repository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        OrdenTrabajo guardada = service.crear(orden("Pendiente", null));

        assertNotNull(guardada.getFechaCreacion());
        assertFalse(guardada.getFechaCreacion().isBefore(antes));
        assertFalse(guardada.getFechaCreacion().isAfter(LocalDateTime.now()));
    }

    @Test
    void crearConservaLaFechaCuandoViene() {
        when(repository.save(any(OrdenTrabajo.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime fija = LocalDateTime.of(2026, 1, 15, 8, 30);

        OrdenTrabajo guardada = service.crear(orden("Pendiente", fija));

        assertEquals(fija, guardada.getFechaCreacion());
    }

    @Test
    void actualizarEstadoCambiaElEstadoYGuarda() {
        UUID id = UUID.randomUUID();
        OrdenTrabajo existente = orden("Pendiente", LocalDateTime.of(2026, 1, 15, 8, 30));
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        OrdenTrabajo resultado = service.actualizarEstado(id, "Finalizado");

        assertEquals("Finalizado", resultado.getEstado());
        verify(repository).save(existente);
    }

    @Test
    void actualizarEstadoLanzaExcepcionSiLaOrdenNoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> service.actualizarEstado(id, "Finalizado"));

        assertTrue(error.getMessage().contains(id.toString()));
        verify(repository, never()).save(any(OrdenTrabajo.class));
    }
}
