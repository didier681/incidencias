package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncidenciaRespuesta(
        UUID id,
        UUID usuarioId,
        UUID categoriaId,
        String titulo,
        String descripcion,
        String ubicacion,
        EstadoIncidencia estado,
        LocalDateTime fechaReporte) {
}