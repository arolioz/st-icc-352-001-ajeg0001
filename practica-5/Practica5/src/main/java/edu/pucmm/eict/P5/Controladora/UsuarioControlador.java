package edu.pucmm.eict.P5.Controladora;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.pucmm.eict.P5.Entidades.Usuario;
import edu.pucmm.eict.P5.Servicios.Encriptador;
import edu.pucmm.eict.P5.Servicios.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinGson;
import org.jetbrains.annotations.NotNull;


import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UsuarioControlador {

    private final static UsuarioServices usuarioService = UsuarioServices.getInstancia();

    public static void procesarLogin(@NotNull Context ctx) {
        String usuario = ctx.formParam("usuario");
        String password = ctx.formParam("password");
        String rememberUser = ctx.formParam("rememberUser");

        Usuario user = ValidarUsuario(usuario,password);

        if (user != null){
            ctx.sessionAttribute("usuario", user);

            if (rememberUser != null){
                IO.println("remember");
                Gson gson = new Gson();

                Map<String, Object> datosUsuario = new HashMap<>();
                datosUsuario.put("id", user.getId());
                datosUsuario.put("usuario", user.getUsuario());

                String jsonUser = gson.toJson(datosUsuario);

                String encryptedUsuario = Encriptador.encrypt(jsonUser);

                String encodedUser = URLEncoder.encode(encryptedUsuario, StandardCharsets.UTF_8);



                ctx.cookie("usuario",encodedUser, 60 * 60 * 24 * 7);
            }
            else{
                IO.println("noremember");
                ctx.removeCookie("usuario");
            }

            UsuarioServices.getInstancia().guardarHistorialLogin(usuario);
            ctx.redirect("/");
        }
        else {
            ctx.redirect("/login.html");
        }
    }

    private static Usuario ValidarUsuario(String usuario, String password){
        Usuario u = usuarioService.findUsuarioPorUsuario(usuario, password);

        return  u;
    }

    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.removeCookie("usuario");

        ctx.redirect("/");
    }

    public static void recordarUsuario(@NotNull Context ctx){
        if (ctx.sessionAttribute("usuario") == null){
            String usuarioCookie = ctx.cookie("usuario");



            if (usuarioCookie != null){
                String encryptedUsuario = URLDecoder.decode(usuarioCookie, StandardCharsets.UTF_8);

                String json = Encriptador.decrypt(encryptedUsuario);

                Type type = new TypeToken<Map<String, Object>>() {}.getType();

                Map<String, Object> datos = new Gson().fromJson(json, type);

                String usuario = (String) datos.get("usuario");
                int id = ((Double) datos.get("id")).intValue();

                Usuario validatedUser = usuarioService.find(id);

                if (validatedUser != null){
                    ctx.sessionAttribute("usuario", validatedUser);
                }
            }
        }
    }

    public static boolean verificarUsuario(Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");

        if (usuario != null && usuario.getUsuario().equals("admin") && usuario.getPassword().equals("admin")) {
            return true;
        }
        return false;
    }

    public static void esAdministrador(Context ctx) {

        try {
            boolean esAdmin = verificarUsuario(ctx);

            ctx.json(esAdmin);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
