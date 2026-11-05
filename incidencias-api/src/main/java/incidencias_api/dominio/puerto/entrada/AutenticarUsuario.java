package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.Usuario;

public interface AutenticarUsuario {

    Usuario autenticar(String correo, String contrasena);
}