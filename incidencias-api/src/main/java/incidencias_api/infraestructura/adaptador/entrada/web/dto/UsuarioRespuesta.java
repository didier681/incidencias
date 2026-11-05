package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import incidencias_api.dominio.modelo.RolUsuario;
import java.util.UUID;

public record UsuarioRespuesta(
        UUID id,
        String nombre,
        String correo,
        RolUsuario rol,
        boolean activo
) {
}