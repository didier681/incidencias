package incidencias_api.dominio.modelo;

import java.util.UUID;

public class CategoriaIncidencia {

    private final UUID id;
    private String nombre;
    private String descripcion;
    private boolean activa;

    public CategoriaIncidencia(
            UUID id,
            String nombre,
            String descripcion,
            boolean activa) {
        if (id == null) {
            throw new IllegalArgumentException("El id es obligatorio.");
        }
        validarNombre(nombre);

        this.id = id;
        this.nombre = nombre.trim();
        this.descripcion = descripcion;
        this.activa = activa;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void actualizar(String nombre, String descripcion) {
        validarNombre(nombre);
        this.nombre = nombre.trim();
        this.descripcion = descripcion;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
    }
}