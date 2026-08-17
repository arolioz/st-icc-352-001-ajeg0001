package Pucmm.eict.edu;

import Pucmm.eict.edu.Services.DbService;
import io.javalin.Javalin;

public class Main {
    public void main(){
        var app = Javalin.create(config -> {
           config.routes.get("/", ctx -> {
               ctx.result("Hola mundo!");
               DbService.inicializar();
           });

        });

        app.start(7001);
    }
}
