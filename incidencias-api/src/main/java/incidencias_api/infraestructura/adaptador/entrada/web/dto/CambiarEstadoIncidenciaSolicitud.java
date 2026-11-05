package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoIncidenciaSolicitud(
        @NotNull EstadoIncidencia estado) {
}