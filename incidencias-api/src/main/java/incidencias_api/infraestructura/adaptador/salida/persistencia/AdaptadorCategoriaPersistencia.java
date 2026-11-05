package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.CategoriaIncidencia;
import incidencias_api.dominio.puerto.salida.RepositorioCategoria;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.CategoriaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.CategoriaRepositorioJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class AdaptadorCategoriaPersistencia implements RepositorioCategoria {

    private final CategoriaRepositorioJpa repositorioJpa;

    public AdaptadorCategoriaPersistencia(CategoriaRepositorioJpa repositorioJpa) {
        this.repositorioJpa = repositorioJpa;
    }

    @Override
    public CategoriaIncidencia guardar(CategoriaIncidencia categoria) {
        CategoriaEntidad entidad = new CategoriaEntidad(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.isActiva()
        );

        CategoriaEntidad guardada = repositorioJpa.save(entidad);
        return convertirADominio(guardada);
    }

    @Override
    public java.util.Optional<CategoriaIncidencia> buscarPorId(UUID id) {
        return repositorioJpa.findById(id)
                .map(this::convertirADominio);
    }

    @Override
    public List<CategoriaIncidencia> listarTodas() {
        return repositorioJpa.findAll()
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private CategoriaIncidencia convertirADominio(CategoriaEntidad entidad) {
        return new CategoriaIncidencia(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.isActiva()
        );
    }
}