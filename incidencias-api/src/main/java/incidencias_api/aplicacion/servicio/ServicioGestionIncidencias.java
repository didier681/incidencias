package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.HistorialEstadoIncidencia;
import incidencias_api.dominio.modelo.Incidencia;
import incidencias_api.dominio.puerto.salida.RepositorioHistorialEstadoIncidencia;
import incidencias_api.dominio.puerto.salida.RepositorioIncidencia;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioGestionIncidencias {

    private final RepositorioIncidencia repositorioIncidencia;
    private final RepositorioHistorialEstadoIncidencia repositorioHistorial;
    private final ServicioGestionNotificaciones servicioNotificaciones;

    public ServicioGestionIncidencias(
            RepositorioIncidencia repositorioIncidencia,
            RepositorioHistorialEstadoIncidencia repositorioHistorial,
            ServicioGestionNotificaciones servicioNotificaciones) {
        this.repositorioIncidencia = repositorioIncidencia;
        this.repositorioHistorial = repositorioHistorial;
        this.servicioNotificaciones = servicioNotificaciones;
    }

    @Transactional
    public Incidencia crear(
            UUID usuarioId,
            UUID categoriaId,
            String titulo,
            String descripcion,
            String ubicacion) {
        LocalDateTime ahora = LocalDateTime.now();

        Incidencia incidencia = new Incidencia(
                UUID.randomUUID(),
                usuarioId,
                categoriaId,
                titulo,
                descripcion,
                ubicacion,
                EstadoIncidencia.REPORTADA,
                ahora
        );

        Incidencia guardada = repositorioIncidencia.guardar(incidencia);

        repositorioHistorial.guardar(new HistorialEstadoIncidencia(
                UUID.randomUUID(),
                guardada.getId(),
                null,
                guardada.getEstado(),
                ahora
        ));

        return guardada;
    }

    @Transactional(readOnly = true)
    public List<Incidencia> listarTodas() {
        return repositorioIncidencia.listarTodas();
    }

    @Transactional(readOnly = true)
    public List<Incidencia> listarConFiltros(
            EstadoIncidencia estado,
            UUID categoriaId) {
        return repositorioIncidencia.listarConFiltros(estado, categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Incidencia> listarPorUsuarioId(UUID usuarioId) {
        return repositorioIncidencia.listarPorUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Incidencia buscarPorId(UUID id) {
        return repositorioIncidencia.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La incidencia no existe."
                ));
    }

    @Transactional(readOnly = true)
    public Incidencia buscarPorIdYUsuarioId(UUID id, UUID usuarioId) {
        return repositorioIncidencia.buscarPorIdYUsuarioId(id, usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La incidencia no existe o no pertenece al usuario."
                ));
    }

    @Transactional(readOnly = true)
    public List<HistorialEstadoIncidencia> listarHistorialEstados(
            UUID incidenciaId) {
        buscarPorId(incidenciaId);
        return repositorioHistorial.listarPorIncidenciaId(incidenciaId);
    }

    @Transactional
    public Incidencia cambiarEstado(
            UUID id,
            EstadoIncidencia nuevoEstado) {
        Incidencia incidencia = buscarPorId(id);
        EstadoIncidencia estadoAnterior = incidencia.getEstado();

        incidencia.cambiarEstado(nuevoEstado);

        Incidencia actualizada = repositorioIncidencia.guardar(incidencia);

        repositorioHistorial.guardar(new HistorialEstadoIncidencia(
                UUID.randomUUID(),
                actualizada.getId(),
                estadoAnterior,
                actualizada.getEstado(),
                LocalDateTime.now()
        ));

        servicioNotificaciones.notificarCambioEstado(
                actualizada.getUsuarioId(),
                actualizada.getId(),
                estadoAnterior,
                actualizada.getEstado()
        );

        return actualizada;
    }
}