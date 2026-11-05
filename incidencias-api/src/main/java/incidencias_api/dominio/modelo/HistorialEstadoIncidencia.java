package incidencias_api.dominio.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class HistorialEstadoIncidencia {

    private final UUID id;
    private final UUID incidenciaId;
    private final EstadoIncidencia estadoAnterior;
    private final EstadoIncidencia estadoNuevo;
    private final LocalDateTime fechaCambio;

    public HistorialEstadoIncidencia(
            UUID id,
            UUID incidenciaId,
            EstadoIncidencia estadoAnterior,
            EstadoIncidencia estadoNuevo,
            LocalDateTime fechaCambio) {
        if (id == null || incidenciaId == null) {
            throw new IllegalArgumentException(
                    "Los identificadores son obligatorios."
            );
        }
        if (estadoNuevo == null || fechaCambio == null) {
            throw new IllegalArgumentException(
                    "El nuevo estado y la fecha son obligatorios."
            );
        }

        this.id = id;
        this.incidenciaId = incidenciaId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIncidenciaId() {
        return incidenciaId;
    }

    public EstadoIncidencia getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoIncidencia getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }
}