package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.ProductoCarrito;
import edu.pucmm.eict.P3.Entidades.ProductoVista;
import edu.pucmm.eict.P3.Servicios.Controladora;
import edu.pucmm.eict.P3.Servicios.ProductoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CarritoControlador {
    private final static ProductoServices productoServices = ProductoServices.getInstancia();
    private final static Controladora controladora = Controladora.getInstance();

    public static void agregar(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");
        int id = Integer.parseInt(ctx.pathParam("id"));

        int cantidad = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cantidad")));

        agregandoProductoACarrito(carrito, id, cantidad);

        if (carrito != null){
            ctx.sessionAttribute("carrito",carrito);
        }

        ctx.redirect("/");
    }

    private static CarroCompra agregandoProductoACarrito(CarroCompra carrito, int idProducto, int cantidad){
        Producto producto = productoServices.find(idProducto);

        if (producto != null){
            carrito.agregarProducto(producto,cantidad);
        }

        return carrito;

    }

    public static void cargarCarrito(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");


        Map<String, Object> modelo = controladora.construirModeloBase(ctx);


        assert carrito != null;
        List<ProductoVista> lista = new ArrayList<>();

        for (ProductoCarrito p : carrito.getListaProductos()){
            Producto producto = productoServices.find(p.getIdProducto());

            if (producto != null){
                lista.add(new ProductoVista(p.getIdProducto(),p.getCantidad(),producto.getNombre(),producto.getPrecio()));
            }

        }

        BigDecimal total = controladora.calcularPrecioTotal(lista);

        modelo.put("lista", lista);
        modelo.put("total", total);
        //enviando al sistema de plantilla.
        ctx.render("/Templates/Crud/carritoCompra.html", modelo);
    }

    public static void LimpiarCarrito(@NotNull Context ctx) {
        ctx.sessionAttribute("carrito",new CarroCompra());

        ctx.redirect("/crud-producto/carrito");
    }

    public static void EliminarProductoCarrito(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");
        int id = Integer.parseInt(ctx.pathParam("id"));

        if (carrito != null){
            eliminarProductoCarrito(carrito,id);
            ctx.sessionAttribute("carroto",carrito);
        }

        ctx.redirect("/crud-producto/carrito");
    }

    private static void eliminarProductoCarrito(CarroCompra carrito, int id) {
        if (carrito != null) {
            carrito.getListaProductos()
                    .removeIf(p -> p.getIdProducto() == id);
        }
    }
}
