package edu.pucmm.eict.P2;

import io.javalin.Javalin;

public class Main {
    void main(){
        var app = Javalin.create(config ->{
            config.routes.get("/", ctx ->{
                ctx.result("Hola mundo!");
            });
        });

        app.start(7000);
    }
}
