package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.Notificacion;
import incidencias_api.dominio.puerto.salida.RepositorioNotificacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioGestionNotificaciones {

    private final RepositorioNotificacion repositorioNotificacion;

    public ServicioGestionNotificaciones(
            RepositorioNotificacion repositorioNotificacion) {
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Transactional
    public Notificacion notificarCambioEstado(
            UUID usuarioId,
            UUID incidenciaId,
            EstadoIncidencia estadoAnterior,
            EstadoIncidencia estadoNuevo) {
        String mensaje = "El estado de tu incidencia cambió de "
                + estadoAnterior + " a " + estadoNuevo + ".";

        Notificacion notificacion = new Notificacion(
                UUID.randomUUID(),
                usuarioId,
                incidenciaId,
                mensaje,
                LocalDateTime.now(),
                false
        );

        return repositorioNotificacion.guardar(notificacion);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuarioId(UUID usuarioId) {
        return repositorioNotificacion.listarPorUsuarioId(usuarioId);
    }

    @Transactional
    public Notificacion marcarComoLeida(UUID notificacionId, UUID usuarioId) {
        Notificacion notificacion = repositorioNotificacion
                .buscarPorIdYUsuarioId(notificacionId, usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La notificación no existe."
                ));

        notificacion.marcarComoLeida();
        return repositorioNotificacion.guardar(notificacion);
    }
}