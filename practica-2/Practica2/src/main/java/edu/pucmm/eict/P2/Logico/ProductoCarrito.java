package edu.pucmm.eict.P2.Logico;

public class ProductoCarrito {
    private int idCarrito;
    private int idProducto;
    private int cantidad;

    public ProductoCarrito(int idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getIdCarrito() {return idCarrito;}

    public int getIdProducto() {return idProducto;}

    public int getCantidad() {return cantidad;}

    public void setIdCarrito(int id) {this.idCarrito = id;}

    public void setIdProducto(int id) {this.idProducto = id;}

    public void setCantidad(int n) {this.cantidad = n;}

}
