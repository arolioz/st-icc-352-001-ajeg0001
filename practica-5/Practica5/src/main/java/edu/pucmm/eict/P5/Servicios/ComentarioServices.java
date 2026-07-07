package edu.pucmm.eict.P5.Servicios;

import edu.pucmm.eict.P5.Entidades.Comentario;

public class ComentarioServices extends GestionDb<Comentario>{
    private static ComentarioServices instancia;

    private ComentarioServices(){
        super(Comentario.class);
    }

    public static ComentarioServices getInstancia(){
        if(instancia==null){
            instancia = new ComentarioServices();
        }
        return instancia;
    }
}
