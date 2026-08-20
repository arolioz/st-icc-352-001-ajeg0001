package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static Pucmm.eict.edu.Main.LLAVE_SECRETA;

public class JwtControladora {
    public static String generarToken(Usuario u) {
        return Jwts.builder()
                .subject(u.getId().toHexString())
                .claim("user", u.getUser())
                .claim("roles", u.getListaRoles().stream().map(Enum::name).toList())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(12, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(LLAVE_SECRETA.getBytes()))
                .compact();
    }
}
