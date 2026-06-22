package edu.pucmm.eict.P3;

import edu.pucmm.eict.P3.Controladora.CrudControladorProducto;
import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.BootStrapServices;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    void main()  {

        BootStrapServices.getInstancia().startDb();


        //EntityManagerFactory em = Persistence.createEntityManagerFactory("MiUnidadPersistencia");


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
            });

            config.routes.get("/login", ctx -> {
                ctx.redirect("/login.html");
            });



            config.routes.apiBuilder(() ->{
                path("/crud-producto/", () -> {
                    get(ctx -> ctx.redirect("/crud-producto/listar"));
                    get("/listar", CrudControladorProducto::listar);
                    post("/agregar/{id}",CrudControladorProducto::agregar);
                    get("/carrito",CrudControladorProducto::cargarCarrito);
                    get("/limpiar-carrito",CrudControladorProducto::LimpiarCarrito);
                    post("/eliminar-producto-carrito/{id}",CrudControladorProducto::EliminarProductoCarrito);
                    post("/procesar-compra", CrudControladorProducto::ProcesarCompra);
                });
            });

            config.routes.apiBuilder(() ->{
                path("/administracion/", () -> {
                    get(ctx -> ctx.redirect("/administracion/administrar"));
                    get("/administrar",CrudControladorProducto::administrar);
                    get("/crear",CrudControladorProducto::crear);
                    post("/crear",CrudControladorProducto::procesarCrear);
                    get("/modificar/{id}",CrudControladorProducto::Modificar);
                    post("modificar/{id}", CrudControladorProducto::ProcesarModificar);
                    post("/eliminar/{id}",CrudControladorProducto::ProcesarEliminar);
                    get("/ventas",CrudControladorProducto::ventas);
                });
            });


            config.routes.apiBuilder(() ->{
                path("/", () -> {
                    get(ctx -> ctx.redirect("/crud-producto/listar"));
                    post("/procesar-login", CrudControladorProducto::procesarLogin);
                    get("/cerrar-sesion", CrudControladorProducto::cerrarSesion);
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
