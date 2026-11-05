package incidencias_api.aplicacion.servicio;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.dominio.modelo.EstadisticasIncidencias;
import incidencias_api.dominio.modelo.HistorialEstadoIncidencia;
import incidencias_api.dominio.modelo.Incidencia;
import incidencias_api.dominio.puerto.salida.RepositorioHistorialEstadoIncidencia;
import incidencias_api.dominio.puerto.salida.RepositorioIncidencia;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioEstadisticasIncidencias {

    private final RepositorioIncidencia repositorioIncidencia;
    private final RepositorioHistorialEstadoIncidencia repositorioHistorial;

    public ServicioEstadisticasIncidencias(
            RepositorioIncidencia repositorioIncidencia,
            RepositorioHistorialEstadoIncidencia repositorioHistorial) {
        this.repositorioIncidencia = repositorioIncidencia;
        this.repositorioHistorial = repositorioHistorial;
    }

    @Transactional(readOnly = true)
    public EstadisticasIncidencias obtenerEstadisticas() {
        List<Incidencia> incidencias = repositorioIncidencia.listarTodas();

        long reportadas = contarPorEstado(
                incidencias,
                EstadoIncidencia.REPORTADA
        );
        long enRevision = contarPorEstado(
                incidencias,
                EstadoIncidencia.EN_REVISION
        );
        long enProceso = contarPorEstado(
                incidencias,
                EstadoIncidencia.EN_PROCESO
        );
        long resueltas = contarPorEstado(
                incidencias,
                EstadoIncidencia.RESUELTA
        );
        long rechazadas = contarPorEstado(
                incidencias,
                EstadoIncidencia.RECHAZADA
        );

        double tiempoPromedio = calcularTiempoPromedioResolucion(incidencias);

        return new EstadisticasIncidencias(
                incidencias.size(),
                reportadas,
                enRevision,
                enProceso,
                resueltas,
                rechazadas,
                tiempoPromedio
        );
    }

    private long contarPorEstado(
            List<Incidencia> incidencias,
            EstadoIncidencia estado) {
        return incidencias.stream()
                .filter(incidencia -> incidencia.getEstado() == estado)
                .count();
    }

    private double calcularTiempoPromedioResolucion(
            List<Incidencia> incidencias) {
        long totalSegundos = 0;
        long cantidadResueltas = 0;

        for (Incidencia incidencia : incidencias) {
            List<HistorialEstadoIncidencia> historial = repositorioHistorial
                    .listarPorIncidenciaId(incidencia.getId());

            HistorialEstadoIncidencia primeraResolucion = historial.stream()
                    .filter(evento ->
                            evento.getEstadoNuevo() == EstadoIncidencia.RESUELTA)
                    .findFirst()
                    .orElse(null);

            if (primeraResolucion != null) {
                totalSegundos += Duration.between(
                        incidencia.getFechaReporte(),
                        primeraResolucion.getFechaCambio()
                ).getSeconds();
                cantidadResueltas++;
            }
        }

        if (cantidadResueltas == 0) {
            return 0.0;
        }

        double promedioHoras =
                (double) totalSegundos / cantidadResueltas / 3600.0;

        return Math.round(promedioHoras * 100.0) / 100.0;
    }
}