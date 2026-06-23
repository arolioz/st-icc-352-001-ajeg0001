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

        VentaProductos venta = new VentaProductos();

        assert carrito != null;

        if (nombre != null){
            venta.setNombreCliente(nombre);
            construirListaCompra(venta,carrito);
            ventaServices.crear(venta);

            ctx.redirect("/crud-producto/limpiar-carrito");
        }
        else{
            ctx.redirect("/crud-producto/carrito");
        }
    }

    private static void construirListaCompra(VentaProductos venta,CarroCompra carrito) {


        for (ProductoCarrito p : carrito.getListaProductos()){
            Producto producto = productoServices.find(p.getIdProducto());

            if (producto != null){
                ProductoVista tmp = new ProductoVista();
                tmp.setIdProducto(p.getIdProducto());
                tmp.setCantidad(p.getCantidad());
                tmp.setNombre(producto.getNombre());
                tmp.setPrecio(producto.getPrecio());
                tmp.setVenta(venta);

                venta.addProducto(tmp);
            }
        }
    }
}
