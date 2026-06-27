package edu.pucmm.eict;

import edu.pucmm.eict.controladores.*;
import edu.pucmm.eict.servicios.BootStrapServices;
import edu.pucmm.eict.servicios.DataBaseServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hola Mundo en Javalin 7 :-D");

        BootStrapServices.startDb();

        DataBaseServices.getInstancia().testConexion();

        BootStrapServices.crearTablas();

        /**
         * En Javalin 7 TODAS las rutas y manejadores deben registrarse
         * dentro del bloque Javalin.create() antes de llamar a start().
         */
        Javalin app = Javalin.create(config -> {

            // Archivos estáticos servidos desde /publico en el classpath
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
            });

            // Motor de plantillas por defecto: Thymeleaf (usado en /crud-simple y /thymeleaf)
            config.fileRenderer(new JavalinThymeleaf());

            // Rutas del API REST (/api/estudiante) y CRUD tradicional (/crud-simple)
            config.routes.apiBuilder(() -> {


                /**
                 * CRUD con plantillas Thymeleaf (flujo petición-respuesta tradicional).
                 * http://localhost:7000/crud-simple/listar
                 */
                path("/crud-simple/", () -> {
                    get(ctx -> ctx.redirect("/crud-simple/listar"));
                    get("/listar", CrudTradicionalControlador::listar);
                    get("/crear", CrudTradicionalControlador::crearEstudianteForm);
                    post("/crear", CrudTradicionalControlador::procesarCreacionEstudiante);
                    get("/visualizar/{matricula}", CrudTradicionalControlador::visualizarEstudiante);
                    get("/editar/{matricula}", CrudTradicionalControlador::editarEstudianteForm);
                    post("/editar", CrudTradicionalControlador::procesarEditarEstudiante);
                    get("/eliminar/{matricula}", CrudTradicionalControlador::eliminarEstudiante);
                });
            });

            // Endpoint raíz
            config.routes.get("/", ctx -> ctx.result("Hola Mundo en Javalin 7 :-D"));


            // Resumen visual de todas las rutas registradas → http://localhost:7000/routes
            config.bundledPlugins.enableRouteOverview("/routes");

        });

        app.start(7000
        );
    }

}
