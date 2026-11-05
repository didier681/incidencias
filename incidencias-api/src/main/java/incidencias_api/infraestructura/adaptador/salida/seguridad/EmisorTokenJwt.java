package incidencias_api.infraestructura.adaptador.salida.seguridad;

import incidencias_api.dominio.modelo.Usuario;
import incidencias_api.dominio.puerto.salida.EmisorTokenAcceso;
import java.time.Instant;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class EmisorTokenJwt implements EmisorTokenAcceso {

    private static final long DURACION_SEGUNDOS = 3600;

    private final JwtEncoder codificadorJwt;

    public EmisorTokenJwt(JwtEncoder codificadorJwt) {
        this.codificadorJwt = codificadorJwt;
    }

    @Override
    public String emitir(Usuario usuario) {
        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("incidencias-api")
                .issuedAt(ahora)
                .expiresAt(ahora.plusSeconds(DURACION_SEGUNDOS))
                .subject(usuario.getId().toString())
                .claim("correo", usuario.getCorreo())
                .claim("rol", usuario.getRol().name())
                .build();

        JwsHeader encabezado = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return codificadorJwt.encode(
                JwtEncoderParameters.from(encabezado, claims)
        ).getTokenValue();
    }

    @Override
    public long duracionEnSegundos() {
        return DURACION_SEGUNDOS;
    }
}