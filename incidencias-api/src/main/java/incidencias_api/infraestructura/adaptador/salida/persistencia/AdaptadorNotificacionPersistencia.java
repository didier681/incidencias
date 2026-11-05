package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.Notificacion;
import incidencias_api.dominio.puerto.salida.RepositorioNotificacion;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.IncidenciaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.NotificacionEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.UsuarioEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.IncidenciaRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.NotificacionRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.UsuarioRepositorioJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdaptadorNotificacionPersistencia
        implements RepositorioNotificacion {

    private final NotificacionRepositorioJpa notificacionJpa;
    private final UsuarioRepositorioJpa usuarioJpa;
    private final IncidenciaRepositorioJpa incidenciaJpa;

    public AdaptadorNotificacionPersistencia(
            NotificacionRepositorioJpa notificacionJpa,
            UsuarioRepositorioJpa usuarioJpa,
            IncidenciaRepositorioJpa incidenciaJpa) {
        this.notificacionJpa = notificacionJpa;
        this.usuarioJpa = usuarioJpa;
        this.incidenciaJpa = incidenciaJpa;
    }

    @Override
    @Transactional
    public Notificacion guardar(Notificacion notificacion) {
        NotificacionEntidad entidad = notificacionJpa
                .findById(notificacion.getId())
                .orElse(null);

        if (entidad == null) {
            UsuarioEntidad usuario = usuarioJpa
                    .findById(notificacion.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El usuario de la notificación no existe."
                    ));

            IncidenciaEntidad incidencia = incidenciaJpa
                    .findById(notificacion.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La incidencia de la notificación no existe."
                    ));

            entidad = new NotificacionEntidad(
                    usuario,
                    incidencia,
                    notificacion.getMensaje(),
                    notificacion.getFechaCreacion(),
                    notificacion.isLeida()
            );
        } else {
            entidad.setLeida(notificacion.isLeida());
        }

        return convertirADominio(notificacionJpa.save(entidad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuarioId(UUID usuarioId) {
        return notificacionJpa
                .findAllByUsuario_IdOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notificacion> buscarPorIdYUsuarioId(
            UUID id,
            UUID usuarioId) {
        return notificacionJpa.findByIdAndUsuario_Id(id, usuarioId)
                .map(this::convertirADominio);
    }

    private Notificacion convertirADominio(NotificacionEntidad entidad) {
        return new Notificacion(
                entidad.getId(),
                entidad.getUsuario().getId(),
                entidad.getIncidencia().getId(),
                entidad.getMensaje(),
                entidad.getFechaCreacion(),
                entidad.isLeida()
        );
    }
}