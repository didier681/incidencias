package incidencias_api.infraestructura.adaptador.salida.seguridad;

import incidencias_api.dominio.puerto.salida.CodificadorContrasena;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CodificadorContrasenaBcrypt implements CodificadorContrasena {

    private final BCryptPasswordEncoder codificador =
            new BCryptPasswordEncoder();

    @Override
    public String codificar(String contrasena) {
        return codificador.encode(contrasena);
    }

    @Override
    public boolean coincide(String contrasena, String hash) {
        return codificador.matches(contrasena, hash);
    }
}