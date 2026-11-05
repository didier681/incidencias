package incidencias_api.dominio.puerto.salida;

public interface CodificadorContrasena {

    String codificar(String contrasena);

    boolean coincide(String contrasena, String hash);
}