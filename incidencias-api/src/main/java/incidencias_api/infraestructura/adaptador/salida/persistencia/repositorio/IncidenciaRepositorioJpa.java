package incidencias_api.infraestructura.adaptador.salida.persistencia.repositorio;

import incidencias_api.dominio.modelo.EstadoIncidencia;
import incidencias_api.infraestructura.adaptador.salida.persistencia.entidad.IncidenciaEntidad;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncidenciaRepositorioJpa
        extends JpaRepository<IncidenciaEntidad, UUID> {

    List<IncidenciaEntidad> findAllByUsuario_Id(UUID usuarioId);

    Optional<IncidenciaEntidad> findByIdAndUsuario_Id(
            UUID id,
            UUID usuarioId
    );

    @Query("""
            SELECT i
            FROM IncidenciaEntidad i
            WHERE (:estado IS NULL OR i.estado = :estado)
              AND (:categoriaId IS NULL OR i.categoria.id = :categoriaId)
            """)
    List<IncidenciaEntidad> buscarConFiltros(
            @Param("estado") EstadoIncidencia estado,
            @Param("categoriaId") UUID categoriaId
    );
}