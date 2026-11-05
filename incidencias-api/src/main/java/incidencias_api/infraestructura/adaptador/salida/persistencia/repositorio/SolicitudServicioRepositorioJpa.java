package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.SolicitudServicioEntidad;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudServicioRepositorioJpa
        extends JpaRepository<SolicitudServicioEntidad, UUID> {

    List<SolicitudServicioEntidad>
            findAllByIncidencia_IdOrderByFechaSolicitudDesc(UUID incidenciaId);
}