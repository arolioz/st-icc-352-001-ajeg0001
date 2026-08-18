package Pucmm.eict.edu.Services;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.config.DbConfig;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<Usuario> listarUsuarios() throws Exception {

        Datastore ds = DbConfig.getDatastore();

        List<Usuario> usuarios = ds.find(Usuario.class).iterator().toList();

        IO.println("ELIMINAR DEBUG DESPUES -> Usuarios: ");
        for (Usuario u: usuarios){
            IO.println(u.toString());
        }

        return usuarios;
    }

    public Usuario obtenerUsuario(ObjectId id){
        Datastore ds = DbConfig.getDatastore();

        Usuario u = ds.find(Usuario.class).filter(Filters.eq("_id", id)).first();

        assert u != null;
        IO.println(u.toString());

        return u;
    }
    public void eliminarUsuario(ObjectId id){
        Datastore ds = DbConfig.getDatastore();

        Usuario u = ds.find(Usuario.class).filter(Filters.eq("_id", id)).first();

        if (u == null){
            IO.println("Usuario no encontrado");
            return;
        }

        ds.find(Usuario.class).filter(Filters.eq("_id", id)).delete();

        IO.println("Usuario eliminado" + u.toString());

    }



    public Usuario modificarUsuario(ObjectId id, String nuevoUser) {
        Datastore ds = DbConfig.getDatastore();

        Usuario u = ds.find(Usuario.class).filter(Filters.eq("_id", id)).first();
        if (u == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        u.setUser(nuevoUser);

        try {
            return ds.save(u);
        } catch (MongoWriteException e) {
            if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
            }
            throw e;
        }
    }
}
