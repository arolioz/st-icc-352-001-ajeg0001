package edu.pucmm.eict.App2;

import io.javalin.Javalin;

public class Main {
    void main(){
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.routes.get("/", ctx -> {
                ctx.result("Hola desde la aplicacion 2!");
            });
        });

        app.start(7001);
    }
}
