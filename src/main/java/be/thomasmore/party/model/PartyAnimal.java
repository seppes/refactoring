package be.thomasmore.party.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class PartyAnimal {
    @Id
    int id;
    String name;
    @ManyToMany
    private Collection<Party> parties;

    public PartyAnimal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Party> getParties() {
        return parties;
    }
}
