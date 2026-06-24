package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.*;
import edu.pucmm.eict.P3.Servicios.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();








    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.redirect("/");
    }



}
