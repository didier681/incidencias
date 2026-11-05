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
@Table(name = "incidencias")
public class IncidenciaEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 250)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoIncidencia estado;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntidad usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntidad categoria;

    protected IncidenciaEntidad() {
    }

    public IncidenciaEntidad(
            String titulo,
            String descripcion,
            String ubicacion,
            EstadoIncidencia estado,
            LocalDateTime fechaReporte,
            UsuarioEntidad usuario,
            CategoriaEntidad categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
        this.usuario = usuario;
        this.categoria = categoria;
    }

    public UUID getId() {
        return id;
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

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public CategoriaEntidad getCategoria() {
        return categoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public void setCategoria(CategoriaEntidad categoria) {
        this.categoria = categoria;
    }
}