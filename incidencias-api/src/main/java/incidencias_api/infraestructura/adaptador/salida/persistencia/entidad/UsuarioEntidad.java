package incidencias_api.infraestructura.adaptador.salida.persistencia.entidad;

import incidencias_api.dominio.modelo.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class UsuarioEntidad {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 180)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RolUsuario rol;

    @Column(nullable = false)
    private boolean activo;

    protected UsuarioEntidad() {
        // Constructor requerido por JPA.
    }

    public UsuarioEntidad(
            UUID id,
            String nombre,
            String correo,
            String passwordHash,
            RolUsuario rol,
            boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
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
}