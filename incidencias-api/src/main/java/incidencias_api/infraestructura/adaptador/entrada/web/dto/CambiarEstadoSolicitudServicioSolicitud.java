package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.EstadoSolicitudServicio;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoSolicitudServicioSolicitud(
        @NotNull(message = "El estado es obligatorio.")
        EstadoSolicitudServicio estado) {
}