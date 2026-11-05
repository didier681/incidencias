package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.CategoriaIncidencia;
import incidencias_api.dominio.puerto.entrada.GestionarCategorias;
import incidencias_api.dominio.puerto.salida.RepositorioCategoria;
import java.util.List;
import java.util.UUID;

public class ServicioGestionCategorias implements GestionarCategorias {

    private final RepositorioCategoria repositorioCategoria;

    public ServicioGestionCategorias(RepositorioCategoria repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    @Override
    public CategoriaIncidencia crear(String nombre, String descripcion) {
        CategoriaIncidencia categoria = new CategoriaIncidencia(
                UUID.randomUUID(),
                nombre,
                descripcion,
                true
        );
        return repositorioCategoria.guardar(categoria);
    }

    @Override
    public CategoriaIncidencia actualizar(
            UUID id,
            String nombre,
            String descripcion) {
        CategoriaIncidencia categoria = buscarPorId(id);
        categoria.actualizar(nombre, descripcion);
        return repositorioCategoria.guardar(categoria);
    }

    @Override
    public CategoriaIncidencia buscarPorId(UUID id) {
        return repositorioCategoria.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe la categoría: " + id));
    }

    @Override
    public List<CategoriaIncidencia> listarTodas() {
        return repositorioCategoria.listarTodas();
    }

    @Override
    public void desactivar(UUID id) {
        CategoriaIncidencia categoria = buscarPorId(id);
        categoria.desactivar();
        repositorioCategoria.guardar(categoria);
    }
}