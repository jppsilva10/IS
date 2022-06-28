package data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Balance_last_month implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    private float metric;

    public Balance_last_month() {
        super();
    }

    public Balance_last_month(long id, float metric) {
        super();
        this.id = id;
        this.metric = metric;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMetric() {
        return metric;
    }

    public void setMetric(float metric) {
        this.metric = metric;
    }
}
