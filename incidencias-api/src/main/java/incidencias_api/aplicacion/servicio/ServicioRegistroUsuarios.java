package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.RolUsuario;
import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.RegistrarUsuario;
import incidencias_api.dominio.puerto.salida.CodificadorContrasena;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import java.util.Locale;
import java.util.UUID;

public class ServicioRegistroUsuarios implements RegistrarUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final CodificadorContrasena codificadorContrasena;

    public ServicioRegistroUsuarios(
            RepositorioUsuario repositorioUsuario,
            CodificadorContrasena codificadorContrasena) {
        this.repositorioUsuario = repositorioUsuario;
        this.codificadorContrasena = codificadorContrasena;
    }

    @Override
    public Usuario registrar(String nombre, String correo, String contrasena) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (correo == null || correo.isBlank() || !correo.contains("@")) {
            throw new IllegalArgumentException("El correo no es válido.");
        }
        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres.");
        }

        String correoNormalizado = correo.trim().toLowerCase(Locale.ROOT);

        if (repositorioUsuario.existePorCorreo(correoNormalizado)) {
            throw new IllegalArgumentException(
                    "Ya existe una cuenta con ese correo.");
        }

        String hash = codificadorContrasena.codificar(contrasena);

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                nombre,
                correoNormalizado,
                hash,
                RolUsuario.USUARIO,
                true
        );

        return repositorioUsuario.guardar(usuario);
    }
}