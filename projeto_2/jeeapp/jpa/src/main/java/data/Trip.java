package data;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Entity
@Table(name = "TRIP")
public class Trip {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //@Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "departure_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;
    @Column(name = "capacity", nullable = false)
    @Min(1)
    private int capacity;
    @Column(name = "price", nullable = false)
    @DecimalMin(value="0")
    private float price;

    @OneToMany(mappedBy = "trip_id", cascade = CascadeType.REMOVE)
    private Collection<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name="departure", nullable=false)
    private Place departure;

    @ManyToOne
    @JoinColumn(name="destination", nullable=false)
    private Place destination;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Place getDeparture() {
        return departure;
    }

    public void setDeparture(Place departure) {
        this.departure = departure;
    }

    public Place getDestination() {
        return destination;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id= " + id +
                ", departureDate= " + departureDate +
                ", capacity= " + capacity +
                ", price= " + price +
                ", departure= " + departure +
                ", destination= " + destination +
                '}';
    }
}
