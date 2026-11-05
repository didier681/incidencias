package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.Incidencia;
import incidencias_api.dominio.puerto.salida.RepositorioIncidencia;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.CategoriaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.IncidenciaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.UsuarioEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.CategoriaRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.IncidenciaRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.UsuarioRepositorioJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdaptadorIncidenciaPersistencia implements RepositorioIncidencia {

    private final IncidenciaRepositorioJpa incidenciaJpa;
    private final UsuarioRepositorioJpa usuarioJpa;
    private final CategoriaRepositorioJpa categoriaJpa;

    public AdaptadorIncidenciaPersistencia(
            IncidenciaRepositorioJpa incidenciaJpa,
            UsuarioRepositorioJpa usuarioJpa,
            CategoriaRepositorioJpa categoriaJpa) {
        this.incidenciaJpa = incidenciaJpa;
        this.usuarioJpa = usuarioJpa;
        this.categoriaJpa = categoriaJpa;
    }

    @Override
    @Transactional
    public Incidencia guardar(Incidencia incidencia) {
        UsuarioEntidad usuario = usuarioJpa.findById(incidencia.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no existe."
                ));

        CategoriaEntidad categoria = categoriaJpa.findById(incidencia.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoría no existe."
                ));

        IncidenciaEntidad entidad = incidenciaJpa.findById(incidencia.getId())
                .orElse(null);

        if (entidad == null) {
            entidad = new IncidenciaEntidad(
                    incidencia.getTitulo(),
                    incidencia.getDescripcion(),
                    incidencia.getUbicacion(),
                    incidencia.getEstado(),
                    incidencia.getFechaReporte(),
                    usuario,
                    categoria
            );
        } else {
            entidad.setTitulo(incidencia.getTitulo());
            entidad.setDescripcion(incidencia.getDescripcion());
            entidad.setUbicacion(incidencia.getUbicacion());
            entidad.setEstado(incidencia.getEstado());
            entidad.setCategoria(categoria);
        }

        IncidenciaEntidad guardada = incidenciaJpa.save(entidad);
        return convertirADominio(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Incidencia> buscarPorId(UUID id) {
        return incidenciaJpa.findById(id)
                .map(this::convertirADominio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Incidencia> buscarPorIdYUsuarioId(UUID id, UUID usuarioId) {
        return incidenciaJpa.findByIdAndUsuario_Id(id, usuarioId)
                .map(this::convertirADominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidencia> listarTodas() {
        return incidenciaJpa.findAll()
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidencia> listarPorUsuarioId(UUID usuarioId) {
        return incidenciaJpa.findAllByUsuario_Id(usuarioId)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incidencia> listarConFiltros(
            EstadoIncidencia estado,
            UUID categoriaId) {
        return incidenciaJpa.buscarConFiltros(estado, categoriaId)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private Incidencia convertirADominio(IncidenciaEntidad entidad) {
        return new Incidencia(
                entidad.getId(),
                entidad.getUsuario().getId(),
                entidad.getCategoria().getId(),
                entidad.getTitulo(),
                entidad.getDescripcion(),
                entidad.getUbicacion(),
                entidad.getEstado(),
                entidad.getFechaReporte()
        );
    }
}