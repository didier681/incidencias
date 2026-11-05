package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import java.time.LocalDateTime;
import java.util.UUID;

public record HistorialEstadoRespuesta(
        UUID id,
        UUID incidenciaId,
        EstadoIncidencia estadoAnterior,
        EstadoIncidencia estadoNuevo,
        LocalDateTime fechaCambio) {
}