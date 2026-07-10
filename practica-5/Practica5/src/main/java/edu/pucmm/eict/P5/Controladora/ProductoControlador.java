package edu.pucmm.eict.P5.Controladora;

import edu.pucmm.eict.P5.Entidades.CarroCompra;
import edu.pucmm.eict.P5.Entidades.Comentario;
import edu.pucmm.eict.P5.Entidades.Producto;
import edu.pucmm.eict.P5.Entidades.Usuario;
import edu.pucmm.eict.P5.Servicios.Controladora;
import edu.pucmm.eict.P5.Servicios.ProductoServices;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class ProductoControlador {

    private final static ProductoServices productoServices = ProductoServices.getInstancia();
    private final static Controladora controladora = Controladora.getInstance();

    public record ComentarioDTO(
            int id,
            String usuario,
            String comentario,
            Boolean habilitado
    ){}

    public static Map<String, Object> construirModeloBase(Context ctx){
        Map<String, Object> modelo = new HashMap<>();

        Usuario user = ctx.sessionAttribute("usuario");

        boolean deshabilitado = true;

        if(user != null){
            if (user.getUsuario().equals("admin") && user.getPassword().equals("admin")){
                deshabilitado = false;
            }
        }

        CarroCompra carrito = ctx.sessionAttribute("carrito");

        int cantProductos = 0;

        if (carrito != null){
            cantProductos = controladora.cantProductosCarrito(carrito);
        }

        modelo.put("usuario",user);
        modelo.put("cantCarrito",cantProductos);
        modelo.put("deshabilitado",deshabilitado);

        return modelo;
    }

    public static void listar(@NotNull Context ctx){
        ctx.redirect("/crud-producto/listar/1");
    }

    public static void listarConPaginacion(@NotNull Context ctx){
        Map<String, Object> modelo = construirModeloBase(ctx);
        int pagina = Integer.parseInt(ctx.pathParam("pagina"));

        List<Producto> lista = productoServices.getProductosPaginacion(pagina);

        int paginaAnterior = (pagina > 1) ? pagina - 1 : 1;
        int paginaSiguiente = pagina + 1;
        long totalProductos = ProductoServices.getInstancia().contarProductos();
        int ultimaPagina = (int) Math.ceil((double) totalProductos / 10);
        if (ultimaPagina == 0) ultimaPagina = 1;




        modelo.put("pagina",pagina);
        modelo.put("paginaAnterior",paginaAnterior);
        modelo.put("paginaSiguiente",paginaSiguiente);
        modelo.put("ultimaPagina",ultimaPagina);
        IO.println(pagina + "," + ultimaPagina);


        IO.println(modelo);
        if (lista !=  null){
            modelo.put("lista",lista);
            ctx.render("/Templates/Crud/ListaProductos.html", modelo);
        }
        else{
            ctx.result("Error a la hora de listar");
        }
    }



    public static void procesarCrear(@NotNull Context ctx) throws Exception {
        String nombre = ctx.formParam("nombre");
        int precio = Integer.parseInt(Objects.requireNonNull(ctx.formParam("precio")));
        String descripcion = ctx.formParam("descripcion");

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setDescripcion(descripcion);

        productoServices.crear(producto);
        FotoControlador.procesarFotos(ctx,producto.getIdProducto());

        ctx.redirect("/administracion/");
    }

    public static void crear(@NotNull Context ctx) {
        Map<String, Object> modelo = construirModeloBase(ctx);
        modelo.put("titulo","Crear productos");

        ctx.render("/Templates/Crear/productos.html",modelo);
    }


    public static void administrar(@NotNull Context ctx) {
        List<Producto> lista = productoServices.findAll();;


        Map<String, Object> modelo = construirModeloBase(ctx);
        modelo.put("lista", lista);

        ctx.render("/Templates/Crud/CrudProductos.html",modelo);
    }

    public static void Modificar(@NotNull Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Producto p = productoServices.find(id);

        Map<String, Object> modelo = construirModeloBase(ctx);

        modelo.put("titulo","Modificar productos");

        if (p != null){

            modelo.put("producto",p);
            ctx.render("/Templates/Crear/productos.html",modelo);
        }
        else{
            ctx.redirect("/administracion");
        }

    }


    public static void ProcesarModificar(@NotNull Context ctx) throws Exception {
        int id = Integer.parseInt(ctx.pathParam("id"));
        String nombre = ctx.formParam("nombre");
        BigDecimal precio = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(ctx.formParam("precio"))));
        String descripcion = ctx.formParam("descripcion");

        Producto p = productoServices.find(id);

        List<UploadedFile> files = ctx.uploadedFiles("foto");
        IO.println(files.size());

        FotoControlador.procesarFotos(ctx,p.getIdProducto());


        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(descripcion);

        productoServices.editar(p);

        ctx.redirect("/administracion");
    }

    public static void ProcesarEliminar(@NotNull Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));

        Producto p = productoServices.find(id);


        if (p != null){
            productoServices.eliminar(id);
        }

        ctx.redirect("/administracion");

    }

    public static void vizualizar(@NotNull Context ctx){
        Map<String, Object> modelo = construirModeloBase(ctx);

        int id = Integer.parseInt(ctx.pathParam("id"));

        Producto p = productoServices.find(id);

        if (p !=  null){
            modelo.put("producto",p);
            ctx.render("/Templates/Vizualizar/producto.html", modelo);
        }
        else{
            ctx.result("Error a la hora de enviar el producto");
        }

    }

    public static void dashboard(@NotNull Context ctx) {
        Map<String, Object> modelo = construirModeloBase(ctx);

        ctx.render("/Templates/Crud/EstadisticasVenta.html",modelo);
    }

    public static void comentariosProducto(@NotNull Context ctx) {

        int id = Integer.parseInt(ctx.pathParam("id"));
        Producto producto = ProductoServices.getInstancia().find(id);
        List<Comentario> comentarios = producto.getComentarios();

        if (producto != null) {
            List<ComentarioDTO> respuesta = producto.getComentarios().stream()
                    .map( c -> new ComentarioDTO(
                                            c.getId(),
                                            c.getUsuario(),
                                            c.getContenido(),
                                            c.getHabilitado()

                    )).toList();

            ctx.json(respuesta);
        }
        else {
            ctx.result("Producto no encontrado");
        }

    }
}
