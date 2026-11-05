package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.Usuario;
import java.util.UUID;

public interface CambiarEstadoUsuario {

    Usuario cambiarEstado(UUID id, boolean activo);
}
