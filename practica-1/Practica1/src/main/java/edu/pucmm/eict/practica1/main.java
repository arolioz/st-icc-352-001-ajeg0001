package edu.pucmm.eict.practica1;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese la url: ");

        String url = scanner.nextLine();

        scanner.close();

        HttpClient cliente = HttpClient.newHttpClient();


        try{
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            HttpHeaders responseHeaders = response.headers();

            String tipoRecurso = responseHeaders.firstValue("Content-type").orElse("Error");

            System.out.println("[El tipo de recurso es " + tipoRecurso + "]");

            if (!tipoRecurso.contains("text/html")){
                System.out.println("El recurso no es HTML, abortando el programa...");
                return;
            }

            String body = response.body();

            Document doc =  Jsoup.parse(body, url);

            String contenidoHtml = doc.toString();

            System.out.println("1- Cantidad de lineas del recurso retornado: " + contenidoHtml.split("\n").length);

            Elements parrafos = doc.select("p");

            System.out.println("2- Cantidad de parrafos del recurso retornado: " + parrafos.size());

            Elements imgEnParrafos = doc.select("p img");

            System.out.println("3- Cantidad de imagenes dentro de los parrafos del recurso retornado: " + imgEnParrafos.size());

            Elements formsPost = doc.select("form[method=post]");
            Elements formsGet = doc.select("form[method=get]");

            System.out.println("4- Cantidad de forms con el metodo post: " + formsPost.size());
            System.out.println("4- Cantidad de forms con el metodo get: " + formsGet.size());

            Elements forms = doc.select("form");

            System.out.println("5- Inputs de cada formulario");

            int n = 1;
            for (Element formulario : forms){

                System.out.println("Formulario["+n+"]");

                Elements inputs = formulario.select("input");

                if (inputs.isEmpty()){
                    System.out.println("No hay inputs en este formulario");
                    System.out.println("----------------------");
                }
                else {
                    System.out.println("Inputs:");

                    int k = 1;
                    for (Element input : inputs) {
                        String tipo = input.attr("type");

                        if (tipo.isEmpty()) {
                            tipo = "text";
                        }

                        System.out.println(k + "-" + input.attr("name") + "[Tipo: " + tipo + "]");
                        k++;
                    }
                    System.out.println("----------------------");
                }
                n++;
            }

            System.out.println("6- Peticiones al servidor a partir de los forms con el metodo post: ");

            n = 1;
            for (Element form : forms){

                if (form.attr("method").equalsIgnoreCase("post")){

                    String action = form.attr("action");

                    System.out.println("Formulario" + "[" + n + "]");
                    if (!action.isEmpty()){
                        try{
                            HttpRequest peticion = HttpRequest.newBuilder()
                                    .uri(new URI(action))
                                    .header("matricula-id", "10154428").POST(HttpRequest.BodyPublishers.ofString("asignatura=practica1")).build();
                            HttpResponse<String> respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());

                            System.out.println("Enviando peticion al action: " + action);
                            System.out.println("Respuesta del servidor: " + respuesta.body());
                        }catch (Throwable error){
                            System.out.println("Hubo un error resolviendo el action: " + action);
                        }
                    }
                    else{
                        System.out.println("No se encontro el action del formulario");
                    }
                    n++;
                }


            }

        } catch (Throwable t){
            System.out.println("Ha ocurrido un error utilizando la url " + url);
            System.out.println(t);
        }




        //HttpRequest.newBuilder(request, (name, value) -> !name.equalsIgnoreCase("Foo-Bar"));

    }
}
