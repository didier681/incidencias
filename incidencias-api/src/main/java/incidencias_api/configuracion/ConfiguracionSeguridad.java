package incidencias_api.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/usuarios/registro",
                                "/api/usuarios/login"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categorias"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/incidencias/*/estado"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/incidencias/*/solicitudes-servicio/*/estado"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/incidencias/administracion"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios/administracion"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/usuarios/*/estado"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/usuarios/*/rol"
                        ).hasRole("ADMINISTRADOR")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/estadisticas/incidencias"
                        ).hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(recurso -> recurso
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                convertidorJwt()
                        ))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationConverter convertidorJwt() {
        JwtGrantedAuthoritiesConverter convertidorRoles =
                new JwtGrantedAuthoritiesConverter();

        convertidorRoles.setAuthoritiesClaimName("rol");
        convertidorRoles.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter convertidorJwt =
                new JwtAuthenticationConverter();
        convertidorJwt.setJwtGrantedAuthoritiesConverter(convertidorRoles);

        return convertidorJwt;
    }
}