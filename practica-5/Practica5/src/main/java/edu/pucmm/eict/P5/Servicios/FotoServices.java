package edu.pucmm.eict.P5.Servicios;

import edu.pucmm.eict.P5.Entidades.Foto;

/**
 *
 */
public class FotoServices extends GestionDb<Foto> {

    private static FotoServices instancia;

    private FotoServices(){
        super(Foto.class);
    }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }

}
