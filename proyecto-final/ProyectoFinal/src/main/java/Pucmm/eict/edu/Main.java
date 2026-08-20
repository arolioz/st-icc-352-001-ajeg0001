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
               UsuarioService.getInstancia().modificarUsuario(new ObjectId("6a8339ba4f147b218b416348"), "Aaron");
           });
        });

        app.start(7001);
    }
}
