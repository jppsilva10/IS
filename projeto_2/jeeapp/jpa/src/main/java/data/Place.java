package data;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "PLACE")
public class Place {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //@Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "departure")
    private Collection<Trip> departures;

    @OneToMany(mappedBy = "destination")
    private Collection<Trip> destinations;

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

    public Collection<Trip> getDepartures() {
        return departures;
    }

    public void setDepartures(Collection<Trip> departures) {
        this.departures = departures;
    }

    public Collection<Trip> getDestinations() {
        return destinations;
    }

    public void setDestinations(Collection<Trip> destinations) {
        this.destinations = destinations;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id= " + id +
                ", name= '" + name + '\'' +
                '}';
    }
}
