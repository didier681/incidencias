package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.NotificacionEntidad;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepositorioJpa
        extends JpaRepository<NotificacionEntidad, UUID> {

    List<NotificacionEntidad>
            findAllByUsuario_IdOrderByFechaCreacionDesc(UUID usuarioId);

    Optional<NotificacionEntidad> findByIdAndUsuario_Id(
            UUID id,
            UUID usuarioId
    );
}