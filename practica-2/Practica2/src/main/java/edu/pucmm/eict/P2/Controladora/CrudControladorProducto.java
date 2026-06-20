package edu.pucmm.eict.P2.Controladora;

import edu.pucmm.eict.P2.Logico.*;
import edu.pucmm.eict.P2.Service.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();

    public static void listar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();

        Usuario user = ctx.sessionAttribute("usuario");


        Map<String, Object> modelo = construirModeloBase(ctx);


        modelo.put("lista", lista);
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
        ctx.redirect("/");


    }

    public static void crear(@NotNull Context ctx) {
        Map<String, Object> modelo = construirModeloBase(ctx);

        ctx.render("/templates/Crear/productos.html",modelo);
    }

    public static void administrar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();


        Map<String, Object> modelo = construirModeloBase(ctx);
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

        ctx.redirect("/administracion/");
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

    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.redirect("/");
    }

    public static void Modificar(@NotNull Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Producto p = controladora.buscarProductoPorId(id);

        Map<String, Object> modelo = construirModeloBase(ctx);

        if (p != null){
            modelo.put("producto",p);
            ctx.render("/templates/Crear/productos.html",modelo);
        }
        else{
            ctx.redirect("/administracion");
        }

    }

    public static void ProcesarModificar(@NotNull Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        String nombre = ctx.formParam("nombre");
        int precio = Integer.parseInt(Objects.requireNonNull(ctx.formParam("precio")));


        controladora.modificarProducto(id,nombre, new BigDecimal(precio));

        ctx.redirect("/administracion");
    }

    public static void ProcesarEliminar(@NotNull Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));

        controladora.eliminarProducto(id);

        ctx.redirect("/administracion");

    }

    public static void cargarCarrito(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");


        Map<String, Object> modelo = construirModeloBase(ctx);


        assert carrito != null;
        List <ProductoVista> lista = new ArrayList<>();

        for (ProductoCarrito p : carrito.getListaProductos()){
            Producto producto = controladora.buscarProductoPorId(p.getIdProducto());

            if (producto != null){
                lista.add(new ProductoVista(p.getIdProducto(),p.getCantidad(),producto.getNombre(),producto.getPrecio()));
            }

        }

        BigDecimal total = controladora.calcularPrecioTotal(lista);

        modelo.put("lista", lista);
        modelo.put("total", total);
        //enviando al sistema de plantilla.
        ctx.render("/templates/crud/carritoCompra.html", modelo);
    }

    public static void LimpiarCarrito(@NotNull Context ctx) {
        ctx.sessionAttribute("carrito",new CarroCompra());

        ctx.redirect("/crud-producto/carrito");
    }

    public static void EliminarProductoCarrito(@NotNull Context ctx) {
        CarroCompra carrito = ctx.sessionAttribute("carrito");
        int id = Integer.parseInt(ctx.pathParam("id"));

        if (carrito != null){
            controladora.eliminarProductoCarrito(carrito,id);
            ctx.sessionAttribute("carroto",carrito);
        }

        ctx.redirect("/crud-producto/carrito");
    }

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
}
