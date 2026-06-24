package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Entidades.VentaProductos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class UsuarioServices extends GestionDb<Usuario>{
    private static UsuarioServices instancia;

    public UsuarioServices() {super(Usuario.class);}

    public static UsuarioServices getInstancia(){
        if (instancia == null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public List<Usuario> findUsuarioPorUsuario(String usuario, String password) {
        EntityManager em = getEntityManager();

        Query query = em.createNamedQuery("Usuario.findByUsuario");
        query.setParameter("usuario", "%"+usuario+"%");
        query.setParameter("password", "%"+password+"%");

        List<Usuario> u = query.getResultList();

        return u;
    }
}
