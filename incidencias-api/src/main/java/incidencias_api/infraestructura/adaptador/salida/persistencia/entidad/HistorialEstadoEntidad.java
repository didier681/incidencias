package incidencias_api.infraestructura.adaptador.salida.persistencia.entidad;

import incidencias_api.dominio.modelo.EstadoIncidencia;
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
@Table(name = "historial_estados_incidencia")
public class HistorialEstadoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidencia_id", nullable = false)
    private IncidenciaEntidad incidencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 30)
    private EstadoIncidencia estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private EstadoIncidencia estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    protected HistorialEstadoEntidad() {
    }

    public HistorialEstadoEntidad(
            IncidenciaEntidad incidencia,
            EstadoIncidencia estadoAnterior,
            EstadoIncidencia estadoNuevo,
            LocalDateTime fechaCambio) {
        this.incidencia = incidencia;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public UUID getId() {
        return id;
    }

    public IncidenciaEntidad getIncidencia() {
        return incidencia;
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