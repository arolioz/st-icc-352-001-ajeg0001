package edu.pucmm.eict.P3.Logico;

import java.util.ArrayList;
import java.util.List;

public class CarroCompra {

    private int id;
    private List<ProductoCarrito> listaProductos;


    public CarroCompra(int id) {
        this.id = id;
        listaProductos = new ArrayList<>();
    }

    public CarroCompra() {
        listaProductos = new ArrayList<>();
    }

    public int getId() {return id;}

    public List<ProductoCarrito> getListaProductos() {return listaProductos;}

    public void setId(int id) {this.id = id;}

    public void setListaProductos(List<ProductoCarrito> listaProductos) {this.listaProductos = listaProductos;}

    public void agregarProducto(Producto producto, int cantidad){
        boolean encontrado = false;



        for (ProductoCarrito p : listaProductos){
            if (producto.getIdProducto() == p.getIdProducto()){
                int n = p.getCantidad();

                p.setCantidad(n + cantidad);

                encontrado = true;
                break;
            }
        }

        if (!encontrado){
            listaProductos.add(new ProductoCarrito(producto.getIdProducto(),cantidad));
        }
    }

    public int cantProductos(){
        int n = 0;
        for (ProductoCarrito p : listaProductos){
            n += p.getCantidad();
        }

        return n;
    }
}