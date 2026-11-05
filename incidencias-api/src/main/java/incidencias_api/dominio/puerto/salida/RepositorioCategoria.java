package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.CategoriaIncidencia;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioCategoria {

    CategoriaIncidencia guardar(CategoriaIncidencia categoria);

    Optional<CategoriaIncidencia> buscarPorId(UUID id);

    List<CategoriaIncidencia> listarTodas();
}