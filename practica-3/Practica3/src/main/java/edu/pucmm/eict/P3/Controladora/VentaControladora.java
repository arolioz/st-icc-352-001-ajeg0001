package edu.pucmm.eict.P3.Controladora;

import edu.pucmm.eict.P3.Entidades.*;
import edu.pucmm.eict.P3.Servicios.Controladora;
import edu.pucmm.eict.P3.Servicios.ProductoServices;
import edu.pucmm.eict.P3.Servicios.VentaServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VentaControladora {
    private final static VentaServices ventaServices = VentaServices.getInstancia();
    private final static Controladora controladora = Controladora.getInstance();
    private final static ProductoServices productoServices = ProductoServices.getInstancia();

    public static void ProcesarCompra(@NotNull Context ctx) {
        String nombre = ctx.formParam("nombreCliente");

        CarroCompra carrito = ctx.sessionAttribute("carrito");

        assert carrito != null;
        List<ProductoVista> listaProductos = construirListaCompra(carrito);

        VentaProductos venta = new VentaProductos();

        if (nombre != null){
            venta.setNombreCliente(nombre);
            venta.setListaProductos(listaProductos);
            ventaServices.crear(venta);

            ctx.redirect("/crud-producto/limpiar-carrito");
        }
        else{
            ctx.redirect("/crud-producto/carrito");
        }
    }

    public static List<ProductoVista> construirListaCompra(CarroCompra carrito) {
        List<ProductoVista> lista = new ArrayList<>();

        for (ProductoCarrito p : carrito.getListaProductos()){
            Producto producto = productoServices.find(p.getIdProducto());

            if (producto != null){
                lista.add(new ProductoVista(p.getIdProducto(),p.getCantidad(),producto.getNombre(),producto.getPrecio()));
            }
        }

        return lista;
    }
}
