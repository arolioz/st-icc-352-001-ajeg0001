package edu.pucmm.eict.P2.Api;

import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Services.EventoServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EventoApi {
    public static void listaEventos(@NotNull Context ctx){
        List<Evento> eventos = EventoServices.getInstancia().findAll();

        ctx.json(eventos);
    }

    public static void obtenerEvento(@NotNull Context ctx){
        Long id = Long.parseLong(ctx.pathParam("id"));

        Evento evento = EventoServices.getInstancia().find(id);

        ctx.json(evento);
    }

    public static void cupoEvento(@NotNull Context ctx){
        int id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("id")));

        Evento e = EventoServices.getInstancia().find(id);

        if (e != null){
            ctx.json(e.getCupo());
        }

    }
}
