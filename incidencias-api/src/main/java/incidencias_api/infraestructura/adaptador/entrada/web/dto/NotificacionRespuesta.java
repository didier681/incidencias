package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacionRespuesta(
        UUID id,
        UUID incidenciaId,
        String mensaje,
        LocalDateTime fechaCreacion,
        boolean leida) {
}