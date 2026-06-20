package edu.pucmm.eict.P2.Service;

import edu.pucmm.eict.P2.Logico.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Controladora {
    private static Controladora instancia;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<Producto> listaProductos = new ArrayList<>();
    private List<VentaProductos> listaVentas = new ArrayList<>();

    private static int contadorProductos = 1;

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

    public Producto buscarProductoPorId(int id){


        for (Producto p : listaProductos){
            if (p.getIdProducto() == id){
                return p;
            }
        }
        return null;
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


}
