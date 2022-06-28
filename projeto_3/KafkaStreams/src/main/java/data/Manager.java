package data;

import java.io.Serializable;
import java.util.Collection;

public class Manager implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private Collection<Client> clients;

    public Manager() {
        super();
    }

    public Manager(String name) {
        super();
        this.name = name;
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
