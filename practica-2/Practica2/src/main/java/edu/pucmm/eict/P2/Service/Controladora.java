package edu.pucmm.eict.P2.Service;

import edu.pucmm.eict.P2.Logico.Producto;
import edu.pucmm.eict.P2.Logico.Usuario;
import edu.pucmm.eict.P2.Logico.VentaProductos;

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

}
