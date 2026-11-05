package incidencias_api.infraestructura.adaptador.salida.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "categorias")
public class CategoriaEntidad {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private boolean activa;

    protected CategoriaEntidad() {
        // Constructor requerido por JPA.
    }

    public CategoriaEntidad(
            UUID id,
            String nombre,
            String descripcion,
            boolean activa) {
        this.id = id;
        this.nombre = nombre;
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
}
