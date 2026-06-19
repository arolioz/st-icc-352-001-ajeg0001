package edu.pucmm.eict.P2.Controladora;

import edu.pucmm.eict.P2.Logico.CarroCompra;
import edu.pucmm.eict.P2.Logico.Producto;
import edu.pucmm.eict.P2.Logico.Usuario;
import edu.pucmm.eict.P2.Service.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();

    public static void listar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();

        Usuario user = ctx.sessionAttribute("usuario");

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("lista", lista);
        modelo.put("usuario",user);
        //enviando al sistema de plantilla.
        ctx.render("/templates/crud/ListaProductos.html", modelo);
    }


    public static void agregar(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");
        int id = Integer.parseInt(ctx.pathParam("id"));

        int cantidad = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cantidad")));

        carrito = controladora.agregandoProductoACarrito(carrito,id,cantidad);

        if (carrito != null){
            ctx.sessionAttribute("carrito",carrito);
        }

        //IO.println("Id: " + carrito.getListaProductos().get(0).getIdProducto());
        //IO.println("Cantidad: " + carrito.getListaProductos().get(0).getCantidad());
        ctx.redirect("/crud-producto");


    }

    public static void crear(@NotNull Context ctx) {
        ctx.render("/templates/Crear/productos.html");
    }

    public static void administrar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();


        Map<String, Object> modelo = new HashMap<>();
        modelo.put("lista", lista);

        ctx.render("/templates/crud/CrudProductos.html",modelo);
    }

    public static void procesarCrear(@NotNull Context ctx) {
        String nombre = ctx.formParam("nombre");
        int precio = Integer.parseInt(Objects.requireNonNull(ctx.formParam("precio")));

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));

        controladora.agregarProducto(producto);

        ctx.redirect("/crud-producto/administrar");
    }

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
}
