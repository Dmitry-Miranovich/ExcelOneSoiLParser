package models;

public class Field {
    private int id;
    private int field_user_season_id;
    private String title;
    private int index_number;
    private float area;
    private float[] bbox;
    private Centroid centroid;
    private Geometry geometry;
    private Crops[] crops;
    private LastNDVI last_ndvi;
    private Avg avg;
    private Std std;

    public Field(){}

    public Field(int id, int field_user_season_id, String title, int index_number, float area, float[] bbox,
            Centroid centroid, Geometry geometry, Crops[] crops, LastNDVI last_ndvi, Avg avg, Std std) {
        this.id = id;
        this.field_user_season_id = field_user_season_id;
        this.title = title;
        this.index_number = index_number;
        this.area = area;
        this.bbox = bbox;
        this.centroid = centroid;
        this.geometry = geometry;
        this.crops = crops;
        this.last_ndvi = last_ndvi;
        this.avg = avg;
        this.std = std;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getField_user_season_id() {
        return field_user_season_id;
    }
    public void setField_user_season_id(int field_user_season_id) {
        this.field_user_season_id = field_user_season_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIndex_number() {
        return index_number;
    }
    public void setIndex_number(int index_number) {
        this.index_number = index_number;
    }
    public float getArea() {
        return area;
    }
    public void setArea(float area) {
        this.area = area;
    }
    public float[] getBbox() {
        return bbox;
    }
    public void setBbox(float[] bbox) {
        this.bbox = bbox;
    }
    public Centroid getCentroid() {
        return centroid;
    }
    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }
    public Geometry getgeometry() {
        return geometry;
    }
    public void setgeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    public Crops[] getCrops() {
        return crops;
    }
    public void setCrops(Crops[] crops) {
        this.crops = crops;
    }
    public LastNDVI getLast_ndvi() {
        return last_ndvi;
    }
    public void setLast_ndvi(LastNDVI last_ndvi) {
        this.last_ndvi = last_ndvi;
    }
    public Avg getAvg() {
        return avg;
    }
    public void setAvg(Avg avg) {
        this.avg = avg;
    }
    public Std getStd() {
        return std;
    }
    public void setStd(Std std) {
        this.std = std;
    }
    
    
}
