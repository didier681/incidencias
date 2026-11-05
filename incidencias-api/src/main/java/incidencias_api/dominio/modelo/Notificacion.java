package incidencias_api.dominio.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notificacion {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID incidenciaId;
    private final String mensaje;
    private final LocalDateTime fechaCreacion;
    private boolean leida;

    public Notificacion(
            UUID id,
            UUID usuarioId,
            UUID incidenciaId,
            String mensaje,
            LocalDateTime fechaCreacion,
            boolean leida) {
        if (id == null || usuarioId == null || incidenciaId == null) {
            throw new IllegalArgumentException(
                    "Los identificadores son obligatorios."
            );
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje es obligatorio.");
        }
        if (fechaCreacion == null) {
            throw new IllegalArgumentException(
                    "La fecha de creación es obligatoria."
            );
        }

        this.id = id;
        this.usuarioId = usuarioId;
        this.incidenciaId = incidenciaId;
        this.mensaje = mensaje.trim();
        this.fechaCreacion = fechaCreacion;
        this.leida = leida;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getIncidenciaId() {
        return incidenciaId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isLeida() {
        return leida;
    }

    public void marcarComoLeida() {
        this.leida = true;
    }
}