package data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Total_metric implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private float metric;

    public Total_metric() {
        super();
    }

    public Total_metric(String id, float metric) {
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

    public float getMetric() {
        return metric;
    }

    public void setMetric(float metric) {
        this.metric = metric;
    }
}
