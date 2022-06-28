package data;

import java.io.Serializable;

public class Currency implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private float exchangeRate;

    public Currency() {
        super();
    }

    public Currency(String name, float exchangeRate) {
        super();
        this.name = name;
        this.exchangeRate = exchangeRate;
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

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
