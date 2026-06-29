package edu.pucmm.eict.P3.Servicios;

import edu.pucmm.eict.P3.Entidades.Producto;
import edu.pucmm.eict.P3.Entidades.Usuario;
import edu.pucmm.eict.P3.Entidades.VentaProductos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public Usuario findUsuarioPorUsuario(String usuario, String password) {
        EntityManager em = getEntityManager();

        try {
            return em.createNamedQuery("Usuario.findByUsuario", Usuario.class)
                    .setParameter("usuario", usuario)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void guardarHistorialLogin(String usuario){
        String sql = """
                INSERT INTO login(usuario, fecha)
                VALUES (?, ?)
                """;

        try(Connection con = GestionDBCockroach.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(LocalDateTime.now())
            );

            ps.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
