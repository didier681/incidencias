package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.EstadoSolicitudServicio;
import incidencias_api.dominio.modelo.SolicitudServicio;
import incidencias_api.dominio.puerto.salida.RepositorioIncidencia;
import incidencias_api.dominio.puerto.salida.RepositorioSolicitudServicio;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioGestionSolicitudes {

    private final RepositorioSolicitudServicio repositorioSolicitud;
    private final RepositorioIncidencia repositorioIncidencia;

    public ServicioGestionSolicitudes(
            RepositorioSolicitudServicio repositorioSolicitud,
            RepositorioIncidencia repositorioIncidencia) {
        this.repositorioSolicitud = repositorioSolicitud;
        this.repositorioIncidencia = repositorioIncidencia;
    }

    @Transactional
    public SolicitudServicio crear(
            UUID incidenciaId,
            String tipoServicio,
            String descripcion) {
        repositorioIncidencia.buscarPorId(incidenciaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La incidencia no existe."
                ));

        SolicitudServicio solicitud = new SolicitudServicio(
                UUID.randomUUID(),
                incidenciaId,
                tipoServicio,
                descripcion,
                LocalDateTime.now(),
                EstadoSolicitudServicio.PENDIENTE
        );

        return repositorioSolicitud.guardar(solicitud);
    }

    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarPorIncidenciaId(UUID incidenciaId) {
        repositorioIncidencia.buscarPorId(incidenciaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La incidencia no existe."
                ));

        return repositorioSolicitud.listarPorIncidenciaId(incidenciaId);
    }

    @Transactional
    public SolicitudServicio cambiarEstado(
            UUID solicitudId,
            EstadoSolicitudServicio nuevoEstado) {
        SolicitudServicio solicitud = repositorioSolicitud
                .buscarPorId(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La solicitud de servicio no existe."
                ));

        solicitud.cambiarEstado(nuevoEstado);
        return repositorioSolicitud.guardar(solicitud);
    }
}