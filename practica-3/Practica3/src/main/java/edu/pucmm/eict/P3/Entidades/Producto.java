package edu.pucmm.eict.P3.Entidades;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
public class Producto {

    private int idProducto;
    private String nombre;
    private BigDecimal precio;

    public Producto() {

    }

    public Producto(int idProducto, String nombre, BigDecimal precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getIdProducto() {return idProducto;}

    public String getNombre() {return nombre;}

    public BigDecimal getPrecio() {return precio;}

    public void setIdProducto(int idProducto) {this.idProducto = idProducto;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setPrecio(BigDecimal precio) {this.precio = precio;}
}