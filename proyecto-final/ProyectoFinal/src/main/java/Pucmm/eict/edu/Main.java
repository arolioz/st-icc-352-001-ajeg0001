package Pucmm.eict.edu;

import Pucmm.eict.edu.Services.DbService;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.Javalin;

public class Main {
    public void main(){
        var app = Javalin.create(config -> {
           config.routes.get("/", ctx -> {
               ctx.result("Hola mundo!");
               DbService.inicializar();
               UsuarioService.getInstancia().crearUsuario("Test2","ickkck");
           });
        });

        app.start(7001);
    }
}
