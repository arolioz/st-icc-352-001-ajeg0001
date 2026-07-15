package edu.pucmm.eict.P2;

import io.javalin.Javalin;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class Main {
    void main() {
        var app = Javalin.create(config ->{
            config.routes.get("/", ctx ->{
                ctx.result("Hola mundo!");
            });
        });
        app.start(7000);
    }
}