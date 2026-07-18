package edu.pucmm.eict.P2.Api;

import Util.RolesApp;
import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.Usuario;
import edu.pucmm.eict.P2.Services.EventoServices;
import edu.pucmm.eict.P2.Services.UsuarioServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsuarioApi {
    public static void obtenerOrganizador(@NotNull Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));

        Usuario u = UsuarioServices.getInstancia().find(id);

        if (u != null && !u.getListaRoles().contains(RolesApp.ROLE_ORGANIZADOR)) {
            ctx.json("Error");
        }
        else{
            ctx.json(u.getUsuario());
        }


    }

    public static void esAdmin(@NotNull Context ctx) {
        Usuario u = ctx.sessionAttribute("usuario");

        if (u != null && u.getListaRoles().contains(RolesApp.ROLE_ADMIN)){
            ctx.json(true);
        }
        else{
            ctx.json(false);
        }
    }
    public static void esOrganizador(@NotNull Context ctx) {
        Usuario u = ctx.sessionAttribute("usuario");

        if (u != null && u.getListaRoles().contains(RolesApp.ROLE_ORGANIZADOR)){
            ctx.json(true);
        }
        else{
            ctx.json(false);
        }
    }


}
