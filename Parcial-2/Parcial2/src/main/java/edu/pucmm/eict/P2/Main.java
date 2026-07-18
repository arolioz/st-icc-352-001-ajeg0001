package edu.pucmm.eict.P2;

import Util.RolesApp;
import edu.pucmm.eict.P2.Api.EventoApi;
import edu.pucmm.eict.P2.Api.UsuarioApi;
import edu.pucmm.eict.P2.Controlador.EventoControlador;
import edu.pucmm.eict.P2.Controlador.UsuarioControlador;
import edu.pucmm.eict.P2.Entidades.Usuario;
import edu.pucmm.eict.P2.Services.BootStrapServices;
import edu.pucmm.eict.P2.Services.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    void main() {

        BootStrapServices.getInstancia().init();

        EntityManager em = Persistence
                .createEntityManagerFactory("MiUnidadPersistencia")
                .createEntityManager();

        var app = Javalin.create(config ->{

            config.routes.beforeMatched(ctx -> {

                // Si la ruta no tiene roles, permitir acceso
                if (ctx.routeRoles().isEmpty()) {
                    return;
                }

                Usuario usuario = ctx.sessionAttribute("usuario");

                if (usuario == null) {
                    ctx.status(401).result("Debe iniciar session para entrar aqui");
                    ctx.skipRemainingHandlers();
                    return;
                }

                if (usuario.getListaRoles().contains(RolesApp.ROLE_BLOQUEADO)){
                    ctx.status(401).result("Usted esta bloqueado, comuniquese con un administrador");
                    ctx.skipRemainingHandlers();
                    return;
                }

                boolean autorizado = usuario.getListaRoles()
                        .stream()
                        .anyMatch(ctx.routeRoles()::contains);

                if (!autorizado) {
                    ctx.status(403);
                    ctx.result("NO ESTA AUTORIZADO PARA ENTRAR A ESTA PAGINA");
                    ctx.skipRemainingHandlers();
                }
            });

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.routes.before("/**", UsuarioControlador::recordarUsuario);

            config.routes.apiBuilder(() ->{
                path("/login", () -> {
                    get(ctx -> ctx.redirect("/login.html"));
                    post("/procesarLogin",UsuarioControlador::procesarLogin);
                    post("/procesarRegistrar", UsuarioControlador::procesarRegistrar);
                });
            });
            config.routes.apiBuilder(() ->{
                path("/registrar", () -> {
                    get(ctx -> ctx.redirect("/registrar.html"));
                });
            });

            config.routes.apiBuilder(() -> {
               path("/Usuarios", () -> {
                   get(UsuarioControlador::listarUsuarios,RolesApp.ROLE_ADMIN);
                   get("/Listar",UsuarioControlador::listarUsuarios,RolesApp.ROLE_ADMIN);
               });
            });

            config.routes.apiBuilder(() -> {
                path("/Eventos", () -> {
                    get(ctx -> ctx.redirect("/Templates/listaEventos.html"));
                    get("/Crear", ctx -> ctx.redirect("/Templates/crearEventos.html"));
                    post("/procesar-crear", EventoControlador::procesarCrear);
                    post("/inscribir/{eventId}",EventoControlador::inscribirUsuario, RolesApp.ROLE_USUARIO);
                    post("/procesar-modificar", EventoControlador::procesarModificar);
                    post("/cancelarInscripcion/{eventId}", EventoControlador::cancelarInscripcion);
                    post("cancelar/{id}",EventoControlador::cancelarEvento);
                    post("cambiarEstado/{id}",EventoControlador::cambiarEstadoEvento);

                });
            });

            config.routes.apiBuilder(() -> {
                path("/Api", () -> {
                    get("/listaEventos", EventoApi::listaEventos);
                    get("/obtenerOrganizador/{id}", UsuarioApi::obtenerOrganizador);
                    get("/obtenerCupo-Evento/{id}", EventoApi::cupoEvento);
                    get("usuarioEsAdmin", UsuarioApi::esAdmin);
                    get("usuarioEsOrganizador", UsuarioApi::esOrganizador);
                    get("usuarioEstaInscrito/{id}", UsuarioApi::usuarioEventoInscrito);
                });
            });



            config.routes.get("/", ctx ->{
                ctx.redirect("/inicio.html");
                Usuario u = ctx.sessionAttribute("usuario");

                if (u != null){
                    IO.println(u.getUsuario());
                }

            });
        });

        crearDatosIniciales();
        app.start(7000);
    }

    public static void crearDatosIniciales(){
        List<Usuario> usuarios = UsuarioServices.getInstancia().findAll();

        if (usuarios.isEmpty()){
            Usuario admin = new Usuario();

            admin.setNombre("admin");
            admin.setUsuario("admin");
            admin.setPassword("admin");

            admin.setListaRoles(Set.of(RolesApp.ROLE_ADMIN,RolesApp.ROLE_ORGANIZADOR,RolesApp.ROLE_USUARIO));

            UsuarioServices.getInstancia().crear(admin);
        }



    }
}