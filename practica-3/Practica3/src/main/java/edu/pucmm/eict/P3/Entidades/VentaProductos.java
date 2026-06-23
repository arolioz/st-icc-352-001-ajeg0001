package edu.pucmm.eict.P3.Entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class VentaProductos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL)
    private List<ProductoVista> listaProductos;

    public VentaProductos() {
        fechaCompra = new Date();
        listaProductos = new ArrayList<>();
    }

    public VentaProductos(long id, String nombreCliente) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.fechaCompra = new Date();
        this.listaProductos = new ArrayList<>();
    }

    public long getId() {return id;}

    public Date getFechaCompra() {return fechaCompra;}

    public String getNombreCliente() {return nombreCliente;}

    public List<ProductoVista> getListaProductos() {return listaProductos;}

    public void setId(long id) {this.id = id;}

    public void setFechaCompra(Date fechaCompra) {this.fechaCompra = fechaCompra;}

    public void setNombreCliente(String nombreCliente) {this.nombreCliente = nombreCliente;}

    public void setListaProductos(List<ProductoVista> listaProductos) {this.listaProductos = listaProductos;}

    public BigDecimal getTotal(){
        BigDecimal n = BigDecimal.ZERO;


        if (!listaProductos.isEmpty()){
            for (ProductoVista pv : listaProductos) {
                n = n.add(
                        pv.getPrecio().multiply(
                                BigDecimal.valueOf(pv.getCantidad())
                        )
                );
            }
        }
        
        return n;
    }

    public void addProducto(ProductoVista pv) {
        listaProductos.add(pv);
        pv.setVenta(this);
    }
}