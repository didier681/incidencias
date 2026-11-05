package incidencias_api.infraestructura.adaptador.entrada.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitudServicioSolicitud(
        @NotBlank(message = "El tipo de servicio es obligatorio.")
        @Size(max = 100, message = "El tipo de servicio admite máximo 100 caracteres.")
        String tipoServicio,

        @Size(max = 500, message = "La descripción admite máximo 500 caracteres.")
        String descripcion) {
}