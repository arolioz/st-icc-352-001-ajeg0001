package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.VentaProductos;

public class VentaServices extends GestionDb<VentaProductos>{
    private static VentaServices instancia;

    public VentaServices() {super(VentaProductos.class);}

    public static VentaServices getInstancia(){
        if (instancia == null){
            instancia = new VentaServices();
        }
        return instancia;
    }
}
