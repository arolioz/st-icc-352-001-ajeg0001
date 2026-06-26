package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.ormjpa.entidades.Foto;

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
