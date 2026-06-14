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

    public void setIdCarrito(int id) {idCarrito = id;}

    public void setIdProducto(int id) {idProducto = id;}

    public void setCantidad(int n) {cantidad = n;}

}
