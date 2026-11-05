package incidencias_api.dominio.puerto.salida;

import incidencias_api.dominio.modelo.Usuario;

public interface EmisorTokenAcceso {

    String emitir(Usuario usuario);

    long duracionEnSegundos();
}