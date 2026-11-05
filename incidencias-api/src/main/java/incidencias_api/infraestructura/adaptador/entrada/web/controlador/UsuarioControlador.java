package incidencias_api.infraestructura.adaptador.entrada.web.controlador;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.entrada.AutenticarUsuario;
import incidencias_api.dominio.puerto.entrada.CambiarEstadoUsuario;
import incidencias_api.dominio.puerto.entrada.CambiarRolUsuario;
import incidencias_api.dominio.puerto.entrada.ConsultarPerfilUsuario;
import incidencias_api.dominio.puerto.entrada.ConsultarUsuarios;
import incidencias_api.dominio.puerto.entrada.RegistrarUsuario;
import incidencias_api.dominio.puerto.salida.EmisorTokenAcceso;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CambiarEstadoUsuarioSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.CambiarRolUsuarioSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.InicioSesionRespuesta;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.InicioSesionSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.RegistroUsuarioSolicitud;
import incidencias_api.infraestructura.adaptador.entrada.web.dto.UsuarioRespuesta;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final RegistrarUsuario registrarUsuario;
    private final AutenticarUsuario autenticarUsuario;
    private final ConsultarPerfilUsuario consultarPerfilUsuario;
    private final ConsultarUsuarios consultarUsuarios;
    private final CambiarEstadoUsuario cambiarEstadoUsuario;
    private final CambiarRolUsuario cambiarRolUsuario;
    private final EmisorTokenAcceso emisorTokenAcceso;

    public UsuarioControlador(
            RegistrarUsuario registrarUsuario,
            AutenticarUsuario autenticarUsuario,
            ConsultarPerfilUsuario consultarPerfilUsuario,
            ConsultarUsuarios consultarUsuarios,
            CambiarEstadoUsuario cambiarEstadoUsuario,
            CambiarRolUsuario cambiarRolUsuario,
            EmisorTokenAcceso emisorTokenAcceso) {
        this.registrarUsuario = registrarUsuario;
        this.autenticarUsuario = autenticarUsuario;
        this.consultarPerfilUsuario = consultarPerfilUsuario;
        this.consultarUsuarios = consultarUsuarios;
        this.cambiarEstadoUsuario = cambiarEstadoUsuario;
        this.cambiarRolUsuario = cambiarRolUsuario;
        this.emisorTokenAcceso = emisorTokenAcceso;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioRespuesta> registrar(
            @Valid @RequestBody RegistroUsuarioSolicitud solicitud) {
        Usuario usuario = registrarUsuario.registrar(
                solicitud.nombre(),
                solicitud.correo(),
                solicitud.contrasena()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirRespuesta(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<InicioSesionRespuesta> iniciarSesion(
            @Valid @RequestBody InicioSesionSolicitud solicitud) {
        Usuario usuario = autenticarUsuario.autenticar(
                solicitud.correo(),
                solicitud.contrasena()
        );

        InicioSesionRespuesta respuesta = new InicioSesionRespuesta(
                emisorTokenAcceso.emitir(usuario),
                "Bearer",
                emisorTokenAcceso.duracionEnSegundos(),
                convertirRespuesta(usuario)
        );

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioRespuesta> consultarPerfil(
            @AuthenticationPrincipal Jwt jwt) {
        UUID usuarioId = UUID.fromString(jwt.getSubject());
        Usuario usuario = consultarPerfilUsuario.consultarPorId(usuarioId);

        return ResponseEntity.ok(convertirRespuesta(usuario));
    }

    @GetMapping("/administracion")
    public List<UsuarioRespuesta> listarUsuarios() {
        return consultarUsuarios.listarTodos()
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @PutMapping("/{id}/estado")
    public UsuarioRespuesta cambiarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody CambiarEstadoUsuarioSolicitud solicitud) {
        Usuario usuario = cambiarEstadoUsuario.cambiarEstado(
                id,
                solicitud.activo()
        );

        return convertirRespuesta(usuario);
    }

    @PutMapping("/{id}/rol")
    public UsuarioRespuesta cambiarRol(
            @PathVariable UUID id,
            @Valid @RequestBody CambiarRolUsuarioSolicitud solicitud,
            @AuthenticationPrincipal Jwt jwt) {
        UUID administradorId = UUID.fromString(jwt.getSubject());

        if (id.equals(administradorId)) {
            throw new AccessDeniedException("No puedes cambiar tu propio rol.");
        }

        Usuario usuario = cambiarRolUsuario.cambiarRol(id, solicitud.rol());
        return convertirRespuesta(usuario);
    }

    private UsuarioRespuesta convertirRespuesta(Usuario usuario) {
        return new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.isActivo()
        );
    }
}