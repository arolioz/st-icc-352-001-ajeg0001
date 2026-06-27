package edu.pucmm.eict.servicios;



import edu.pucmm.eict.encapsulaciones.Estudiante;
import org.h2.util.MemoryEstimator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para encapsular los metodos de CRUD
 * Created by vacax on 30/04/16.
 */
public class EstudianteServices {

    private static EstudianteServices instancia;

    private EstudianteServices(){

    }

    public static EstudianteServices getInstancia(){
        if(instancia==null){
            instancia = new EstudianteServices();
        }
        return instancia;
    }

    /**
     * Listado de los estudiantes.
     * @return
     */
    public static List<Estudiante> listaEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select * from estudiante";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Estudiante est = new Estudiante();
                est.setMatricula(rs.getInt("matricula"));
                est.setNombre(rs.getString("nombre"));
                est.setCarrera(rs.getString("carrera"));


                lista.add(est);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    /**
     * Retorna un estudiante dado su matricula
     * @param matricula
     * @return
     */
    public Estudiante getEstudiantePorMatricula(int matricula) {
        Estudiante est = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from estudiante where matricula = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, matricula);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                est = new Estudiante();
                est.setMatricula(rs.getInt("matricula"));
                est.setNombre(rs.getString("nombre"));
                est.setCarrera(rs.getString("carrera"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return est;
    }

    /**
     * 
     * @param est
     * @return
     */
    public boolean crearEstudiante(Estudiante est){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into estudiante(matricula, nombre, carrera) values(?,?,?)";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, est.getMatricula());
            prepareStatement.setString(2, est.getNombre());
            prepareStatement.setString(3, est.getCarrera());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean actualizarEstudiante(Estudiante est){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update estudiante set nombre=?, carrera=? where matricula = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, est.getNombre());
            prepareStatement.setString(2, est.getCarrera());
            //Indica el where...
            prepareStatement.setInt(3, est.getMatricula());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean eliminandoEstudiante(int matricula){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete from estudiante where matricula = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, matricula);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudianteServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

}