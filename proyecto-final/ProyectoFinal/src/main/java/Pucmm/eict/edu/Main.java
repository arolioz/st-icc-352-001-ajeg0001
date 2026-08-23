package Pucmm.eict.edu;

import Pucmm.eict.edu.Controladora.EncuestaControladora;
import Pucmm.eict.edu.Controladora.UsuarioControladora;
import Pucmm.eict.edu.Entidades.Encuesta;
import Pucmm.eict.edu.Services.DbService;
import Pucmm.eict.edu.Services.EncuestaServices;
import Pucmm.eict.edu.Services.UsuarioService;
import Pucmm.eict.edu.Util.RolesApp;
import Pucmm.eict.edu.grpc.GrpcServidor;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static Pucmm.eict.edu.Controladora.EncuestaControladora.numero;
import static Pucmm.eict.edu.Controladora.EncuestaControladora.texto;
import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.http.ContentType.JSON;

public class Main {
    public static final String LLAVE_SECRETA = "ejemplo_de_llave_generada_icc35210231213123123";
    public static final SecretKey LLAVE = Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes());

    void main() throws IOException {
        DbService.inicializar();
        GrpcServidor.iniciar(9090);

        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.jetty.modifyWebSocketServletFactory(factory -> {
                factory.setMaxTextMessageSize(5 * 1024 * 1024);      // 5 MB
                factory.setIdleTimeout(java.time.Duration.ofMinutes(10));
            });

            config.routes.before("/api/*", Main::filtroJwt);
            config.routes.get("/", ctx -> {ctx.redirect("crudFormulario.html");});
            config.routes.apiBuilder(() ->{

                ws("/sincronizacion", ws -> {
                    ws.onConnect(ctx -> {
                        String token = ctx.queryParam("token");

                        if (token == null || token.isBlank()) {
                            ctx.closeSession(4001, "Falta el token");
                            return;
                        }

                        try {
                            Claims claims = Jwts.parser()
                                    .verifyWith(Main.LLAVE)
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload();

                            ctx.attribute("usuarioId", claims.getSubject());
                            ctx.attribute("usuarioNombre", claims.get("user", String.class));

                            System.out.println("[ws] Conectado: " + claims.get("user", String.class));
                            ctx.send("Bienvenido " + claims.get("user", String.class));

                        } catch (Exception e) {
                            ctx.closeSession(4001, "Token invalido o expirado");
                        }
                    });

                    ws.onMessage(ctx -> {
                        try {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> m = ctx.messageAsClass(Map.class);

                            String uuid = texto(m.get("uuid"));

                            if (uuid == null || uuid.isBlank()) {
                                ctx.send("{\"ok\":false,\"motivo\":\"falta el uuid\"}");
                                return;
                            }

                            Encuesta e = new Encuesta();
                            e.setUuid(uuid);
                            e.setNombre(texto(m.get("nombre")));
                            e.setSector(texto(m.get("sector")));
                            e.setNivelEscolar(texto(m.get("nivelEscolar")));
                            e.setLatitud(numero(m.get("latitud")));
                            e.setLongitud(numero(m.get("longitud")));
                            e.setFotoBase64(texto(m.get("fotoBase64")));

                            e.setUsuarioId(new ObjectId(Objects.requireNonNull(ctx.attribute("usuarioId")).toString()));
                            e.setUsuarioNombre(ctx.attribute("usuarioNombre"));
                            e.setFechaRegistro(Instant.now());

                            var r = EncuestaServices.getInstancia().crear(e);

                            ctx.send(Map.of(
                                    "uuid", uuid,
                                    "ok", true,
                                    "id", r.encuesta().getId().toHexString(),
                                    "estado", r.yaExistia() ? "ya_existia" : "creada"
                            ));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ctx.send("{\"ok\":false,\"motivo\":\"" + ex.getMessage() + "\"}");
                        }
                    });

                    ws.onClose(ctx -> System.out.println("[ws] Cerrada"));
                });

                path("/api", () -> {
                    path("/login", () -> {
                        post(UsuarioControladora::procesarLogin);
                    });
                    path("/encuesta", () -> {
                        get(EncuestaControladora::listarEncuestas, RolesApp.ROLE_ADMIN);
                        post(EncuestaControladora::crearEncuesta,RolesApp.ROLE_ENCUESTADOR,RolesApp.ROLE_ADMIN);
                        get("/usuario/{usuarioId}",EncuestaControladora::obtenerEncuestasUsuario,RolesApp.ROLE_ENCUESTADOR,RolesApp.ROLE_ADMIN);
                        path("/{id}", () -> {
                            delete(EncuestaControladora::eliminarEncuesta,RolesApp.ROLE_ADMIN);
                            get(EncuestaControladora::obtenerEncuestaById,RolesApp.ROLE_ENCUESTADOR,RolesApp.ROLE_ADMIN);
                            put(EncuestaControladora::modificarEncuesta,RolesApp.ROLE_ENCUESTADOR,RolesApp.ROLE_ADMIN);
                        });
                    });
                    path("/usuario", () -> {
                        get(UsuarioControladora::listarUsuarios, RolesApp.ROLE_ADMIN);
                        post(UsuarioControladora::crearUsuario,RolesApp.ROLE_ADMIN);
                        path("/{id}", () -> {
                            delete(UsuarioControladora::eliminarUsuario,RolesApp.ROLE_ADMIN);
                            put(UsuarioControladora::cambiarRolEncuestador,RolesApp.ROLE_ADMIN);
                        });
                    });
                });
            });
        });

        app.start(7000);
    }

    private static void filtroJwt(Context ctx) {
        Set<RouteRole> permitidos = ctx.routeRoles();

        if (ctx.method() == HandlerType.OPTIONS) return;
        if (ctx.path().equals("/api/login")) return;

        String header = ctx.header("Authorization");
        String prefijo = "Bearer ";

        if (header == null || !header.startsWith(prefijo)) {
            throw new UnauthorizedResponse("Debe autenticarse");
        }

        List<String> rolesToken;
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(LLAVE)
                    .build()
                    .parseSignedClaims(header.substring(prefijo.length()).trim())
                    .getPayload();

            rolesToken = claims.get("roles", List.class);

            ctx.attribute("jwt-claims", claims);
            ctx.attribute("usuarioId", claims.getSubject());
            ctx.attribute("roles", rolesToken);

        } catch (ExpiredJwtException e) {
            throw new UnauthorizedResponse("El token expiro, vuelva a iniciar sesion");
        } catch (JwtException e) {
            throw new UnauthorizedResponse("Token invalido");
        }


        if (permitidos.isEmpty()) return;

        boolean autorizado = permitidos.stream()
                .map(r -> ((RolesApp) r).name())
                .anyMatch(rolesToken::contains);

        if (!autorizado) {
            ctx.status(401);
            throw new ForbiddenResponse("No tiene permisos para esta operacion");
        }
    }
}
