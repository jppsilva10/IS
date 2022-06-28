package data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Managers implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "manager_id", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Collection<Client> clients;

    public Managers() {
        super();
    }

    public Managers(String name) {
        super();
        this.name = name;
    }

    public ManagersDTO DTO(){
        return new ManagersDTO(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Client> getClients() {
        return clients;
    }

    public void setClients(Collection<Client> clients) {
        this.clients = clients;
    }
}
