package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.dominio.modelo.CategoriaIncidencia;
import incidencias_api.dominio.puerto.entrada.GestionarCategorias;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CategoriaRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CategoriaSolicitud;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaControlador {

    private final GestionarCategorias gestionarCategorias;

    public CategoriaControlador(GestionarCategorias gestionarCategorias) {
        this.gestionarCategorias = gestionarCategorias;
    }

    @PostMapping
    public ResponseEntity<CategoriaRespuesta> crear(
            @Valid @RequestBody CategoriaSolicitud solicitud) {
        CategoriaIncidencia categoria = gestionarCategorias.crear(
                solicitud.nombre(),
                solicitud.descripcion()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirRespuesta(categoria));
    }

    @GetMapping
    public List<CategoriaRespuesta> listar() {
        return gestionarCategorias.listarTodas()
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaRespuesta buscarPorId(@PathVariable UUID id) {
        return convertirRespuesta(gestionarCategorias.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public CategoriaRespuesta actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CategoriaSolicitud solicitud) {
        CategoriaIncidencia categoria = gestionarCategorias.actualizar(
                id,
                solicitud.nombre(),
                solicitud.descripcion()
        );

        return convertirRespuesta(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        gestionarCategorias.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    private CategoriaRespuesta convertirRespuesta(CategoriaIncidencia categoria) {
        return new CategoriaRespuesta(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.isActiva()
        );
    }
}