package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.aplicacion.servicio.ServicioGestionNotificaciones;
import incidencias_api.dominio.modelo.Notificacion;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.NotificacionRespuesta;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionControlador {

    private final ServicioGestionNotificaciones servicio;

    public NotificacionControlador(ServicioGestionNotificaciones servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<NotificacionRespuesta> listarPropias(
            @AuthenticationPrincipal Jwt jwt) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());

        return servicio.listarPorUsuarioId(usuarioId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @PutMapping("/{id}/leida")
    public NotificacionRespuesta marcarComoLeida(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());

        Notificacion notificacion =
                servicio.marcarComoLeida(id, usuarioId);

        return convertirRespuesta(notificacion);
    }

    private NotificacionRespuesta convertirRespuesta(
            Notificacion notificacion) {
        return new NotificacionRespuesta(
                notificacion.getId(),
                notificacion.getIncidenciaId(),
                notificacion.getMensaje(),
                notificacion.getFechaCreacion(),
                notificacion.isLeida()
        );
    }
}