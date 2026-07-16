package edu.pucmm.eict.P2.Services;

import edu.pucmm.eict.P2.Entidades.Usuario;
import jakarta.persistence.EntityManager;

public class UsuarioServices extends GestionDb<Usuario>{
    private static UsuarioServices instancia;

    public UsuarioServices() {super(Usuario.class);}

    public static UsuarioServices getInstancia(){
        if (instancia == null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public Usuario findUsuarioPorUsuarioYPassword(String usuario, String password) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("Usuario.findByUsuarioAndPassword", Usuario.class)
                    .setParameter("usuario", usuario)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Usuario findUsuarioPorUsuario(String usuario) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("Usuario.findByUsuarioAndPassword", Usuario.class)
                    .setParameter("usuario", usuario)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
