package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record IncidenciaSolicitud(
        @NotNull UUID categoriaId,
        @NotBlank @Size(max = 120) String titulo,
        @Size(max = 2000) String descripcion,
        @Size(max = 250) String ubicacion) {
}