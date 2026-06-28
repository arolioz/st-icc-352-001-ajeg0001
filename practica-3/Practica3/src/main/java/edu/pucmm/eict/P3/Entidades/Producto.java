package edu.pucmm.eict.P3.Entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProducto;
    private String nombre;
    private BigDecimal precio;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "producto",cascade = CascadeType.ALL)
    private List<Foto> fotosProducto;

    public Producto() {
        this.fotosProducto = new ArrayList<>();
    }

    public Producto(int idProducto, String nombre, BigDecimal precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.fotosProducto = new ArrayList<>();
    }

    public int getIdProducto() {return idProducto;}

    public String getNombre() {return nombre;}

    public BigDecimal getPrecio() {return precio;}

    public void setIdProducto(int idProducto) {this.idProducto = idProducto;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setPrecio(BigDecimal precio) {this.precio = precio;}

    public void addFoto(Foto foto){
        fotosProducto.add(foto);
    }
}