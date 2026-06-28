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
    private String descripcion;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "producto",cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

    public Producto() {
        this.fotosProducto = new ArrayList<>();
    }

    public Producto(int idProducto, String nombre, BigDecimal precio, String descripcion) {
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

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String d) {this.descripcion = d;}

    public void setFotosProducto(List<Foto> fotos){
        this.fotosProducto = fotos;
    }

    public List<Comentario> getComentarios() {return comentarios;}

    public void setComentarios(List<Comentario> c) {this.comentarios = c;}

    public List<Foto> getFotosProducto() {return fotosProducto;}

    public void addComentario(Comentario comentario){
        comentarios.add(comentario);
    }
}