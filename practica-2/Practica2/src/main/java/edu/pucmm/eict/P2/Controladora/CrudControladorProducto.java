package edu.pucmm.eict.P2.Controladora;

import edu.pucmm.eict.P2.Logico.CarroCompra;
import edu.pucmm.eict.P2.Logico.Producto;
import edu.pucmm.eict.P2.Service.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();

    public static void listar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();


        Map<String, Object> modelo = new HashMap<>();
        modelo.put("lista", lista);
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

    public static void crear(@NotNull Context context) {

    }
}
