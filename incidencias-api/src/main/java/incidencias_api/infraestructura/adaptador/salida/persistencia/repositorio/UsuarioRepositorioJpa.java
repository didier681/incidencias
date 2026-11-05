package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.UsuarioEntidad;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorioJpa
        extends JpaRepository<UsuarioEntidad, UUID> {

    Optional<UsuarioEntidad> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);
}