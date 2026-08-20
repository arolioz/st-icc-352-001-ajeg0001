package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UsuarioControladora {
    public static void procesarLogin(@NotNull Context ctx) {
        String usuario = ctx.formParam("usuario");
        String password = ctx.formParam("password");

        Usuario u = UsuarioService.autenteificarUsuario(usuario,password);

        if (u == null) {
            ctx.status(401).result("Usuario o Contrasena incorrectos");
            return;
        }

        IO.println(u.getId());
        IO.println(u.getPassword());

    }
}
