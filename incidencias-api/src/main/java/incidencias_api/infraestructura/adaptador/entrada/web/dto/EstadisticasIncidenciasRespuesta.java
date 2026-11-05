package incidencias_api.infraestructura.adaptador.entrada.web.dto;

public record EstadisticasIncidenciasRespuesta(
        long totalIncidencias,
        long reportadas,
        long enRevision,
        long enProceso,
        long resueltas,
        long rechazadas,
        double tiempoPromedioResolucionHoras) {
}
