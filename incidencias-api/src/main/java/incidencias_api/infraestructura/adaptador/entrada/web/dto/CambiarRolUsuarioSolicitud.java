package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.RolUsuario;
import jakarta.validation.constraints.NotNull;

public record CambiarRolUsuarioSolicitud(
        @NotNull RolUsuario rol) {
}