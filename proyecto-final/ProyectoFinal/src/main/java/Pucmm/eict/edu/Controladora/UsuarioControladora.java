package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.http.Context;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


}
