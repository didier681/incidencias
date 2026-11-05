package incidencias_api.dominio.modelo;

import java.util.UUID;

public class Usuario {

    private final UUID id;
    private String nombre;
    private String correo;
    private String passwordHash;
    private RolUsuario rol;
    private boolean activo;

    public Usuario(
            UUID id,
            String nombre,
            String correo,
            String passwordHash,
            RolUsuario rol,
            boolean activo) {
        if (id == null) {
            throw new IllegalArgumentException("El id es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (correo == null || correo.isBlank() || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo no es válido.");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("El hash de contraseña es obligatorio.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }

        this.id = id;
        this.nombre = nombre.trim();
        this.correo = correo.trim().toLowerCase();
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void actualizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void cambiarPasswordHash(String nuevoPasswordHash) {
        if (nuevoPasswordHash == null || nuevoPasswordHash.isBlank()) {
            throw new IllegalArgumentException("El hash de contraseña es obligatorio.");
        }
        this.passwordHash = nuevoPasswordHash;
    }

    public void cambiarRol(RolUsuario nuevoRol) {
        if (nuevoRol == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
        this.rol = nuevoRol;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }
}