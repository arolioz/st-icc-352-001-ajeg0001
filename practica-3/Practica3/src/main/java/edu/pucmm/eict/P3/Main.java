package edu.pucmm.eict.P3;

import edu.pucmm.eict.P3.Controladora.*;
import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.BootStrapServices;
import edu.pucmm.eict.P3.Servicios.Controladora;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    void main()  {



        BootStrapServices.getInstancia().startDb();

        Controladora.getInstance().initData();


        var app = Javalin.create(config -> {

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
                    post("/agregar/{id}", CarritoControlador::agregar);
                    get("/carrito",CarritoControlador::cargarCarrito);
                    get("/limpiar-carrito",CarritoControlador::LimpiarCarrito);
                    post("/eliminar-producto-carrito/{id}",CarritoControlador::EliminarProductoCarrito);
                    post("/procesar-compra", VentaControladora::ProcesarCompra);
                    get("/vizualizar/{id}", ProductoControlador::vizualizar);
                    post("/procesar-comentario/{id}",ComentarioControladora::procesarComentario);
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
}
