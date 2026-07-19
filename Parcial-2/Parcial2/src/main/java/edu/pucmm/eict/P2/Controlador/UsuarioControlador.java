package edu.pucmm.eict.P2.Controlador;

import Util.Encriptador;
import Util.RolesApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.EventoUsuario;
import edu.pucmm.eict.P2.Entidades.Usuario;
import edu.pucmm.eict.P2.Services.EventoServices;
import edu.pucmm.eict.P2.Services.EventoUsuarioServices;
import edu.pucmm.eict.P2.Services.UsuarioServices;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UsuarioControlador {

    public static void procesarLogin(@NotNull Context ctx) {
        String usuario = ctx.formParam("usuario");
        String password = ctx.formParam("password");
        String rememberUser = ctx.formParam("recordarUsuario");

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

            ctx.redirect("/");
        }
        else {
            ctx.redirect("/login.html");
        }
    }

    private static Usuario ValidarUsuario(String usuario, String password){
        Usuario u = UsuarioServices.getInstancia().findUsuarioPorUsuarioYPassword(usuario, password);

        return  u;
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

                Usuario validatedUser = UsuarioServices.getInstancia().find(id);

                if (validatedUser != null){
                    ctx.sessionAttribute("usuario", validatedUser);
                }
            }
        }
    }

    public static void cerrarSesion(@NotNull Context ctx) {
        ctx.sessionAttribute("usuario", null);
        ctx.removeCookie("usuario");

        ctx.redirect("/");
    }


    public static void listarUsuarios(@NotNull Context ctx) {
        List<Usuario> usuarios = UsuarioServices.getInstancia().findAll();

        ctx.json(usuarios);
    }

    public static void procesarRegistrar(@NotNull Context ctx){
        String usuario = ctx.formParam("usuario");
        String nombre  = ctx.formParam("nombre");
        String password = ctx.formParam("password");
        String institucion = ctx.formParam("institucion");
        String email = ctx.formParam("email");

        if (usuario == null || usuario.isBlank() ||
                password == null || password.isBlank() ||
                email == null || email.isBlank()) {

            ctx.status(400).result("No se pudo procesar el registro").redirect("/registrar");
            return;
        }

        IO.println(existeUsuario(usuario));

        if (!existeUsuario(usuario)){
            Usuario u = new Usuario();
            u.setUsuario(usuario);
            u.setNombre(nombre);
            u.setPassword(password);
            u.setInstitucion(institucion);
            u.setEmail(email);
            u.setListaRoles(Set.of(RolesApp.ROLE_USUARIO));

            UsuarioServices.getInstancia().crear(u);
            ctx.status(200);
            ctx.redirect("/login");
        }
        else{
            ctx.status(400).result("El usuario ya existe").redirect("/registrar");
        }
    }

    public static Boolean existeUsuario(String usuario) {
        Usuario tmpU = UsuarioServices.getInstancia().findUsuarioPorUsuario(usuario);

        IO.println(usuario);
        IO.println(tmpU);

        if (tmpU != null){
            return true;
        }
        else{
            return false;
        }
    }

    public static void cambiarRolOrganizador(@NotNull Context ctx){
        Usuario admin = ctx.sessionAttribute("usuario");

        long idUsuario = Long.parseLong(Objects.requireNonNull(ctx.pathParam("id")));

        if (admin != null && admin.getListaRoles().contains(RolesApp.ROLE_ADMIN)){
            Usuario usuario = UsuarioServices.getInstancia().find(idUsuario);

            assert usuario != null;
            if (usuario.getListaRoles().contains(RolesApp.ROLE_ORGANIZADOR)) {
                usuario.getListaRoles().remove(RolesApp.ROLE_ORGANIZADOR);
            } else {
                usuario.getListaRoles().add(RolesApp.ROLE_ORGANIZADOR);
            }

            UsuarioServices.getInstancia().editar(usuario);
        }
    }

    public static void cambiarEstadoUsuario(@NotNull Context ctx){
        Usuario admin = ctx.sessionAttribute("usuario");

        long idUsuario = Long.parseLong(Objects.requireNonNull(ctx.pathParam("id")));

        if (admin != null && admin.getListaRoles().contains(RolesApp.ROLE_ADMIN)){
            Usuario usuario = UsuarioServices.getInstancia().find(idUsuario);

            assert usuario != null;
            if (usuario.getListaRoles().contains(RolesApp.ROLE_BLOQUEADO)) {
                usuario.getListaRoles().remove(RolesApp.ROLE_BLOQUEADO);
            } else {
                usuario.getListaRoles().add(RolesApp.ROLE_BLOQUEADO);
            }

            UsuarioServices.getInstancia().editar(usuario);
        }
    }

    public static void mostrarQr(@NotNull Context ctx){
        Long idEvento = Long.valueOf(Objects.requireNonNull(ctx.pathParam("idEvento")));

        Usuario tmp = ctx.sessionAttribute("usuario");

        Usuario usuario = null;
        if (tmp != null){
            usuario = UsuarioServices.getInstancia().find(tmp.getId());
        }



        if ((usuario != null && usuario.getListaRoles().contains(RolesApp.ROLE_USUARIO))){
            EventoUsuario tmpEU = EventoUsuarioServices.getInstancia().findUsuarioEnEvento(usuario.getId(),idEvento);

            if (tmpEU != null) {
                try {
                    QRCodeWriter writer = new QRCodeWriter();

                    Gson gson = new Gson();

                    Map<String, Object> data = new HashMap<>();
                    data.put("eventId", tmpEU.getEvento().getId());
                    data.put("userId", tmpEU.getUsuario().getId());
                    data.put("token", tmpEU.getToken());

                    String qrContent = gson.toJson(data);

                    BitMatrix matrix = writer.encode(
                            qrContent,
                            BarcodeFormat.QR_CODE,
                            300,
                            300
                    );
                    BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "PNG", baos);

                    ctx.contentType("image/png");
                    ctx.result(baos.toByteArray());

                } catch (Exception e) {
                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
                    ctx.result("Error al generar el QR");
                }
            }
        }
    }
}
