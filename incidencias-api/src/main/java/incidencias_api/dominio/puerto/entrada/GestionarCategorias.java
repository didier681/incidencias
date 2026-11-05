package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.CategoriaIncidencia;
import java.util.List;
import java.util.UUID;

public interface GestionarCategorias {

    CategoriaIncidencia crear(String nombre, String descripcion);

    CategoriaIncidencia actualizar(
            UUID id,
            String nombre,
            String descripcion);

    CategoriaIncidencia buscarPorId(UUID id);

    List<CategoriaIncidencia> listarTodas();

    void desactivar(UUID id);
}
