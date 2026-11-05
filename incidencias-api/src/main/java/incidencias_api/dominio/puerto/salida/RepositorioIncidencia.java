package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.Incidencia;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioIncidencia {

    Incidencia guardar(Incidencia incidencia);

    Optional<Incidencia> buscarPorId(UUID id);

    Optional<Incidencia> buscarPorIdYUsuarioId(UUID id, UUID usuarioId);

    List<Incidencia> listarTodas();

    List<Incidencia> listarPorUsuarioId(UUID usuarioId);

    List<Incidencia> listarConFiltros(
            EstadoIncidencia estado,
            UUID categoriaId
    );
}