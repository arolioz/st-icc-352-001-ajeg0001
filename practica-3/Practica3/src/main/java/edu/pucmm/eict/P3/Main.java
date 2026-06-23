package edu.pucmm.eict.P3;

import edu.pucmm.eict.P3.Controladora.CrudControladorProducto;
import edu.pucmm.eict.P3.Controladora.ProductoControlador;
import edu.pucmm.eict.P3.Controladora.VentaControladora;
import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.BootStrapServices;
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
                    get("/listar", ProductoControlador::listar);
                    post("/agregar/{id}",CrudControladorProducto::agregar);
                    get("/carrito",CrudControladorProducto::cargarCarrito);
                    get("/limpiar-carrito",CrudControladorProducto::LimpiarCarrito);
                    post("/eliminar-producto-carrito/{id}",CrudControladorProducto::EliminarProductoCarrito);
                    post("/procesar-compra", VentaControladora::ProcesarCompra);
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
