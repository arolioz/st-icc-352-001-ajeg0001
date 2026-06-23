package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class ProductoServices extends GestionDb<Producto>{
    private static ProductoServices instancia;

    public ProductoServices() {super(Producto.class);}

    public static ProductoServices getInstancia(){
        if (instancia == null){
            instancia = new ProductoServices();
        }
        return instancia;
    }



}
