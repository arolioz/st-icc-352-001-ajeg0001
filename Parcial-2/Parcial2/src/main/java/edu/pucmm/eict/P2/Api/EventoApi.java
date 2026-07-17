package edu.pucmm.eict.P2.Api;

import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Services.EventoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventoApi {
    public static void listaEventos(@NotNull Context ctx){
        List<Evento> eventos = EventoServices.getInstancia().findAll();

        ctx.json(eventos);
    }
}
