package edu.pucmm.eict.P2.Logico;

import java.util.ArrayList;
import java.util.List;

public class CarroCompra {

    private int id;
    private List<ProductoCarrito> listaProductos;


    public CarroCompra(int id) {
        this.id = id;listaProductos = new ArrayList<>();
    }

    public int getId() {return id;}

    public List<ProductoCarrito> getListaProductos() {return listaProductos;}

    public void setId(int id) {this.id = id;}

    public void setListaProductos(List<ProductoCarrito> listaProductos) {this.listaProductos = listaProductos;}
}