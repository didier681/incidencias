package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.aplicacion.servicio.ServicioEstadisticasIncidencias;
import incidencias_api.dominio.modelo.EstadisticasIncidencias;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.EstadisticasIncidenciasRespuesta;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasControlador {

    private final ServicioEstadisticasIncidencias servicio;

    public EstadisticasControlador(
            ServicioEstadisticasIncidencias servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/incidencias")
    public EstadisticasIncidenciasRespuesta obtenerEstadisticas() {
        EstadisticasIncidencias estadisticas =
                servicio.obtenerEstadisticas();

        return new EstadisticasIncidenciasRespuesta(
                estadisticas.totalIncidencias(),
                estadisticas.reportadas(),
                estadisticas.enRevision(),
                estadisticas.enProceso(),
                estadisticas.resueltas(),
                estadisticas.rechazadas(),
                estadisticas.tiempoPromedioResolucionHoras()
        );
    }
}