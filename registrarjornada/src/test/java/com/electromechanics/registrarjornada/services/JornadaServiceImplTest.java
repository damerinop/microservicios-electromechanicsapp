package com.electromechanics.registrarjornada.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.electromechanics.registrarjornada.entities.Jornada;
import com.electromechanics.registrarjornada.repository.jornadaRepository;

@ExtendWith(MockitoExtension.class)
class JornadaServiceImplTest {

    @Mock
    private jornadaRepository repository;

    private jornadaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new jornadaServiceImpl(repository);
    }

    private Jornada jornada(String tipo, LocalDateTime fecha) {
        Jornada j = new Jornada();
        j.setIdEmpleado(UUID.randomUUID());
        j.setTipoMarcacion(tipo);
        j.setFechaHoraMarcacion(fecha);
        j.setLatitudMarcacion(-33.5123);
        j.setLongitudMarcacion(-70.7561);
        return j;
    }

    @Test
    void obtenerTodasDevuelveLasJornadasDelRepositorio() {
        List<Jornada> lista = List.of(jornada("ENTRADA", null), jornada("SALIDA", null));
        when(repository.findAll()).thenReturn(lista);

        assertSame(lista, service.obtenerTodas());
    }

    @Test
    void obtenerPorIdDevuelveLaJornadaExistente() {
        UUID id = UUID.randomUUID();
        Jornada j = jornada("ENTRADA", null);
        when(repository.findById(id)).thenReturn(Optional.of(j));

        Optional<Jornada> resultado = service.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertSame(j, resultado.get());
    }

    @Test
    void obtenerPorIdDevuelveVacioSiNoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertTrue(service.obtenerPorId(id).isEmpty());
    }

    @Test
    void crearAsignaLaFechaActualSiNoViene() {
        when(repository.save(any(Jornada.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        Jornada guardada = service.crear(jornada("ENTRADA", null));

        assertNotNull(guardada.getFechaHoraMarcacion());
        assertFalse(guardada.getFechaHoraMarcacion().isBefore(antes));
        assertFalse(guardada.getFechaHoraMarcacion().isAfter(LocalDateTime.now()));
    }

    @Test
    void crearConservaLaFechaCuandoViene() {
        when(repository.save(any(Jornada.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime fija = LocalDateTime.of(2026, 1, 15, 8, 30);

        Jornada guardada = service.crear(jornada("SALIDA", fija));

        assertEquals(fija, guardada.getFechaHoraMarcacion());
    }

    @Test
    void crearConservaTipoYCoordenadas() {
        when(repository.save(any(Jornada.class))).thenAnswer(inv -> inv.getArgument(0));

        Jornada guardada = service.crear(jornada("ENTRADA", null));

        assertEquals("ENTRADA", guardada.getTipoMarcacion());
        assertEquals(-33.5123, guardada.getLatitudMarcacion());
        assertEquals(-70.7561, guardada.getLongitudMarcacion());
    }

    @Test
    void crearDevuelveLoQueEntregaElRepositorio() {
        UUID id = UUID.randomUUID();
        Jornada entrada = jornada("ENTRADA", LocalDateTime.of(2026, 1, 15, 8, 30));
        Jornada persistida = jornada("ENTRADA", LocalDateTime.of(2026, 1, 15, 8, 30));
        persistida.setIdRegistro(id);
        when(repository.save(entrada)).thenReturn(persistida);

        Jornada resultado = service.crear(entrada);

        assertEquals(id, resultado.getIdRegistro());
        verify(repository).save(entrada);
    }
}
