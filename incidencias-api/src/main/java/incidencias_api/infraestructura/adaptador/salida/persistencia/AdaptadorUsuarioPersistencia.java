package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.UsuarioEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.UsuarioRepositorioJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class AdaptadorUsuarioPersistencia implements RepositorioUsuario {

    private final UsuarioRepositorioJpa repositorioJpa;

    public AdaptadorUsuarioPersistencia(UsuarioRepositorioJpa repositorioJpa) {
        this.repositorioJpa = repositorioJpa;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntidad entidad = new UsuarioEntidad(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getPasswordHash(),
                usuario.getRol(),
                usuario.isActivo()
        );

        return convertirADominio(repositorioJpa.save(entidad));
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repositorioJpa.findById(id).map(this::convertirADominio);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repositorioJpa.findByCorreoIgnoreCase(correo.trim())
                .map(this::convertirADominio);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repositorioJpa.findAll()
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return repositorioJpa.existsByCorreoIgnoreCase(correo.trim());
    }

    private Usuario convertirADominio(UsuarioEntidad entidad) {
        return new Usuario(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getCorreo(),
                entidad.getPasswordHash(),
                entidad.getRol(),
                entidad.isActivo()
        );
    }
}