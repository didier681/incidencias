package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.EstadoSolicitudServicio;
import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitudServicioRespuesta(
        UUID id,
        UUID incidenciaId,
        String tipoServicio,
        String descripcion,
        LocalDateTime fechaSolicitud,
        EstadoSolicitudServicio estado) {
}