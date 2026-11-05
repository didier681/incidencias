package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.HistorialEstadoIncidencia;
import java.util.List;
import java.util.UUID;

public interface RepositorioHistorialEstadoIncidencia {

    HistorialEstadoIncidencia guardar(HistorialEstadoIncidencia historial);

    List<HistorialEstadoIncidencia> listarPorIncidenciaId(UUID incidenciaId);
}