package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Vianda;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class HeladeraRepository {
    private List<Heladera> heladeras = new ArrayList<>();
    private static AtomicInteger seqID = new AtomicInteger();

    public Heladera save(Heladera heladeraNueva){
        if (heladeraNueva.getHeladeraId() == null) {
            heladeraNueva.setHeladeraId(seqID.incrementAndGet());
        }

        boolean found = false;
        for (Heladera existingHeladera : heladeras) {
            if (existingHeladera.getHeladeraId().equals(heladeraNueva.getHeladeraId())) {
                // reemplazo la vianda existente pisandola
                heladeras.set(heladeras.indexOf(existingHeladera), heladeraNueva);
                found = true;
                break;
            }
        }
        if (!found) {
            heladeras.add(heladeraNueva);
        }
        return heladeraNueva;
    }

    public Heladera findById(Integer id){
        Optional<Heladera> heladeraOptional = this.heladeras.stream().filter( h -> h.getHeladeraId().equals(id) ).findFirst();
        return heladeraOptional.orElseThrow( () -> new NoSuchElementException( String.format("No existe una heladera con ese ID %s",id) ));
    }

}
