package edu.pucmm.eict.P2.Services;

import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.EventoUsuario;
import edu.pucmm.eict.P2.Entidades.Usuario;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EventoUsuarioServices extends  GestionDb<EventoUsuario>{
    private static EventoUsuarioServices instancia;

    public EventoUsuarioServices() {super(EventoUsuario.class);}

    public static EventoUsuarioServices getInstancia(){
        if (instancia == null){
            instancia = new EventoUsuarioServices();
        }
        return instancia;
    }

    public EventoUsuario findUsuarioEnEvento(Long idUsuario, Long idEvento) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("EventoUsuario.findUsuarioEnEvento", EventoUsuario.class)
                    .setParameter("idEvento", idEvento)
                    .setParameter("idUsuario",idUsuario)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public EventoUsuario findRegistroToken(String token) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("EventoUsuario.findRegistroToken", EventoUsuario.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EventoUsuario> findAsistenciaEvento(Long idEvento) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("EventoUsuario.findAsistenciaEvento", EventoUsuario.class)
                    .setParameter("idEvento", idEvento)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EventoUsuario> findInscripcionesEvento(Long idEvento) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("EventoUsuario.findInscripcionesEvento", EventoUsuario.class)
                    .setParameter("idEvento", idEvento)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
