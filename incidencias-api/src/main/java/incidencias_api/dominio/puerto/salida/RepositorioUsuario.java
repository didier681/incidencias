package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioUsuario {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorCorreo(String correo);

    List<Usuario> listarTodos();

    boolean existePorCorreo(String correo);
}