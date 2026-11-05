package incidencias_api.infraestructura.adaptador.salida.persistencia.entidad;

import incidencias_api.dominio.modelo.EstadoSolicitudServicio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "solicitudes_servicio")
public class SolicitudServicioEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    private IncidenciaEntidad incidencia;

    @Column(name = "tipo_servicio", nullable = false, length = 100)
    private String tipoServicio;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoSolicitudServicio estado;

    protected SolicitudServicioEntidad() {
    }

    public SolicitudServicioEntidad(
            IncidenciaEntidad incidencia,
            String tipoServicio,
            String descripcion,
            LocalDateTime fechaSolicitud,
            EstadoSolicitudServicio estado) {
        this.incidencia = incidencia;
        this.tipoServicio = tipoServicio;
        this.descripcion = descripcion;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public IncidenciaEntidad getIncidencia() {
        return incidencia;
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

    public void setEstado(EstadoSolicitudServicio estado) {
        this.estado = estado;
    }
}