package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.CategoriaEntidad;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorioJpa
        extends JpaRepository<CategoriaEntidad, UUID> {
}