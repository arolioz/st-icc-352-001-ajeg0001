package Pucmm.eict.edu;

import Pucmm.eict.edu.Controladora.UsuarioControladora;
import Pucmm.eict.edu.Services.DbService;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.Javalin;
import org.bson.types.ObjectId;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    void main(){
        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });


            config.routes.apiBuilder(() ->{
                path("/api", () -> {
                    path("/login", () -> {
                        post("/", UsuarioControladora::procesarLogin);
                    });
                });
            });
        });

        app.start(7001);
    }
}
