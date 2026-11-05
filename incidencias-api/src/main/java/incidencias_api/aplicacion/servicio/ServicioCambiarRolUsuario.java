package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.RolUsuario;
import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.CambiarRolUsuario;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class ServicioCambiarRolUsuario implements CambiarRolUsuario {

    private final RepositorioUsuario repositorioUsuario;

    public ServicioCambiarRolUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional
    public Usuario cambiarRol(UUID id, RolUsuario nuevoRol) {
        Usuario usuario = repositorioUsuario.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        usuario.cambiarRol(nuevoRol);
        return repositorioUsuario.guardar(usuario);
    }
}