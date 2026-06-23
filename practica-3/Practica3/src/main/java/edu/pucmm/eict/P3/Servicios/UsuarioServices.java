package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Entidades.VentaProductos;

public class UsuarioServices extends GestionDb<Usuario>{
    private static UsuarioServices instancia;

    public UsuarioServices() {super(Usuario.class);}

    public static UsuarioServices getInstancia(){
        if (instancia == null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }

}
