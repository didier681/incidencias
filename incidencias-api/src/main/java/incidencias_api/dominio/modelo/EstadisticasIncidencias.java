package incidencias_api.dominio.modelo;

public record EstadisticasIncidencias(
        long totalIncidencias,
        long reportadas,
        long enRevision,
        long enProceso,
        long resueltas,
        long rechazadas,
        double tiempoPromedioResolucionHoras) {
}