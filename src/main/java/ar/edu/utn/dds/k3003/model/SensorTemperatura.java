package ar.edu.utn.dds.k3003.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SensorTemperatura {
    Integer heladeraIdPerteneciente;
    Map<Integer, LocalDateTime> temperaturas = new HashMap<>();
    Integer ultimaTemperaRegistrada;

    public SensorTemperatura(){}

    public SensorTemperatura(Integer heladeraId){
        this.heladeraIdPerteneciente = heladeraId;
    }

    public Map<Integer, LocalDateTime> obtenerTodasLasTemperaturas(){
        return this.temperaturas;
    }

    public Map.Entry<Integer, LocalDateTime> obtenerTemperatura(){
        return setNuevaTemperatura( (int) (Math.random() * 10) , LocalDateTime.now() );
    }

    public Map.Entry<Integer, LocalDateTime> setNuevaTemperatura(Integer temperatura, LocalDateTime tiempo) {
        this.temperaturas.put(temperatura, tiempo);
        this.ultimaTemperaRegistrada = temperatura;
        return new AbstractMap.SimpleEntry<>(temperatura, tiempo);
    }

}
