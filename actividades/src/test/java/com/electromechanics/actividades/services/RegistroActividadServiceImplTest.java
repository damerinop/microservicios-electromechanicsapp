package com.electromechanics.actividades.services;

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

import com.electromechanics.actividades.entities.RegistroActividad;
import com.electromechanics.actividades.repository.registroactividadRepository;

@ExtendWith(MockitoExtension.class)
class RegistroActividadServiceImplTest {

    @Mock
    private registroactividadRepository repository;

    private registroactividadServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new registroactividadServiceImpl(repository);
    }

    private RegistroActividad actividad(LocalDateTime fecha) {
        RegistroActividad a = new RegistroActividad();
        a.setIdOrden(UUID.randomUUID());
        a.setIdTecnico(UUID.randomUUID());
        a.setComentario("Se reemplazo el contactor principal");
        a.setEvidenciaFotografica("/9j/4AAQSkZJRgABAQAAAQABAAD");
        a.setFechaHoraRegistro(fecha);
        return a;
    }

    @Test
    void obtenerTodasDevuelveLasActividadesDelRepositorio() {
        List<RegistroActividad> lista = List.of(actividad(null), actividad(null));
        when(repository.findAll()).thenReturn(lista);

        assertSame(lista, service.obtenerTodas());
    }

    @Test
    void obtenerPorIdDevuelveLaActividadExistente() {
        UUID id = UUID.randomUUID();
        RegistroActividad a = actividad(null);
        when(repository.findById(id)).thenReturn(Optional.of(a));

        Optional<RegistroActividad> resultado = service.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertSame(a, resultado.get());
    }

    @Test
    void obtenerPorIdDevuelveVacioSiNoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertTrue(service.obtenerPorId(id).isEmpty());
    }

    @Test
    void crearAsignaLaFechaActualSiNoViene() {
        when(repository.save(any(RegistroActividad.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime antes = LocalDateTime.now();

        RegistroActividad guardada = service.crear(actividad(null));

        assertNotNull(guardada.getFechaHoraRegistro());
        assertFalse(guardada.getFechaHoraRegistro().isBefore(antes));
        assertFalse(guardada.getFechaHoraRegistro().isAfter(LocalDateTime.now()));
    }

    @Test
    void crearConservaLaFechaCuandoViene() {
        when(repository.save(any(RegistroActividad.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDateTime fija = LocalDateTime.of(2026, 1, 15, 8, 30);

        RegistroActividad guardada = service.crear(actividad(fija));

        assertEquals(fija, guardada.getFechaHoraRegistro());
    }

    @Test
    void crearConservaComentarioYEvidencia() {
        when(repository.save(any(RegistroActividad.class))).thenAnswer(inv -> inv.getArgument(0));

        RegistroActividad guardada = service.crear(actividad(null));

        assertEquals("Se reemplazo el contactor principal", guardada.getComentario());
        assertEquals("/9j/4AAQSkZJRgABAQAAAQABAAD", guardada.getEvidenciaFotografica());
    }

    @Test
    void crearDevuelveLoQueEntregaElRepositorio() {
        UUID id = UUID.randomUUID();
        RegistroActividad entrada = actividad(LocalDateTime.of(2026, 1, 15, 8, 30));
        RegistroActividad persistida = actividad(LocalDateTime.of(2026, 1, 15, 8, 30));
        persistida.setIdActividad(id);
        when(repository.save(entrada)).thenReturn(persistida);

        RegistroActividad resultado = service.crear(entrada);

        assertEquals(id, resultado.getIdActividad());
        verify(repository).save(entrada);
    }
}
