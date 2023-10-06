package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AntellisField {
     @JsonProperty("Name")
    private String name;
    @JsonProperty("Group")
    private String group;
    @JsonProperty("Points")
    private FieldCoordinate[] points;

    public AntellisField() {
    }
    public AntellisField(String name, String group, FieldCoordinate[] points) {
        this.name = name;
        this.group = group;
        this.points = points;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroup() {
        return this.group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public FieldCoordinate[] getPoints() {
        return points;
    }
    public void setPoints(FieldCoordinate[] points) {
        this.points = points;
    }

    
}
