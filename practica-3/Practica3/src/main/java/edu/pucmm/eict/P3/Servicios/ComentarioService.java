package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.Comentario;
import edu.pucmm.eict.P3.Entidades.Foto;

public class ComentarioService extends GestionDb<Comentario>{
    private static ComentarioService instancia;

    private ComentarioService(){
        super(Comentario.class);
    }

    public static ComentarioService getInstancia(){
        if(instancia==null){
            instancia = new ComentarioService();
        }
        return instancia;
    }
}
