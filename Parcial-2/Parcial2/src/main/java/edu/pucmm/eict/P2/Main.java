package edu.pucmm.eict.P2;

import edu.pucmm.eict.P2.Services.BootStrapServices;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class Main {
    void main() {

        BootStrapServices.getInstancia().init();

        EntityManager em = Persistence
                .createEntityManagerFactory("MiUnidadPersistencia")
                .createEntityManager();

        var app = Javalin.create(config ->{
            config.routes.get("/", ctx ->{
                ctx.result("Hola mundo!");
            });
        });
        app.start(7000);
    }
}