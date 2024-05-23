package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;

import static ar.edu.utn.dds.k3003.utils.utils.randomID;

public class Vianda {
    Integer viandaId;
    String qrVianda;
    EstadoViandaEnum estadoVianda;
    Integer heladeraId;

    public Vianda(){}

    public Vianda(String qrVianda) {
        this.viandaId = randomID();
        this.qrVianda = qrVianda;
        this.estadoVianda = EstadoViandaEnum.PREPARADA;
        this.heladeraId = null;
    }

    public Integer getHeladeraId() {
        return heladeraId;
    }

    public Integer getViandaId() {
        return viandaId;
    }

    public String getQrVianda() {
        return qrVianda;
    }

    public EstadoViandaEnum getEstadoVianda() {
        return estadoVianda;
    }

    public void setEstadoVianda(EstadoViandaEnum estadoVianda) {
        this.estadoVianda = estadoVianda;
    }

    public void setHeladeraId(Integer heladeraId) {
        this.heladeraId = heladeraId;
    }
}
