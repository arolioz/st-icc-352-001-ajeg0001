package edu.pucmm.eict.P2.Api;

import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.EventoUsuario;
import edu.pucmm.eict.P2.Services.EventoServices;
import edu.pucmm.eict.P2.Services.EventoUsuarioServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;

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

    public static void estadisticasEvento(@NotNull Context ctx){
        Long id = Long.parseLong(Objects.requireNonNull(ctx.pathParam("id")));

        Evento evento = EventoServices.getInstancia().find(id);
        List<EventoUsuario> asistenciaEvento = EventoUsuarioServices.getInstancia().findAsistenciaEvento(id);
        List<EventoUsuario> inscripcionesEvento = EventoUsuarioServices.getInstancia().findInscripcionesEvento(id);


        int totalInscritos = inscripcionesEvento.size();
        int totalAsistencia = asistenciaEvento.size();

        float porcentajeAsistencia = totalInscritos == 0 ? 0 : ((float) totalAsistencia / totalInscritos) * 100;

        Map<LocalDate, Integer> inscripcionesPorDia = new TreeMap<>();

        for(EventoUsuario eu : inscripcionesEvento){
            LocalDate fecha = eu.getFechaInscripcion().toLocalDate();

            inscripcionesPorDia.put(
                    fecha,
                    inscripcionesPorDia.getOrDefault(fecha, 0) + 1
            );
        }

        Map<Integer, Integer> asistenciaPorHora = new TreeMap<>();

        for(EventoUsuario eu : asistenciaEvento){

            if(eu.getFechaAsistencia() != null){

                int hora = eu.getFechaAsistencia().getHour();

                asistenciaPorHora.put(
                        hora,
                        asistenciaPorHora.getOrDefault(hora, 0) + 1
                );
            }
        }

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("evento", evento.getTitulo());
        respuesta.put("totalInscritos", totalInscritos);
        respuesta.put("totalAsistencia", totalAsistencia);
        respuesta.put("porcentajeAsistencia", porcentajeAsistencia);
        respuesta.put("inscripcionesPorDia", inscripcionesPorDia);
        respuesta.put("asistenciaPorHora", asistenciaPorHora);

        ctx.json(respuesta);

    }
}
