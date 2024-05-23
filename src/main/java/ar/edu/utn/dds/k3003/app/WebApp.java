package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.controller.HeladeraController;
import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class WebApp {

    public static void main(String[] args) {

        var env = System.getenv();
        var objectMapper = createObjectMapper();
        var fachada = new Fachada();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                configureObjectMapper(mapper);
            }));
        }).start(port);

        var heladeraController = new HeladeraController(fachada);

        app.post("/heladeras", heladeraController::agregar);
        app.get("/heladeras/{heladeraId}", heladeraController::obtenerHeladera);
        app.post("/depositos", heladeraController::depositarVianda);
        app.post("/retiros", heladeraController::retirarVianda);
        app.post("/temperaturas", heladeraController::registrarTemperatura);
        app.get("/heladeras/{heladeraId}/temperaturas", heladeraController::obtenerTemperaturas);
    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}