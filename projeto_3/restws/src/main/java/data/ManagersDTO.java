package data;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

public class ManagersDTO implements Serializable{
    private long id;

    private String name;

    private Collection<ClientDTO> clients;

    public ManagersDTO() {
        super();
    }

    public ManagersDTO(String name) {
        super();
        this.name = name;
    }

    public ManagersDTO(Managers m) {
        this.id = m.getId();
        this.name = m.getName();
        this.clients = m.getClients().stream().map(Client::DTO).collect(Collectors.toList());
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

    public Collection<ClientDTO> getClients() {
        return clients;
    }

    public void setClients(Collection<ClientDTO> clients) {
        this.clients = clients;
    }
}
