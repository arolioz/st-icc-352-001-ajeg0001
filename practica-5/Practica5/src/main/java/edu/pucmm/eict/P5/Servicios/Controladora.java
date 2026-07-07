package edu.pucmm.eict.P5.Servicios;

import edu.pucmm.eict.P5.Entidades.*;
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
        listaProductos.add(new Producto(0,"Huevo",new BigDecimal(100),"Test"));
    }

    public static Controladora getInstance(){
        if (instancia == null){
            instancia = new Controladora();
        }
        return instancia;
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


    public static int cantProductosCarrito(CarroCompra carrito){
        int n = 0;

        for (ProductoCarrito p : carrito.getListaProductos()){
            if (buscarProductoPorId(p.getIdProducto()) != null){
                n += p.getCantidad();
            }
        }

        return n;
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

    public void initData(){

        if (UsuarioServices.getInstancia().findAll().isEmpty()){
            Usuario admin = new Usuario("admin","admin","admin");
            Usuario test = new Usuario("test","test","test");


            UsuarioServices.getInstancia().crear(admin);
            UsuarioServices.getInstancia().crear(test);
        }

        if (ProductoServices.getInstancia().findAll().isEmpty()){
            Producto p = new Producto();
            p.setPrecio(new BigDecimal(10));
            p.setNombre("Harina");

            ProductoServices.getInstancia().crear(p);
        }

    }

    public List<VentaProductos> listarVentas() {return listaVentas;}
}
