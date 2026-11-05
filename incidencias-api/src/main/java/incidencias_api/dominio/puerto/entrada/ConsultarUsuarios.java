package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.Usuario;
import java.util.List;

public interface ConsultarUsuarios {

    List<Usuario> listarTodos();
}
