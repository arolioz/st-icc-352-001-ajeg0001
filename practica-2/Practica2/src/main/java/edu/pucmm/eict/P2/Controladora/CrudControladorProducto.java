package edu.pucmm.eict.P2.Controladora;

import edu.pucmm.eict.P2.Logico.Producto;
import edu.pucmm.eict.P2.Service.Controladora;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudControladorProducto {

    private final static Controladora controladora = Controladora.getInstance();

    public static void listar(@NotNull Context ctx) {
        List<Producto> lista = controladora.listarProductos();

        IO.println(lista.get(0).getIdProducto());

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("lista", lista);
        //enviando al sistema de plantilla.
        ctx.render("/templates/crud/productos.html", modelo);
    }


}
