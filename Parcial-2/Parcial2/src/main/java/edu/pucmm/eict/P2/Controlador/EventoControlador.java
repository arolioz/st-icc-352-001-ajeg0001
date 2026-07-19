package edu.pucmm.eict.P2.Controlador;

import Util.RolesApp;
import edu.pucmm.eict.P2.Entidades.Evento;
import edu.pucmm.eict.P2.Entidades.EventoUsuario;
import edu.pucmm.eict.P2.Entidades.Usuario;
import edu.pucmm.eict.P2.Services.EventoServices;
import edu.pucmm.eict.P2.Services.EventoUsuarioServices;
import edu.pucmm.eict.P2.Services.UsuarioServices;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class EventoControlador {
    public static void procesarCrear(@NotNull Context ctx) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String titulo = ctx.formParam("titulo");
        String descripcion  = ctx.formParam("descripcion");
        String fechaStr =  ctx.formParam("fecha");
        Date fecha = format.parse(fechaStr);
        LocalTime hora = LocalTime.parse(Objects.requireNonNull(ctx.formParam("hora")));
        int cupoMaximo = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cupo")));
        String lugar = ctx.formParam("lugar");


        Usuario usuario = ctx.sessionAttribute("usuario");

        if (usuario == null || !usuario.getListaRoles().contains(RolesApp.ROLE_ORGANIZADOR)) {
            ctx.status(403);
            return;
        }

        if (titulo == null || titulo.isBlank() ||
                fecha == null || descripcion == null || hora == null
                || cupoMaximo <= 0 || lugar == null || lugar.isBlank()) {

            ctx.status(400).result("No se pudo procesar el registro").redirect("/registrar");
            return;
        }



        Evento e = new Evento();
        e.setIdOirganizador(usuario.getId());
        e.setTitulo(titulo);
        e.setFecha(fecha);
        e.setHora(hora);
        e.setDescripcion(descripcion);
        e.setCupoMaximo(cupoMaximo);
        e.setLugar(lugar);
        e.setActivo(true);
        e.setPublicado(true);
        EventoServices.getInstancia().crear(e);
        ctx.status(200);
        ctx.redirect("/Eventos");

    }

    public static void inscribirUsuario (@NotNull Context ctx){

        Long eventId = (long) Integer.parseInt(ctx.pathParam("eventId"));

        Usuario tmp = ctx.sessionAttribute("usuario");

        Usuario usuario = null;
        if (tmp != null){
            usuario = UsuarioServices.getInstancia().find(tmp.getId());
        }
        Evento evento = EventoServices.getInstancia().find(eventId);


        Date fechaEventoDate = evento.getFecha();

        LocalDate fechaEvento = fechaEventoDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(fechaEvento)) {
            ctx.status(401).result("Las inscripciones ya están cerradas");
            return;
        }

        if ((usuario != null && usuario.getListaRoles().contains(RolesApp.ROLE_USUARIO)) && evento.getCupo() < evento.getCupoMaximo() && evento != null && evento.getPublicado() == true){
            EventoUsuario tmpEU = EventoUsuarioServices.getInstancia().findUsuarioEnEvento(usuario.getId(),eventId);

            if (tmpEU == null){
                String token = UUID.randomUUID().toString();

                evento.setCupo(evento.getCupo() + 1);
                EventoUsuario eu = new EventoUsuario();
                eu.setUsuario(usuario);
                eu.setEvento(evento);
                eu.setToken(token);
                eu.setFechaInscripcion(LocalDateTime.now());
                EventoUsuarioServices.getInstancia().crear(eu);
                EventoServices.getInstancia().editar(evento);
            }

        }
    }

    public static void cancelarInscripcion(@NotNull Context ctx){
        Long eventId = (long) Integer.parseInt(ctx.pathParam("eventId"));


        Usuario tmp = ctx.sessionAttribute("usuario");

        Usuario usuario = null;
        if (tmp != null){
            usuario = UsuarioServices.getInstancia().find(tmp.getId());
        }

        Evento evento = EventoServices.getInstancia().find(eventId);

        if ((usuario != null && usuario.getListaRoles().contains(RolesApp.ROLE_USUARIO)) && evento != null){
            EventoUsuario tmpEU = EventoUsuarioServices.getInstancia().findUsuarioEnEvento(usuario.getId(),eventId);

            if (tmpEU != null){
                EventoUsuarioServices.getInstancia().eliminar(tmpEU.getId());
                evento.setCupo(evento.getCupo() - 1);
                EventoServices.getInstancia().editar(evento);
            }
        }
    }

    public static void procesarModificar(@NotNull Context ctx) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String titulo = ctx.formParam("titulo");
        String descripcion  = ctx.formParam("descripcion");
        String fechaStr =  ctx.formParam("fecha");
        Date fecha = format.parse(fechaStr);
        LocalTime hora = LocalTime.parse(Objects.requireNonNull(ctx.formParam("hora")));
        int cupoMaximo = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cupo")));
        String lugar = ctx.formParam("lugar");
        Long id = Long.valueOf(Objects.requireNonNull(ctx.formParam("id")));

        Evento e = EventoServices.getInstancia().find(id);

        Usuario usuario = ctx.sessionAttribute("usuario");

        if (usuario == null || !usuario.getListaRoles().contains(RolesApp.ROLE_ORGANIZADOR)) {
            ctx.status(403);
            return;
        }

        if (titulo == null || titulo.isBlank() ||
                fecha == null || descripcion == null || hora == null
                || cupoMaximo <= 0 || lugar == null || lugar.isBlank()) {

            ctx.status(400).result("No se pudo procesar el registro").redirect("/registrar");
            return;
        }

        if (e != null){
            e.setTitulo(titulo);
            e.setFecha(fecha);
            e.setHora(hora);
            e.setDescripcion(descripcion);
            e.setLugar(lugar);
            e.setActivo(true);

            if (e.getCupo() >= cupoMaximo){
                e.setCupoMaximo(e.getCupo());
            }
            else{
                e.setCupoMaximo(cupoMaximo);
            }

            EventoServices.getInstancia().editar(e);
        }

    }

    public static void cambiarEstadoEvento(@NotNull Context ctx){
        Long id = Long.valueOf(Objects.requireNonNull(ctx.pathParam("id")));

        Evento e = EventoServices.getInstancia().find(id);

        if (e != null){
            if(e.getPublicado() == null || e.getPublicado() == false){
                e.setPublicado(true);
            }
            else{
                e.setPublicado(false);
            }
        }
    }

    public  static void cancelarEvento(@NotNull Context ctx){
        Long id = Long.valueOf(Objects.requireNonNull(ctx.formParam("id")));

        Evento e = EventoServices.getInstancia().find(id);

        if (e != null){
            e.setActivo(false);
        }
    }

    public static void eliminarEvento(@NotNull Context ctx){
        Long id = Long.valueOf(Objects.requireNonNull(ctx.formParam("id")));

        Evento e = EventoServices.getInstancia().find(id);

        if (e != null){
            e.setEliminado(true);
            e.setPublicado(false);
            e.setActivo(false);
        }
    }

    public static void marcarAsistencia(@NotNull Context ctx){
        String token = ctx.pathParam("token");

        EventoUsuario eu = EventoUsuarioServices.getInstancia().findRegistroToken(token);

        if (eu == null) {
            ctx.status(404).result("Token no válido");
            return;
        }

        Date fechaEventoDate = eu.getEvento().getFecha();

        LocalDate fechaEvento = fechaEventoDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate hoy = LocalDate.now();

        if (!fechaEvento.equals(hoy)) {
            ctx.status(400).result("La asistencia solo puede registrarse el día del evento");
            return;
        }

        if (!eu.getAsistencia()) {
            eu.setAsistencia(true);
            eu.setFechaAsistencia(LocalDateTime.now());
            EventoUsuarioServices.getInstancia().editar(eu);
        }
    }
}
