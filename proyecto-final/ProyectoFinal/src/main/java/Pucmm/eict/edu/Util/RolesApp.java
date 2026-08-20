package Pucmm.eict.edu.Util;

import io.javalin.security.RouteRole;

/**
 * Enum para manejar los roles de la aplicacion.
 */
public enum RolesApp implements RouteRole {
    ROLE_USUARIO,
    ROLE_ENCUESTADOR,
    ROLE_ADMIN;
}
