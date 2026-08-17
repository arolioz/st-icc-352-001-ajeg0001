package Pucmm.eict.edu.Services;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.config.DbConfig;
import dev.morphia.Datastore;
import dev.morphia.config.MorphiaConfig;
import org.mindrot.jbcrypt.BCrypt;

public class DbService {
    public static void inicializar() {
        Datastore ds = DbConfig.getDatastore();

        long total = ds.find(Usuario.class).count();
        if (total == 0) {
            String claveAdmin = System.getenv()
                    .getOrDefault("ADMIN_PASSWORD", "admin");

            ds.save(new Usuario("admin", BCrypt.hashpw(claveAdmin, BCrypt.gensalt(12))));

            ds.save(new Usuario("Test", BCrypt.hashpw("test", BCrypt.gensalt(12))));

            System.out.println("Base de datos creada con el usuario: admin");
        } else {
            System.out.println("Base de datos encontrada");
        }
    }

    public static boolean probarConexion() {
        try {
            DbConfig.getDatastore().find(Usuario.class).count();
            return true;
        } catch (Exception e) {
            System.err.println("[Init] Fallo la conexion a Mongo: " + e.getMessage());
            return false;
        }
    }
}
