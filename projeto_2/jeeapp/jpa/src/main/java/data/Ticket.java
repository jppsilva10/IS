package data;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "TICKET", uniqueConstraints = @UniqueConstraint(columnNames={"place", "trip_id"}))
public class Ticket implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    //@Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "place", nullable = false)
    @Min(1)
    private int place;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable=false)
    private Trip trip_id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user_id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Trip getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Trip trip_id) {
        this.trip_id = trip_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id= " + id +
                ", trip_id= " + trip_id +
                ", user_id= " + user_id +
                '}';
    }
}
