package models;

public class Avg {
    private float value;
    private float dynamics;

    public Avg(){}

    public Avg(float value, float dynamics) {
        this.value = value;
        this.dynamics = dynamics;
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public float getDynamics() {
        return dynamics;
    }
    public void setDynamics(float dynamics) {
        this.dynamics = dynamics;
    }

    
}
