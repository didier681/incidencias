package incidencias_api.configuracion;

import incidencias_api.aplicacion.servicio.ServicioAutenticacionUsuarios;
import incidencias_api.aplicacion.servicio.ServicioCambiarEstadoUsuario;
import incidencias_api.aplicacion.servicio.ServicioCambiarRolUsuario;
import incidencias_api.aplicacion.servicio.ServicioConsultaPerfilUsuario;
import incidencias_api.aplicacion.servicio.ServicioConsultaUsuarios;
import incidencias_api.aplicacion.servicio.ServicioGestionCategorias;
import incidencias_api.aplicacion.servicio.ServicioRegistroUsuarios;
import incidencias_api.dominio.puerto.entrada.AutenticarUsuario;
import incidencias_api.dominio.puerto.entrada.CambiarEstadoUsuario;
import incidencias_api.dominio.puerto.entrada.CambiarRolUsuario;
import incidencias_api.dominio.puerto.entrada.ConsultarPerfilUsuario;
import incidencias_api.dominio.puerto.entrada.ConsultarUsuarios;
import incidencias_api.dominio.puerto.entrada.GestionarCategorias;
import incidencias_api.dominio.puerto.entrada.RegistrarUsuario;
import incidencias_api.dominio.puerto.salida.CodificadorContrasena;
import incidencias_api.dominio.puerto.salida.RepositorioCategoria;
import incidencias_api.dominio.puerto.salida.RepositorioUsuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionAplicacion {

    @Bean
    public GestionarCategorias gestionarCategorias(
            RepositorioCategoria repositorioCategoria) {
        return new ServicioGestionCategorias(repositorioCategoria);
    }

    @Bean
    public RegistrarUsuario registrarUsuario(
            RepositorioUsuario repositorioUsuario,
            CodificadorContrasena codificadorContrasena) {
        return new ServicioRegistroUsuarios(
                repositorioUsuario,
                codificadorContrasena
        );
    }

    @Bean
    public AutenticarUsuario autenticarUsuario(
            RepositorioUsuario repositorioUsuario,
            CodificadorContrasena codificadorContrasena) {
        return new ServicioAutenticacionUsuarios(
                repositorioUsuario,
                codificadorContrasena
        );
    }

    @Bean
    public ConsultarPerfilUsuario consultarPerfilUsuario(
            RepositorioUsuario repositorioUsuario) {
        return new ServicioConsultaPerfilUsuario(repositorioUsuario);
    }

    @Bean
    public ConsultarUsuarios consultarUsuarios(
            RepositorioUsuario repositorioUsuario) {
        return new ServicioConsultaUsuarios(repositorioUsuario);
    }

    @Bean
    public CambiarEstadoUsuario cambiarEstadoUsuario(
            RepositorioUsuario repositorioUsuario) {
        return new ServicioCambiarEstadoUsuario(repositorioUsuario);
    }

    @Bean
    public CambiarRolUsuario cambiarRolUsuario(
            RepositorioUsuario repositorioUsuario) {
        return new ServicioCambiarRolUsuario(repositorioUsuario);
    }
}