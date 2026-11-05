package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.CambiarEstadoUsuario;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class ServicioCambiarEstadoUsuario implements CambiarEstadoUsuario {

    private final RepositorioUsuario repositorioUsuario;

    public ServicioCambiarEstadoUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional
    public Usuario cambiarEstado(UUID id, boolean activo) {
        Usuario usuario = repositorioUsuario.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        if (activo) {
            usuario.activar();
        } else {
            usuario.desactivar();
        }

        return repositorioUsuario.guardar(usuario);
    }
}