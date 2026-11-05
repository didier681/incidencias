package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.ConsultarPerfilUsuario;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public class ServicioConsultaPerfilUsuario implements ConsultarPerfilUsuario {

    private final RepositorioUsuario repositorioUsuario;

    public ServicioConsultaPerfilUsuario(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario consultarPorId(UUID id) {
        return repositorioUsuario.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));
    }
}