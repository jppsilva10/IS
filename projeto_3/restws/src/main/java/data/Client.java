package data;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.io.Serializable;

@Entity
public class Client implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="manager_id", nullable=false)
    private Managers manager_id;

    public Client() {
        super();
    }

    public Client(String name, Managers manager_id) {
        super();
        this.name = name;
        this.manager_id = manager_id;
    }

    public ClientDTO DTO(){
        return new ClientDTO(this);
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

    public Managers getManager_id() {
        return manager_id;
    }

    public void setManager_id(Managers manager_id) {
        this.manager_id = manager_id;
    }
}
