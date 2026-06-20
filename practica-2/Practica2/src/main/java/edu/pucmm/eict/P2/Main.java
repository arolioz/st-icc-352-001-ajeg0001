package edu.pucmm.eict.P2;

import edu.pucmm.eict.P2.Controladora.CrudControladorProducto;
import edu.pucmm.eict.P2.Logico.CarroCompra;
import edu.pucmm.eict.P2.Logico.Usuario;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    void main(){
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
