package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Encuesta;
import Pucmm.eict.edu.Services.EncuestaServices;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.jsonwebtoken.Claims;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class EncuestaControladora {
    public static void listarEncuestas(@NotNull Context ctx) throws Exception {
        List<Encuesta> encuestas = EncuestaServices.getInstancia().listarEncuestas();

        ctx.json(encuestas);
    }

    public static void crearEncuesta(@NotNull Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String uuid = texto(body.get("uuid"));
        if (uuid == null || uuid.isBlank()) {
            throw new BadRequestResponse("Falta uuid");
        }

        // el autor sale del token, nunca del cuerpo
        String usuarioId = ctx.attribute("usuarioId");
        Claims claims = ctx.attribute("jwt-claims");
        String usuarioNombre = (claims == null) ? null : claims.get("user", String.class);

        Encuesta e = new Encuesta();
        e.setUuid(uuid);
        e.setNombre(texto(body.get("nombre")));
        e.setSector(texto(body.get("sector")));
        e.setNivelEscolar(texto(body.get("nivelEscolar")));
        e.setLatitud(numero(body.get("latitud")));
        e.setLongitud(numero(body.get("longitud")));
        e.setFotoBase64(texto(body.get("fotoBase64")));
        e.setUsuarioId(new ObjectId(usuarioId));
        e.setUsuarioNombre(usuarioNombre);
        e.setFechaRegistro(Instant.now());

        Encuesta guardada = EncuestaServices.getInstancia().crear(e).encuesta();

        ctx.status(201);
        ctx.json(Map.of(
                "estado", "creada",
                "id", guardada.getId().toHexString(),
                "uuid", guardada.getUuid()
        ));

    }

    private static String texto(Object o) {
        return o == null ? null : o.toString();
    }

    private static Double numero(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static void obtenerEncuestasUsuario(@NotNull Context ctx) throws Exception {
        String idUsuario = ctx.pathParam("usuarioId");

        ObjectId id = new ObjectId(idUsuario);

        List<Encuesta> encuestas = EncuestaServices.getInstancia().listarPorUsuario(id);

        if (encuestas != null){
            ctx.json(encuestas);
        }
    }

    public static void eliminarEncuesta(@NotNull Context ctx) {
    }

    public static void obtenerEncuestaById(@NotNull Context ctx) {
    }

    public static void modificarEncuesta(@NotNull Context ctx) {
    }
}
