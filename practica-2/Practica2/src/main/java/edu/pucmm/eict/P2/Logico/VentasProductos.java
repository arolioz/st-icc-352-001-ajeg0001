package edu.pucmm.eict.P2.Logico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentasProductos {

    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private List<ProductoCarrito> listaProductos;

    public VentasProductos() {
        fechaCompra = new Date();
        listaProductos = new ArrayList<>();
    }

    public VentasProductos(long id, String nombreCliente) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.fechaCompra = new Date();
        this.listaProductos = new ArrayList<>();
    }

    public long getId() {return id;}

    public Date getFechaCompra() {return fechaCompra;}

    public String getNombreCliente() {return nombreCliente;}

    public List<ProductoCarrito> getListaProductos() {return listaProductos;}

    public void setId(long id) {this.id = id;}

    public void setFechaCompra(Date fechaCompra) {this.fechaCompra = fechaCompra;}

    public void setNombreCliente(String nombreCliente) {this.nombreCliente = nombreCliente;}

    public void setListaProductos(List<ProductoCarrito> listaProductos) {this.listaProductos = listaProductos;}
}