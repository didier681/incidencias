package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.aplicacion.servicio.ServicioGestionIncidencias;
import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.HistorialEstadoIncidencia;
import incidencias_api.dominio.modelo.Incidencia;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CambiarEstadoIncidenciaSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.HistorialEstadoRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.IncidenciaDetalleRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.IncidenciaRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.IncidenciaSolicitud;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaControlador {

    private final ServicioGestionIncidencias servicio;

    public IncidenciaControlador(ServicioGestionIncidencias servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidenciaRespuesta crear(
            @Valid @RequestBody IncidenciaSolicitud solicitud,
            @AuthenticationPrincipal Jwt jwt) {

        UUID usuarioId = UUID.fromString(jwt.getSubject());

        Incidencia incidencia = servicio.crear(
                usuarioId,
                solicitud.categoriaId(),
                solicitud.titulo(),
                solicitud.descripcion(),
                solicitud.ubicacion()
        );

        return convertirRespuesta(incidencia);
    }

    @GetMapping
    public List<IncidenciaRespuesta> listarPropias(
            @AuthenticationPrincipal Jwt jwt) {

        UUID usuarioId = UUID.fromString(jwt.getSubject());

        return servicio.listarPorUsuarioId(usuarioId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @GetMapping("/administracion")
    public List<IncidenciaRespuesta> listarTodas(
            @RequestParam(required = false) EstadoIncidencia estado,
            @RequestParam(required = false) UUID categoriaId) {

        return servicio.listarConFiltros(estado, categoriaId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @GetMapping("/{id}")
    public IncidenciaDetalleRespuesta buscarPorId(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        UUID usuarioId = UUID.fromString(jwt.getSubject());

        Incidencia incidencia =
                servicio.buscarPorIdYUsuarioId(id, usuarioId);

        List<HistorialEstadoRespuesta> historial = servicio
                .listarHistorialEstados(id)
                .stream()
                .map(this::convertirHistorialRespuesta)
                .toList();

        return new IncidenciaDetalleRespuesta(
                incidencia.getId(),
                incidencia.getUsuarioId(),
                incidencia.getCategoriaId(),
                incidencia.getTitulo(),
                incidencia.getDescripcion(),
                incidencia.getUbicacion(),
                incidencia.getEstado(),
                incidencia.getFechaReporte(),
                historial
        );
    }

    @PutMapping("/{id}/estado")
    public IncidenciaRespuesta cambiarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody CambiarEstadoIncidenciaSolicitud solicitud) {

        Incidencia incidencia =
                servicio.cambiarEstado(id, solicitud.estado());

        return convertirRespuesta(incidencia);
    }

    private IncidenciaRespuesta convertirRespuesta(Incidencia incidencia) {
        return new IncidenciaRespuesta(
                incidencia.getId(),
                incidencia.getUsuarioId(),
                incidencia.getCategoriaId(),
                incidencia.getTitulo(),
                incidencia.getDescripcion(),
                incidencia.getUbicacion(),
                incidencia.getEstado(),
                incidencia.getFechaReporte()
        );
    }

    private HistorialEstadoRespuesta convertirHistorialRespuesta(
            HistorialEstadoIncidencia historial) {
        return new HistorialEstadoRespuesta(
                historial.getId(),
                historial.getIncidenciaId(),
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                historial.getFechaCambio()
        );
    }
}