package be.thomasmore.party.repositories;

import be.thomasmore.party.model.PartyAnimal;
import org.springframework.data.repository.CrudRepository;

public interface PartyAnimalRepository extends CrudRepository<PartyAnimal, Integer> {
}
