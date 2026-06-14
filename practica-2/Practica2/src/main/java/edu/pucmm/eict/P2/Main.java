package edu.pucmm.eict.P2;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    void main(){
        var app = Javalin.create(config -> {

            config.fileRenderer(new JavalinThymeleaf());

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.routes.get("/", ctx -> {
                ctx.result("HOLA!");
            });

        });

        app.start(7000);
    }
}
