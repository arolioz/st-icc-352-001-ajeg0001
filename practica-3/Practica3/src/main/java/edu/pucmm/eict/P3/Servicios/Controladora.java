package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.*;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Controladora {
    private static Controladora instancia;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private static List<Producto> listaProductos = new ArrayList<>();
    private List<VentaProductos> listaVentas = new ArrayList<>();
    private final static ProductoServices productoServices = ProductoServices.getInstancia();

    private static int contadorProductos = 1;
    private static int contadorVentas = 1;

    private Controladora(){
        listaUsuarios.add(new Usuario("admin","admin","admin"));
        listaProductos.add(new Producto(0,"Huevo",new BigDecimal(100)));
    }

    public static Controladora getInstance(){
        if (instancia == null){
            instancia = new Controladora();
        }
        return instancia;
    }

    public Producto agregarProducto(Producto producto){

        producto.setIdProducto(contadorProductos);
        listaProductos.add(producto);

        contadorProductos += 1;

        return producto;
    }


    public Producto eliminarProducto(int id){
        Producto tmp = buscarProductoPorId(id);

        if (tmp != null){
            listaProductos.remove(tmp);
        }

        return tmp;
    }

    public static Producto buscarProductoPorId(int id){


        Producto p = ProductoServices.getInstancia().find(id);
        return p;
    }

    public List<Producto> listarProductos(){
        return listaProductos;
    }

    public CarroCompra agregandoProductoACarrito(CarroCompra carrito, int idProducto, int cantidad){
        Producto producto = buscarProductoPorId(idProducto);

        if (producto != null){
            carrito.agregarProducto(producto,cantidad);
        }

        return carrito;

    }

    public Usuario ValidarUsuario(String usuario, String password){

        for (Usuario u : listaUsuarios){
            if (u.getUsuario().equals(usuario) && u.getPassword().equals(password)){
                return u;
            }
        }

        return null;
    }

    public void modificarProducto(int id, String nombre, BigDecimal precio) {
        for (Producto p : listaProductos){
            if (id == p.getIdProducto()){
                p.setPrecio(precio);
                p.setNombre(nombre);
            }
        }
    }

    public BigDecimal calcularPrecioTotal(List<ProductoVista> productos) {
        BigDecimal n = BigDecimal.ZERO;

        if (productos != null) {
            for (ProductoVista pv : productos) {
                n = n.add(
                        pv.getPrecio().multiply(
                                BigDecimal.valueOf(pv.getCantidad())
                        )
                );
            }
        }
        return n;
    }

    public void eliminarProductoCarrito(CarroCompra carrito, int id) {
        if (carrito != null) {
            carrito.getListaProductos()
                    .removeIf(p -> p.getIdProducto() == id);
        }
    }

    public static int cantProductosCarrito(CarroCompra carrito){
        int n = 0;

        for (ProductoCarrito p : carrito.getListaProductos()){
            if (buscarProductoPorId(p.getIdProducto()) != null){
                n += p.getCantidad();
            }
        }

        return n;
    }

    public void procesarCompra(CarroCompra carrito,String nombreCliente) {
        List<ProductoVista> lista = new ArrayList<>();

        for (ProductoCarrito p : carrito.getListaProductos()){
            Producto producto = buscarProductoPorId(p.getIdProducto());

            if (producto != null){
                lista.add(new ProductoVista(p.getIdProducto(),p.getCantidad(),producto.getNombre(),producto.getPrecio()));
            }
        }

        if (!lista.isEmpty()){
            crearVenta(lista,nombreCliente);
        }
    }
    public void crearVenta(List<ProductoVista> lista, String nombreCliente){
        VentaProductos venta = new VentaProductos();
        venta.setListaProductos(lista);
        venta.setNombreCliente(nombreCliente);
        venta.setId(contadorVentas);

        listaVentas.add(venta);

        contadorVentas += 1;
    }

    public Map<String, Object> construirModeloBase(Context ctx){
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
            cantProductos = cantProductosCarrito(carrito);
        }

        modelo.put("usuario",user);
        modelo.put("cantCarrito",cantProductos);
        modelo.put("deshabilitado",deshabilitado);

        return modelo;
    }

    public List<VentaProductos> listarVentas() {return listaVentas;}
}
