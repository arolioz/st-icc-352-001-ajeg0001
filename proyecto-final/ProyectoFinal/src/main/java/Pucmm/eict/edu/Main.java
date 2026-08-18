package Pucmm.eict.edu;

import Pucmm.eict.edu.Services.DbService;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.Javalin;
import org.bson.types.ObjectId;

public class Main {
    public void main(){
        var app = Javalin.create(config -> {
           config.routes.get("/", ctx -> {
               ctx.result("Hola mundo!");
               DbService.inicializar();
               UsuarioService.getInstancia().eliminarUsuario(new ObjectId("6a83a184fee1166c3ffedfbb"));
           });
        });

        app.start(7001);
    }
}
