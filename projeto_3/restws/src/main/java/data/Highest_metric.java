package data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Highest_metric implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private long metric;

    public Highest_metric() {
        super();
    }

    public Highest_metric(String id, long metric) {
        super();
        this.id = id;
        this.metric = metric;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMetric() {
        return metric;
    }

    public void setMetric(long metric) {
        this.metric = metric;
    }
}
