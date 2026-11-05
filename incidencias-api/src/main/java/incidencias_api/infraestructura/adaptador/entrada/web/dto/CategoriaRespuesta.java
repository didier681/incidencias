package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import java.util.UUID;

public record CategoriaRespuesta(
        UUID id,
        String nombre,
        String descripcion,
        boolean activa
) {
}