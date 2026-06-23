package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Servicios.Controladora;
import edu.pucmm.eict.P3.Servicios.ProductoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductoControlador {

    private final static ProductoServices productoServices = ProductoServices.getInstancia();
    private final static Controladora controladora = Controladora.getInstance();
    

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
        Map<String, Object> modelo = construirModeloBase(ctx);

        List<Producto> lista = productoServices.findAll();

        if (lista !=  null){
            modelo.put("lista",lista);
            ctx.render("/templates/crud/ListaProductos.html", modelo);
        }
        else{
            ctx.result("Error a la hora de listar");
        }

    }

    public static void procesarCrear(@NotNull Context ctx) {
        String nombre = ctx.formParam("nombre");
        int precio = Integer.parseInt(Objects.requireNonNull(ctx.formParam("precio")));

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));

        productoServices.crear(producto);

        ctx.redirect("/administracion/");
    }

    public static void crear(@NotNull Context ctx) {
        Map<String, Object> modelo = construirModeloBase(ctx);
        modelo.put("titulo","Crear productos");

        ctx.render("/templates/Crear/productos.html",modelo);
    }
}
