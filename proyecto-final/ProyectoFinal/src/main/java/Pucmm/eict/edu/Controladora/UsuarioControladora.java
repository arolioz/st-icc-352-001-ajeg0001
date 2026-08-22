package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.Services.UsuarioService;
import Pucmm.eict.edu.Util.RolesApp;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static Pucmm.eict.edu.Main.LLAVE_SECRETA;

public class UsuarioControladora {
    public static void procesarLogin(@NotNull Context ctx) {
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String user = body.get("usuario");
        String password = body.get("password");
        Usuario u = UsuarioService.getInstancia().autenteificarUsuario(user,password);

        if (u == null) {
            IO.println("ERROR GARRAFAL");
            ctx.status(401).result("Usuario o Contrasena incorrectos");
            return;
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("token", JwtControladora.generarToken(u));

        Map<String, Object> usuario = new LinkedHashMap<>();
        usuario.put("usuario", u.getUser());
        usuario.put("id", u.getId().toHexString());
        usuario.put("roles", u.getListaRoles() == null ? List.of() : u.getListaRoles());

        respuesta.put("usuario", usuario);

        ctx.json(respuesta);
    }

    public static void crearUsuario(@NotNull Context ctx){
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String user = body.get("usuario");
        String password = body.get("password");
        UsuarioService.getInstancia().crearUsuario(user,password);
    }

    public static void eliminarUsuario(@NotNull Context ctx){
        String id = ctx.pathParam("id");

        UsuarioService.getInstancia().eliminarUsuario(new ObjectId(id));
    }

    public static void listarUsuarios(@NotNull Context ctx) throws Exception {
        ctx.json(UsuarioService.getInstancia().listarUsuarios());
    }

    public static void cambiarRolEncuestador(@NotNull Context ctx){


        String idUsuario = ctx.pathParam("id");

        Usuario usuario = UsuarioService.getInstancia().obtenerUsuario(new ObjectId(idUsuario));

        assert usuario != null;

        if (usuario.getListaRoles().contains(RolesApp.ROLE_ADMIN)){
            ctx.status(403).result("No se pueden cambiar los roles de un administrador");
            return;
        }
        if (usuario.getListaRoles().contains(RolesApp.ROLE_ENCUESTADOR)) {
            usuario.getListaRoles().remove(RolesApp.ROLE_ENCUESTADOR);
        } else {
            usuario.getListaRoles().add(RolesApp.ROLE_ENCUESTADOR);
        }

        UsuarioService.getInstancia().modificarRoles(new ObjectId(idUsuario), usuario.getListaRoles());
    }
}
