package edu.pucmm.eict.P5.Entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ProductoVista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int idProducto;
    private int cantidad;
    private String nombre;
    private BigDecimal precio;
    @ManyToOne
    private VentaProductos venta;

    public ProductoVista(int idProducto, int cantidad, String nombre, BigDecimal precio) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.precio = precio;
    }

    public ProductoVista() {

    }


    public int getIdProducto() {return idProducto;}

    public int getCantidad() {return cantidad;}

    public String getNombre(){return  nombre;}

    public BigDecimal getPrecio() {return precio;}

    public void setIdProducto(int id) {this.idProducto = id;}

    public void setCantidad(int n) {this.cantidad = n;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setPrecio(BigDecimal precio) {this.precio = precio;}

    public void setVenta(VentaProductos venta) {
        this.venta = venta;
    }
}
