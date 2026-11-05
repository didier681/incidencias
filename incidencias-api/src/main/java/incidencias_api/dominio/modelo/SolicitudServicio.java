package incidencias_api.dominio.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class SolicitudServicio {

    private final UUID id;
    private final UUID incidenciaId;
    private final String tipoServicio;
    private final String descripcion;
    private final LocalDateTime fechaSolicitud;
    private EstadoSolicitudServicio estado;

    public SolicitudServicio(
            UUID id,
            UUID incidenciaId,
            String tipoServicio,
            String descripcion,
            LocalDateTime fechaSolicitud,
            EstadoSolicitudServicio estado) {
        if (id == null || incidenciaId == null) {
            throw new IllegalArgumentException(
                    "Los identificadores son obligatorios."
            );
        }
        if (tipoServicio == null || tipoServicio.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de servicio es obligatorio."
            );
        }
        if (fechaSolicitud == null || estado == null) {
            throw new IllegalArgumentException(
                    "La fecha y el estado son obligatorios."
            );
        }

        this.id = id;
        this.incidenciaId = incidenciaId;
        this.tipoServicio = tipoServicio.trim();
        this.descripcion = descripcion;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIncidenciaId() {
        return incidenciaId;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoSolicitudServicio getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoSolicitudServicio nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException(
                    "El nuevo estado es obligatorio."
            );
        }
        this.estado = nuevoEstado;
    }
}