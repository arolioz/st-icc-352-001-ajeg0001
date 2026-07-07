package edu.pucmm.eict.P5.Controladora;

import edu.pucmm.eict.P5.Entidades.*;
import edu.pucmm.eict.P5.Servicios.Controladora;
import edu.pucmm.eict.P5.Servicios.ProductoServices;
import edu.pucmm.eict.P5.Servicios.VentaServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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

    public static void ventas(@NotNull Context ctx) {
        Map<String, Object> modelo = controladora.construirModeloBase(ctx);

        List<VentaProductos> ventas = ventaServices.findAll();

        modelo.put("listaVentas",ventas);

        ctx.render("/Templates/Crud/ventas.html",modelo);

    }
}
