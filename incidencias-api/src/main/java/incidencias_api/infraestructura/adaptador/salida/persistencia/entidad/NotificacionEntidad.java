package incidencias_api.infraestructura.adaptador.salida.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
public class NotificacionEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntidad usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    private IncidenciaEntidad incidencia;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private boolean leida;

    protected NotificacionEntidad() {
    }

    public NotificacionEntidad(
            UsuarioEntidad usuario,
            IncidenciaEntidad incidencia,
            String mensaje,
            LocalDateTime fechaCreacion,
            boolean leida) {
        this.usuario = usuario;
        this.incidencia = incidencia;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.leida = leida;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public IncidenciaEntidad getIncidencia() {
        return incidencia;
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

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}