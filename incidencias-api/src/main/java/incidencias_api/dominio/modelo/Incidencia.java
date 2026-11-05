package incidencias_api.dominio.modelo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Incidencia {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID categoriaId;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private EstadoIncidencia estado;
    private final LocalDateTime fechaReporte;

    public Incidencia(
            UUID id,
            UUID usuarioId,
            UUID categoriaId,
            String titulo,
            String descripcion,
            String ubicacion,
            EstadoIncidencia estado,
            LocalDateTime fechaReporte) {
        if (id == null || usuarioId == null || categoriaId == null) {
            throw new IllegalArgumentException("Los identificadores son obligatorios.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (estado == null || fechaReporte == null) {
            throw new IllegalArgumentException("El estado y la fecha son obligatorios.");
        }

        this.id = id;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.titulo = titulo.trim();
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void cambiarEstado(EstadoIncidencia nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio.");
        }
        this.estado = nuevoEstado;
    }
}