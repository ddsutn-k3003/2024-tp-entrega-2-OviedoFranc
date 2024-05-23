package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.repositories.HeladeraRepository;

import java.time.LocalDateTime;
import java.util.*;


public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras {

    private final HeladeraRepository heladeraRepository = new HeladeraRepository();
    private FachadaViandas fachadaViandas;

    public Fachada() {
    }

    public HeladeraDTO obtenerHeladera(Integer heladeraID){
        Heladera heladera = heladeraRepository.findById(heladeraID);
        return new HeladeraDTO(
                heladera.getHeladeraId(),
                heladera.getNombre(),
                heladera.cantidadDeViandas());
    }

    public Boolean existeHeladera(Integer heladeraID){
        try{
            Heladera heladera = heladeraRepository.findById(heladeraID);
            return heladera!=null;
        }
        catch (Exception e) {return false;}
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {
        Heladera heladera = new Heladera( heladeraDTO.getId(), heladeraDTO.getNombre());
        Heladera heladeraGuardada = heladeraRepository.save(heladera);
        return new HeladeraDTO( heladeraGuardada.getHeladeraId(), heladeraGuardada.getNombre(), heladeraGuardada.cantidadDeViandas());
    }

    @Override
    public void depositar(Integer heladeraID, String qrVianda) throws NoSuchElementException {
        Heladera heladera = heladeraRepository.findById(heladeraID);
        ViandaDTO vianda = fachadaViandas.buscarXQR(qrVianda);

        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
        fachadaViandas.modificarHeladera(vianda.getCodigoQR(), heladeraID);

        try {
            heladera.guardarVianda(qrVianda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        heladeraRepository.save(heladera);
    }

    @Override
    public Integer cantidadViandas(Integer integer) throws NoSuchElementException {
        Heladera heladera = heladeraRepository.findById(integer);
        return heladera.cantidadDeViandas();
    }

    @Override
    public void retirar(RetiroDTO retiroDTO) throws NoSuchElementException {
        Heladera heladera = heladeraRepository.findById(retiroDTO.getHeladeraId());
        ViandaDTO vianda = fachadaViandas.buscarXQR(retiroDTO.getQrVianda());

        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.RETIRADA);
        fachadaViandas.modificarHeladera(vianda.getCodigoQR(), -1);   // -1 SIGNIFICA SET NULL

        heladera.retirarVianda(retiroDTO.getQrVianda());
        heladeraRepository.save(heladera);
    }

    @Override
    public void temperatura(TemperaturaDTO temperaturaDTO) {
        Heladera heladera = heladeraRepository.findById(temperaturaDTO.getHeladeraId());
        heladera.getSensor().setNuevaTemperatura(temperaturaDTO.getTemperatura(), temperaturaDTO.getFechaMedicion());
        heladeraRepository.save(heladera);
    }

    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer integer) {
        Heladera heladera = heladeraRepository.findById(integer);
        Map<Integer, LocalDateTime> temperaturas = heladera.obtenerTodasLasTemperaturas();

        List<TemperaturaDTO> temperaturasMapped = new ArrayList<>();
        temperaturas.forEach( (temperatura,tiempo) -> {
            TemperaturaDTO temperaturaDTO = new TemperaturaDTO(
                    temperatura,
                    heladera.getHeladeraId(),
                    tiempo
            );
            temperaturasMapped.add(temperaturaDTO);
        } );
        return temperaturasMapped;
    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {
        this.fachadaViandas = fachadaViandas;
    }
}
