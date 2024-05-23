package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.DTO.GetErrorHeladeraDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class HeladeraController{
    private final Fachada fachada;

    public HeladeraController(Fachada fachada){
        this.fachada = fachada;
    }

    public void agregar(@NotNull Context context){
        try{
            HeladeraDTO heladeraDTO = fachada.agregar(context.bodyAsClass(HeladeraDTO.class));
            context.json(heladeraDTO);
            context.status(HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            GetErrorHeladeraDTO errorHeladeraDTO =
                    new GetErrorHeladeraDTO(0,"Error Agregando Heladera");
            context.json(errorHeladeraDTO);
            context.status(HttpStatus.BAD_REQUEST);
        }
    }

    public void obtenerHeladera(@NotNull Context context){
        try{
            var heladeraDTO = fachada.obtenerHeladera(Integer.valueOf(context.pathParam("heladeraId")));
            context.json(heladeraDTO);
            context.status(HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            context.status(HttpStatus.NOT_FOUND);
            context.result("Heladera No encontrada");
        }
    }

    public void depositarVianda(@NotNull Context context){
        try{
            Integer heladeraId = Integer.valueOf(context.pathParam("heladeraId"));
            String qrVianda = context.pathParam("codigoQR");
            if (!fachada.existeHeladera(heladeraId)) {
                context.status(HttpStatus.NOT_FOUND);
                context.result("Heladera no encontrada");
            }
            fachada.depositar(heladeraId, qrVianda);
            context.status(HttpStatus.OK);
            context.result("Vianda depositada correctamente");
        }
        catch(NoSuchElementException e){
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Error de solicitud");
        }
    }

    public void retirarVianda(@NotNull Context context){
        try{
            //TODO: mover a un map ->
            String qrVianda = context.pathParam("codigoQR");
            String tarjeta = context.pathParam("tarjeta");
            LocalDateTime fechaRetiro = LocalDateTime.parse(context.pathParam("fechaRetiro"));
            Integer heladeraId = Integer.valueOf(context.pathParam("heladeraId"));

            RetiroDTO retiro = new RetiroDTO( qrVianda, tarjeta, fechaRetiro, heladeraId );

            fachada.retirar(retiro);
            context.status(HttpStatus.OK);
            context.result("Vianda retirada exitosamente");
        }
        catch(NoSuchElementException e){
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Error de solicitud");
        }
    }

    public void registrarTemperatura(@NotNull Context context){
        try{
            fachada.temperatura(context.bodyAsClass(TemperaturaDTO.class));
            context.status(HttpStatus.OK);
            context.result("Temperatura registrada correctamente");
        }
        catch(NoSuchElementException e){
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Error de solicitud");
        }
    }

    public void obtenerTemperaturas(@NotNull Context context){
        try{
            Integer heladeraId = Integer.valueOf(context.pathParam("heladeraId"));
            if (!fachada.existeHeladera(heladeraId)) {
                throw new NoSuchElementException();
            }
            List<TemperaturaDTO> temperaturas = fachada.obtenerTemperaturas(heladeraId);
            context.json(temperaturas);
            context.status(HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Heladera no encontrada");
        }
    }
}
