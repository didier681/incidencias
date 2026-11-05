package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoUsuarioSolicitud(
        @NotNull Boolean activo) {
}