package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.RolUsuario;
import incidencias_api.dominio.modelo.Usuario;
import java.util.UUID;

public interface CambiarRolUsuario {

    Usuario cambiarRol(UUID id, RolUsuario nuevoRol);
}