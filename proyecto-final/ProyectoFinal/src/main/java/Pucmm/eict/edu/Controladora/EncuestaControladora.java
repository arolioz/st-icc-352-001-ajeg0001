package Pucmm.eict.edu.Controladora;

import Pucmm.eict.edu.Entidades.Encuesta;
import Pucmm.eict.edu.Services.EncuestaServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EncuestaControladora {
    public static void listarEncuestas(@NotNull Context ctx) throws Exception {
        List<Encuesta> encuestas = EncuestaServices.getInstancia().listarEncuestas();

        ctx.json(encuestas);
    }

    public static void crearEncuesta(@NotNull Context ctx) {
    }

    public static void obtenerEncuestasUsuario(@NotNull Context ctx) {
    }

    public static void eliminarEncuesta(@NotNull Context ctx) {
    }

    public static void obtenerEncuestaById(@NotNull Context ctx) {
    }

    public static void modificarEncuesta(@NotNull Context ctx) {
    }
}
