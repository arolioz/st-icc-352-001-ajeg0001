package edu.pucmm.eict.P2;

import edu.pucmm.eict.P2.Controladora.CrudControladorProducto;
import edu.pucmm.eict.P2.Logico.CarroCompra;
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
                    get("/crear",CrudControladorProducto::crear);
                    get("/administrar",CrudControladorProducto::administrar);
                    post("/crear",CrudControladorProducto::procesarCrear);
                });
            });



            config.routes.apiBuilder(() ->{
                path("/", () -> {
                    get(ctx -> ctx.redirect("/crud-producto/listar"));
                    post("/procesar-login", CrudControladorProducto::procesarLogin);
                });
            });

        });

        app.start(7000);
    }
}
