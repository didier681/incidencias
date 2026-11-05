package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.HistorialEstadoEntidad;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialEstadoRepositorioJpa
        extends JpaRepository<HistorialEstadoEntidad, UUID> {

    List<HistorialEstadoEntidad>
            findAllByIncidencia_IdOrderByFechaCambioAsc(UUID incidenciaId);
}