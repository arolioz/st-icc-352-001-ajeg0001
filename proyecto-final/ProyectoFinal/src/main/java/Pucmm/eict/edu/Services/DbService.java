package Pucmm.eict.edu.Services;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.Util.RolesApp;
import Pucmm.eict.edu.config.DbConfig;
import dev.morphia.Datastore;
import dev.morphia.config.MorphiaConfig;
import org.mindrot.jbcrypt.BCrypt;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DbService {
    public static void inicializar() {
        Datastore ds = DbConfig.getDatastore();

        long total = ds.find(Usuario.class).count();
        if (total == 0) {
            String claveAdmin = System.getenv()
                    .getOrDefault("ADMIN_PASSWORD", "admin");

            Usuario admin = new Usuario("admin", BCrypt.hashpw(claveAdmin, BCrypt.gensalt(12)));
            List<RolesApp> rolesAdmin = new ArrayList<>();

            admin.setListaRoles(Set.of(RolesApp.ROLE_ADMIN,RolesApp.ROLE_ENCUESTADOR,RolesApp.ROLE_USUARIO));

            ds.save(admin);

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
