package edu.pucmm.eict.App1;

import io.javalin.Javalin;

public class Main {
    void main(){
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.routes.get("/", ctx -> {
                ctx.result("Hola desde la aplicacion 1!");
            });
        });

        app.start(7000);
    }
}
