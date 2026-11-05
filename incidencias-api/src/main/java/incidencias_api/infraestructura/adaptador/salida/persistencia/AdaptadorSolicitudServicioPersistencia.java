package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.SolicitudServicio;
import incidencias_api.dominio.puerto.salida.RepositorioSolicitudServicio;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.IncidenciaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.SolicitudServicioEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.IncidenciaRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.SolicitudServicioRepositorioJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdaptadorSolicitudServicioPersistencia
        implements RepositorioSolicitudServicio {

    private final SolicitudServicioRepositorioJpa solicitudJpa;
    private final IncidenciaRepositorioJpa incidenciaJpa;

    public AdaptadorSolicitudServicioPersistencia(
            SolicitudServicioRepositorioJpa solicitudJpa,
            IncidenciaRepositorioJpa incidenciaJpa) {
        this.solicitudJpa = solicitudJpa;
        this.incidenciaJpa = incidenciaJpa;
    }

    @Override
    @Transactional
    public SolicitudServicio guardar(SolicitudServicio solicitud) {
        SolicitudServicioEntidad entidad = solicitudJpa
                .findById(solicitud.getId())
                .orElse(null);

        if (entidad == null) {
            IncidenciaEntidad incidencia = incidenciaJpa
                    .findById(solicitud.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La incidencia asociada no existe."
                    ));

            entidad = new SolicitudServicioEntidad(
                    incidencia,
                    solicitud.getTipoServicio(),
                    solicitud.getDescripcion(),
                    solicitud.getFechaSolicitud(),
                    solicitud.getEstado()
            );
        } else {
            entidad.setEstado(solicitud.getEstado());
        }

        return convertirADominio(solicitudJpa.save(entidad));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SolicitudServicio> buscarPorId(UUID id) {
        return solicitudJpa.findById(id)
                .map(this::convertirADominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarPorIncidenciaId(UUID incidenciaId) {
        return solicitudJpa
                .findAllByIncidencia_IdOrderByFechaSolicitudDesc(incidenciaId)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private SolicitudServicio convertirADominio(
            SolicitudServicioEntidad entidad) {
        return new SolicitudServicio(
                entidad.getId(),
                entidad.getIncidencia().getId(),
                entidad.getTipoServicio(),
                entidad.getDescripcion(),
                entidad.getFechaSolicitud(),
                entidad.getEstado()
        );
    }
}