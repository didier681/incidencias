package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.AutenticarUsuario;
import incidencias_api.dominio.puerto.salida.CodificadorContrasena;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;

public class ServicioAutenticacionUsuarios implements AutenticarUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final CodificadorContrasena codificadorContrasena;

    public ServicioAutenticacionUsuarios(
            RepositorioUsuario repositorioUsuario,
            CodificadorContrasena codificadorContrasena) {
        this.repositorioUsuario = repositorioUsuario;
        this.codificadorContrasena = codificadorContrasena;
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        if (correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Correo y contraseña son obligatorios.");
        }

        Usuario usuario = repositorioUsuario.buscarPorCorreo(correo.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!usuario.isActivo()
                || !codificadorContrasena.coincide(
                        contrasena,
                        usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }

        return usuario;
    }
}