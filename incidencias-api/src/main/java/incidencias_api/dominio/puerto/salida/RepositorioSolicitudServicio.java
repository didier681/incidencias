package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.SolicitudServicio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioSolicitudServicio {

    SolicitudServicio guardar(SolicitudServicio solicitud);

    Optional<SolicitudServicio> buscarPorId(UUID id);

    List<SolicitudServicio> listarPorIncidenciaId(UUID incidenciaId);
}
