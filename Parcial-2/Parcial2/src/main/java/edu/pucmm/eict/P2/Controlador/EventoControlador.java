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
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

        if ((usuario != null && usuario.getListaRoles().contains(RolesApp.ROLE_USUARIO)) && evento.getCupo() < evento.getCupoMaximo() && evento != null){
            EventoUsuario tmpEU = EventoUsuarioServices.getInstancia().findUsuarioEnEvento(usuario.getId(),eventId);

            if (tmpEU == null){
                evento.setCupo(evento.getCupo() + 1);
                EventoUsuario eu = new EventoUsuario();
                eu.setUsuario(usuario);
                eu.setEvento(evento);
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

            if (e.getCupo() <= cupoMaximo){
                e.setCupoMaximo(e.getCupo());
            }
            else{
                e.setCupoMaximo(cupoMaximo);
            }

            EventoServices.getInstancia().editar(e);
        }

    }
}
