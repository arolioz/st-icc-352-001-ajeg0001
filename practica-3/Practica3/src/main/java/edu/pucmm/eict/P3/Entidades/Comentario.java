package edu.pucmm.eict.P3.Entidades;

import jakarta.persistence.*;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String contenido;
    private String usuario;
    @ManyToOne
    private Producto producto;
    private boolean habilitado;

    public Comentario() {
        this.habilitado = true;
    }

    public Comentario(String contenido, String usuario, Producto producto) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.producto = producto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setHabilitado(boolean b) {
        this.habilitado = b;
    }

    public boolean getHabilitado(){
        return habilitado;
    }
}
