package incidencias_api.infraestructura.adaptador.entrada.web.dto;

public record InicioSesionRespuesta(
        String token,
        String tipoToken,
        long expiraEnSegundos,
        UsuarioRespuesta usuario
) {
}