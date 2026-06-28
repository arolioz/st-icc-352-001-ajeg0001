package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.Comentario;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.ComentarioServices;
import edu.pucmm.eict.P3.Servicios.ProductoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class ComentarioControladora {
    public static void procesarComentario(@NotNull Context ctx){
        Usuario user = ctx.sessionAttribute("usuario");

        if (user == null){
            ctx.redirect("/login.html");
        }
        else{
            String usuario = user.getUsuario();
            String contenido = ctx.formParam("contenido");

            int id = Integer.parseInt(ctx.pathParam("id"));
            Producto p = ProductoServices.getInstancia().find(id);

            if (p != null){
                Comentario comentario = new Comentario();
                comentario.setContenido(contenido);
                comentario.setProducto(p);
                comentario.setUsuario(usuario);
                p.addComentario(comentario);

                ComentarioServices.getInstancia().crear(comentario);
                ctx.redirect("/crud-producto/vizualizar/"+id);
            }

        }
    }
    public static void procesarEliminar(@NotNull Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        IO.println("ID recibido: " + id);

        Comentario c = ComentarioServices.getInstancia().find(id);

        if (c != null){
            c.setHabilitado(false);
            ComentarioServices.getInstancia().editar(c);

            ctx.redirect("/crud-producto/vizualizar/"+c.getProducto().getIdProducto());
        }
    }


}
