package edu.pucmm.eict.P2;

import Util.RolesApp;
import edu.pucmm.eict.P2.Entidades.Usuario;
import edu.pucmm.eict.P2.Services.BootStrapServices;
import edu.pucmm.eict.P2.Services.UsuarioServices;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class Main {
    void main() {

        BootStrapServices.getInstancia().init();

        EntityManager em = Persistence
                .createEntityManagerFactory("MiUnidadPersistencia")
                .createEntityManager();

        var app = Javalin.create(config ->{
            config.routes.get("/", ctx ->{
                ctx.result("Hola mundo!");
            });
        });

        crearDatosIniciales();
        app.start(7000);
    }

    public static void crearDatosIniciales(){
        List<Usuario> usuarios = UsuarioServices.getInstancia().findAll();

        if (usuarios.isEmpty()){
            Usuario u = new Usuario();

            u.setNombre("admin");
            u.setUsuario("admin");
            u.setPassword("admin");

            u.setListaRoles(Set.of(RolesApp.ROLE_ADMIN,RolesApp.ROLE_ORGANIZADOR,RolesApp.ROLE_USUARIO));

            UsuarioServices.getInstancia().crear(u);
        }

    }
}