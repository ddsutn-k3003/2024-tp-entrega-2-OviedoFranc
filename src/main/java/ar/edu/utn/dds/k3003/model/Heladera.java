package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ar.edu.utn.dds.k3003.utils.utils.*;

public class Heladera {
    Integer heladeraId;
    String nombre;
    String modelo;
    Coordenadas coordenadas;
    String direccion;
    Integer cantidadMaximaViandas;
    Float pesoMaximo;
    Float pesoActual;
    Boolean estadoActivo;
    SensorTemperatura sensor;
    Integer temperaturaMinima;
    Integer temperaturaMaxima;
    List<String> viandas = new ArrayList<>();

    public Heladera(){}

    public Heladera(Integer id, String nombre){
        this.heladeraId = id;
        this.nombre = nombre;
        this.coordenadas = new Coordenadas(randomNumberBetween(0,255), randomNumberBetween(0,255));
        this.cantidadMaximaViandas = randomNumberBetween(10,25);
        this.pesoMaximo = 100f;
        this.pesoActual = 0f;
        this.estadoActivo = true;
        this.modelo = generarModeloAleatorio();
        this.direccion = generarDireccionAleatoria();
        this.sensor = new SensorTemperatura(id);
        this.temperaturaMaxima = randomNumberBetween(95,120);
        this.temperaturaMinima = - 10;
    }

    public void setHeladeraId(Integer id){ this.heladeraId = id;};

    public Integer getHeladeraId(){
        return this.heladeraId;
    }

    public String getNombre(){return this.nombre;}

    public Integer ultimaTemperatura(){
        return this.sensor.ultimaTemperaRegistrada;
    }

    public Map<Integer, LocalDateTime> obtenerTodasLasTemperaturas(){
        return this.sensor.obtenerTodasLasTemperaturas();
    }

    public Map.Entry<Integer, LocalDateTime> reportarTemperatura(){
        return this.sensor.obtenerTemperatura();
    }
    public void guardarVianda(String viandaQR) throws Exception {
        if (this.cantidadDeViandas() < cantidadMaximaViandas) {
            this.viandas.add(viandaQR);
        } else {
            throw new Exception("La cantidad de viandas en la heladera ha alcanzado el límite máximo");
        }
    }
    public void retirarVianda(String viandaQR) {
        List<String> viandasList = new ArrayList<>(viandas);
        viandasList.removeIf(v -> v.equals(viandaQR));
        this.viandas = List.of(viandasList.toArray(new String[0]));
    }

    public Integer cantidadDeViandas(){
        return this.viandas.size();
    }

    public SensorTemperatura getSensor() {
        return sensor;
    }
}
