package incidencias_api.infraestructura.adaptador.salida.persistencia;

import incidencias_api.dominio.modelo.HistorialEstadoIncidencia;
import incidencias_api.dominio.puerto.salida.RepositorioHistorialEstadoIncidencia;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.HistorialEstadoEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.IncidenciaEntidad;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.HistorialEstadoRepositorioJpa;
import incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio.IncidenciaRepositorioJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdaptadorHistorialEstadoPersistencia
        implements RepositorioHistorialEstadoIncidencia {

    private final HistorialEstadoRepositorioJpa historialJpa;
    private final IncidenciaRepositorioJpa incidenciaJpa;

    public AdaptadorHistorialEstadoPersistencia(
            HistorialEstadoRepositorioJpa historialJpa,
            IncidenciaRepositorioJpa incidenciaJpa) {
        this.historialJpa = historialJpa;
        this.incidenciaJpa = incidenciaJpa;
    }

    @Override
    @Transactional
    public HistorialEstadoIncidencia guardar(
            HistorialEstadoIncidencia historial) {
        IncidenciaEntidad incidencia = incidenciaJpa
                .findById(historial.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La incidencia del historial no existe."
                ));

        HistorialEstadoEntidad entidad = new HistorialEstadoEntidad(
                incidencia,
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                historial.getFechaCambio()
        );

        return convertirADominio(historialJpa.save(entidad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialEstadoIncidencia> listarPorIncidenciaId(
            UUID incidenciaId) {
        return historialJpa
                .findAllByIncidencia_IdOrderByFechaCambioAsc(incidenciaId)
                .stream()
                .map(this::convertirADominio)
                .toList();
    }

    private HistorialEstadoIncidencia convertirADominio(
            HistorialEstadoEntidad entidad) {
        return new HistorialEstadoIncidencia(
                entidad.getId(),
                entidad.getIncidencia().getId(),
                entidad.getEstadoAnterior(),
                entidad.getEstadoNuevo(),
                entidad.getFechaCambio()
        );
    }
}