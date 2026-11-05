package incidencias_api.dominio.puerto.entrada;

import incidencias_api.dominio.modelo.Usuario;

public interface RegistrarUsuario {

    Usuario registrar(String nombre, String correo, String contrasena);
}