package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.aplicacion.servicio.ServicioGestionIncidencias;
import incidencias_api.aplicacion.servicio.ServicioGestionSolicitudes;
import incidencias_api.dominio.modelo.SolicitudServicio;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CambiarEstadoSolicitudServicioSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.SolicitudServicioRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.SolicitudServicioSolicitud;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidencias/{incidenciaId}/solicitudes-servicio")
public class SolicitudServicioControlador {

    private final ServicioGestionSolicitudes servicioSolicitudes;
    private final ServicioGestionIncidencias servicioIncidencias;

    public SolicitudServicioControlador(
            ServicioGestionSolicitudes servicioSolicitudes,
            ServicioGestionIncidencias servicioIncidencias) {
        this.servicioSolicitudes = servicioSolicitudes;
        this.servicioIncidencias = servicioIncidencias;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudServicioRespuesta crear(
            @PathVariable UUID incidenciaId,
            @Valid @RequestBody SolicitudServicioSolicitud solicitud,
            @AuthenticationPrincipal Jwt jwt) {

        UUID usuarioId = UUID.fromString(jwt.getSubject());

        servicioIncidencias.buscarPorIdYUsuarioId(incidenciaId, usuarioId);

        SolicitudServicio creada = servicioSolicitudes.crear(
                incidenciaId,
                solicitud.tipoServicio(),
                solicitud.descripcion()
        );

        return convertirRespuesta(creada);
    }

    @GetMapping
    public List<SolicitudServicioRespuesta> listarPorIncidencia(
            @PathVariable UUID incidenciaId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID usuarioId = UUID.fromString(jwt.getSubject());

        servicioIncidencias.buscarPorIdYUsuarioId(incidenciaId, usuarioId);

        return servicioSolicitudes.listarPorIncidenciaId(incidenciaId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @PutMapping("/{solicitudId}/estado")
    public SolicitudServicioRespuesta cambiarEstado(
            @PathVariable UUID incidenciaId,
            @PathVariable UUID solicitudId,
            @Valid @RequestBody CambiarEstadoSolicitudServicioSolicitud solicitud) {

        boolean perteneceALaIncidencia = servicioSolicitudes
                .listarPorIncidenciaId(incidenciaId)
                .stream()
                .anyMatch(item -> item.getId().equals(solicitudId));

        if (!perteneceALaIncidencia) {
            throw new IllegalArgumentException(
                    "La solicitud no pertenece a esa incidencia."
            );
        }

        SolicitudServicio actualizada = servicioSolicitudes.cambiarEstado(
                solicitudId,
                solicitud.estado()
        );

        return convertirRespuesta(actualizada);
    }

    private SolicitudServicioRespuesta convertirRespuesta(
            SolicitudServicio solicitud) {
        return new SolicitudServicioRespuesta(
                solicitud.getId(),
                solicitud.getIncidenciaId(),
                solicitud.getTipoServicio(),
                solicitud.getDescripcion(),
                solicitud.getFechaSolicitud(),
                solicitud.getEstado()
        );
    }
}