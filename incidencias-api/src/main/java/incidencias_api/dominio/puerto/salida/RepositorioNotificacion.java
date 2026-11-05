package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.Notificacion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioNotificacion {

    Notificacion guardar(Notificacion notificacion);

    List<Notificacion> listarPorUsuarioId(UUID usuarioId);

    Optional<Notificacion> buscarPorIdYUsuarioId(
            UUID id,
            UUID usuarioId
    );
}
