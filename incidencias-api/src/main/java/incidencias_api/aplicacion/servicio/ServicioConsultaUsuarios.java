package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.ConsultarUsuarios;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class ServicioConsultaUsuarios implements ConsultarUsuarios {

    private final RepositorioUsuario repositorioUsuario;

    public ServicioConsultaUsuarios(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repositorioUsuario.listarTodos();
    }
}