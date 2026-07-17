package edu.pucmm.eict.P2.Services;

import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.Usuario;
import jakarta.persistence.EntityManager;

public class EventoServices extends GestionDb<Evento>{
    private static EventoServices instancia;

    public EventoServices() {super(Evento.class);}

    public static EventoServices getInstancia(){
        if (instancia == null){
            instancia = new EventoServices();
        }
        return instancia;
    }
}
