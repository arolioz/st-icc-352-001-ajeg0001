package Pucmm.eict.edu.Services;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.config.DbConfig;
import dev.morphia.Datastore;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    private static UsuarioService instancia;

    public UsuarioService() {}

    public static UsuarioService getInstancia(){
        if (instancia == null){
            instancia = new UsuarioService();
        }
        return instancia;
    }

    public void crearUsuario(String user, String password){
        Datastore ds = DbConfig.getDatastore();

        if (user == null || password == null){
            return;
        }

        Usuario usuario = new Usuario(user, BCrypt.hashpw(password, BCrypt.gensalt(12)));

        ds.save(usuario);
    }
}
