package Pucmm.eict.edu;

import Pucmm.eict.edu.Controladora.UsuarioControladora;
import Pucmm.eict.edu.Services.DbService;
import Pucmm.eict.edu.Services.UsuarioService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.security.SignatureException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    public static final String LLAVE_SECRETA = "ejemplo_de_llave_generada_icc352";

    void main(){
        DbService.inicializar();

        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "/Publico";
                staticFileConfig.hostedPath = "/";
            });

            config.routes.before("/api/*", Main::filtroJwt);

            config.routes.apiBuilder(() ->{
                path("/api", () -> {
                    path("/login", () -> {
                        post("/", UsuarioControladora::procesarLogin);
                    });
                    path("/encuesta", () -> {
                        get(ctx -> ctx.json("Esto es prueba"));
                    });
                });
            });
        });

        app.start(7001);
    }

    private static void filtroJwt(Context ctx) {
        System.out.println("Validando JWT en la petición...");

        // Permitir peticiones OPTIONS (preflight de CORS)
        if (ctx.method() == HandlerType.OPTIONS) {
            return;
        }
        if (ctx.path().startsWith("/api/login")) return;


        String headerAutenticacion = ctx.header("Authorization");
        String prefijo = "Bearer";

        if (headerAutenticacion == null || !headerAutenticacion.startsWith(prefijo)) {
            throw new UnauthorizedResponse("Debe autenticarse para acceder al servicio. Envíe el header 'Authorization: Bearer <token>'");
        }


        String tramaJwt = headerAutenticacion.replace(prefijo, "").trim();
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes()))
                    .build()
                    .parseSignedClaims(tramaJwt)
                    .getPayload();

            System.out.println("JWT válido recibido: " + claims.toString());

            // Almacenar claims en el contexto para uso posterior en los handlers
            ctx.attribute("jwt-claims", claims);

        } catch (ExpiredJwtException e) {
            throw new ForbiddenResponse("El token JWT ha expirado: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new ForbiddenResponse("Token JWT inválido: " + e.getMessage());
        }
    }
}
