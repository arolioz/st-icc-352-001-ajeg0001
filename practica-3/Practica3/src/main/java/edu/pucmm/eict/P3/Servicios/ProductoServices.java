package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.CarroCompra;
import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoServices extends GestionDb<Producto>{
    private static ProductoServices instancia;

    public ProductoServices() {super(Producto.class);}

    public static ProductoServices getInstancia(){
        if (instancia == null){
            instancia = new ProductoServices();
        }
        return instancia;
    }

    public List<Producto> getProductosPaginacion(int pagina) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Producto> query = em.createQuery(
                    "FROM Producto",
                    Producto.class
            );

            int tamanoPagina = 10;

            query.setFirstResult((pagina - 1) * tamanoPagina);
            query.setMaxResults(tamanoPagina);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public long contarProductos() {
        EntityManager em = getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Producto p",
                    Long.class
            ).getSingleResult();

        } finally {
            em.close();
        }
    }

}
