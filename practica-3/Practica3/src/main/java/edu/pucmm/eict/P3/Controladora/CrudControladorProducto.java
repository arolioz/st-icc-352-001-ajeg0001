package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.*;
import edu.pucmm.eict.P3.Servicios.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();






    public static void procesarLogin(@NotNull Context ctx) {
        String usuario = ctx.formParam("usuario");
        String password = ctx.formParam("password");

        Usuario user = controladora.ValidarUsuario(usuario,password);

        if (user != null){
            ctx.sessionAttribute("usuario", user);
            ctx.redirect("/");
        }
        else {
            ctx.redirect("/login.html");
        }



    }

    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.redirect("/");
    }



}
