package edu.pucmm.eict.Asg1;

import io.javalin.Javalin;

public class Main {
    void main(){

        record Usuario(String usuario, String password){

        }

        var app = Javalin.create(config -> {

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.routes.get("/", ctx -> {
                ctx.result("Autentificacion Validada!");
            });

            config.routes.before("/", ctx -> {
                Usuario user = ctx.sessionAttribute("usuario");

                if (user == null || (!user.usuario.equals("Aaron") && !user.password.equals("admin"))){
                    ctx.redirect("Login.html");
                }
            });

            config.routes.post("procesar-login", ctx -> {
                String usuario = ctx.formParam("usuario");
                String password = ctx.formParam("password");

                ctx.sessionAttribute("usuario", new Usuario(usuario,password));

                ctx.redirect("/");
            });

        });

        app.start(7000);
    }


}
