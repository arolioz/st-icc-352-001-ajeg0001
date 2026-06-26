package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.UsuarioServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsuarioControlador {

    private final static UsuarioServices usuarioService = UsuarioServices.getInstancia();

    public static void procesarLogin(@NotNull Context ctx) {
        String usuario = ctx.formParam("usuario");
        String password = ctx.formParam("password");
        String rememberUser = ctx.formParam("rememberUser");

        if (rememberUser != null){
            IO.println("TRUEEEE");
        }
        else{
            IO.println("False");
        }


        Usuario user = ValidarUsuario(usuario,password);

        if (user != null){
            ctx.sessionAttribute("usuario", user);
            ctx.redirect("/");
        }
        else {
            ctx.redirect("/login.html");
        }
    }

    private static Usuario ValidarUsuario(String usuario, String password){
        Usuario u = usuarioService.findUsuarioPorUsuario(usuario, password);

        return  u;
    }

    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.redirect("/");
    }
}
