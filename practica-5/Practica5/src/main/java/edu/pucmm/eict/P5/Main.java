package edu.pucmm.eict.P5;

import edu.pucmm.eict.P5.Controladora.*;
import edu.pucmm.eict.P5.Entidades.CarroCompra;
import edu.pucmm.eict.P5.Entidades.Usuario;
import edu.pucmm.eict.P5.Servicios.BootStrapServices;
import edu.pucmm.eict.P5.Servicios.Controladora;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.websocket.WsContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {

    public static final Queue<WsContext> usuariosConectados = new ConcurrentLinkedQueue<>();

    void main()  {

        BootStrapServices.getInstancia().startDb();

        Controladora.getInstance().initData();

        var app = Javalin.create(config -> {

            config.bundledPlugins.enableRouteOverview("/rutas");

            config.routes.ws("/conectarUsuario", ws -> {


                        ws.onConnect(ctx -> {
                            ctx.enableAutomaticPings();   // ping/pong de control para mantener viva la conexión
                            IO.println("Se conecto un usuario!");

                            usuariosConectados.add(ctx);
                            actualizarCantUsuariosConectados();
                        });

                        ws.onClose(ctx -> {
                            IO.println("Se desconecto un usuario!");

                            usuariosConectados.remove(ctx);
                            actualizarCantUsuariosConectados();
                        });
            });

            config.fileRenderer(new JavalinThymeleaf());

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.routes.before("/**", ctx -> {
                if (ctx.sessionAttribute("carrito") == null){
                    ctx.sessionAttribute("carrito",new CarroCompra());
                }

                UsuarioControlador.recordarUsuario(ctx);
            });

            config.routes.get("/login", ctx -> {
                ctx.redirect("/login.html");
            });



            config.routes.apiBuilder(() ->{
                path("/crud-producto/", () -> {
                    get(ctx -> ctx.redirect("/crud-producto/listar"));
                    get("/listar", ProductoControlador::listar);
                    get("/listar/{pagina}", ProductoControlador::listarConPaginacion);
                    post("/agregar/{id}", CarritoControlador::agregar);
                    get("/carrito",CarritoControlador::cargarCarrito);
                    get("/limpiar-carrito",CarritoControlador::LimpiarCarrito);
                    post("/eliminar-producto-carrito/{id}",CarritoControlador::EliminarProductoCarrito);
                    post("/procesar-compra", VentaControladora::ProcesarCompra);
                    get("/vizualizar/{id}", ProductoControlador::vizualizar);
                    post("/procesar-comentario/{id}",ComentarioControladora::procesarComentario);

                    get("/{id}/comentarios", ProductoControlador::comentariosProducto);
                });
            });

            config.routes.apiBuilder(() ->{
                path("/administracion/", () -> {
                    get(ctx -> ctx.redirect("/administracion/administrar"));
                    get("/administrar",ProductoControlador::administrar);
                    get("/crear", ProductoControlador::crear);
                    post("/crear",ProductoControlador::procesarCrear);
                    get("/modificar/{id}",ProductoControlador::Modificar);
                    post("modificar/{id}", ProductoControlador::ProcesarModificar);
                    post("/eliminar/{id}",ProductoControlador ::ProcesarEliminar);
                    get("/ventas",VentaControladora::ventas);
                    get("/eliminar-comentario/{id}",ComentarioControladora::procesarEliminar);
                    get("/dashboard", ProductoControlador::dashboard);

                    get("/esAdmin", UsuarioControlador::esAdministrador);
                });
            });


            config.routes.apiBuilder(() ->{
                path("/", () -> {
                    get(ctx -> ctx.redirect("/crud-producto/listar"));
                    post("/procesar-login", UsuarioControlador::procesarLogin);
                    get("/cerrar-sesion", UsuarioControlador::cerrarSesion);
                });
            });

            config.routes.before("/administracion/**",ctx -> {
                Usuario user = ctx.sessionAttribute("usuario");


                if (user == null ||  (!user.getUsuario().equals("admin") && !user.getPassword().equals("admin"))){
                    ctx.redirect("/login.html");
                    return;
                }

            });



        });

        app.start(7000);
    }

    public static void actualizarCantUsuariosConectados() {
        String cantidad = String.valueOf(contarUsuariosLogeados());
        usuariosConectados.forEach(ctx -> ctx.send(cantidad));
    }

    public static int contarUsuariosLogeados(){
        Set<String> usuarios = new HashSet<>();

        for (WsContext ctx : usuariosConectados){
            Usuario user = ctx.sessionAttribute("usuario");
            if (user != null){
                usuarios.add(user.getUsuario());
            }
        }
        return  usuarios.size();
    }
}
