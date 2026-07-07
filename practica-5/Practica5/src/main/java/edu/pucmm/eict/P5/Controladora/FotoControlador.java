package edu.pucmm.eict.P5.Controladora;


import edu.pucmm.eict.P5.Entidades.Foto;
import edu.pucmm.eict.P5.Entidades.Producto;
import edu.pucmm.eict.P5.Servicios.FotoServices;
import edu.pucmm.eict.P5.Servicios.ProductoServices;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FotoControlador {


    public static void listarFotos(@NotNull Context ctx) throws Exception {
        List<Foto> fotos = FotoServices.getInstancia().findAll();

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Ejemplo de funcionalidad Thymeleaf");
        modelo.put("fotos", fotos);

        //
        ctx.render("/templates/listar.html", modelo);
    }

    public static void procesarFotos(@NotNull Context ctx, int idProducto) throws Exception {
        Producto p = ProductoServices.getInstancia().find(idProducto);
        IO.println(p.getIdProducto());
        System.out.println("Entró a procesarFotos");



        ctx.uploadedFiles("foto").forEach(uploadedFile -> {
            IO.println("Foto");
            try {

                if (p != null){
                    byte[] bytes = uploadedFile.content().readAllBytes();
                    if (bytes.length > 0) {
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        Foto foto = new Foto(uploadedFile.filename(), uploadedFile.contentType(), encodedString);
                        foto.setProducto(p);
                        p.addFoto(foto);
                        FotoServices.getInstancia().crear(foto);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ctx.redirect("/fotos/listar");
        });
    }

    public static void visualizarFotos(@NotNull Context ctx) throws Exception {
        try {
            Foto foto = FotoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
            if(foto==null){
                ctx.redirect("/fotos/listar");
                return;
            }
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("foto", foto);
            //
            ctx.render("/templates/visualizar.html", modelo);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            ctx.redirect("/fotos/listar");
        }
    }

    public static void eliminarFotos(@NotNull Context ctx) throws Exception {
        try {
            Foto foto = FotoServices.getInstancia().find(ctx.pathParamAsClass("id", Long.class).get());
            if(foto!=null){
                FotoServices.getInstancia().eliminar(foto.getId());
            }
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        ctx.redirect("/fotos/listar");
    }


}
