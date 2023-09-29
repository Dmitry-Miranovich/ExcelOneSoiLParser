package models;

import java.sql.Date;

public class Crops{
    private int id;
    private String uuid;
    private String crop;
    private Date sowing_date;
    private Date harvest_date;
    private String yield_value;
    private String yield_value_units;
    private String source;
    private String variety;
    private Date created_at;
    private Date updated_at;
    private int season_id;

    public Crops(){}

    public Crops(int id, String uuid, String crop, Date sowing_date, Date harvest_date, String yield_value, String yield_value_units,
            String source, String variety, Date created_at, Date updated_at, int season_id) {
        this.id = id;
        this.uuid = uuid;
        this.crop = crop;
        this.sowing_date = sowing_date;
        this.harvest_date = harvest_date;
        this.yield_value = yield_value;
        this.yield_value_units = yield_value_units;
        this.source = source;
        this.variety = variety;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.season_id = season_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getSowing_date() {
        return sowing_date;
    }

    public void setSowing_date(Date sowing_date) {
        this.sowing_date = sowing_date;
    }

    public Date getHarvest_date() {
        return harvest_date;
    }

    public void setHarvest_date(Date harvest_date) {
        this.harvest_date = harvest_date;
    }

    public String getYield_value() {
        return yield_value;
    }

    public void setYield_value(String yield_value) {
        this.yield_value = yield_value;
    }

    public String getYield_value_units() {
        return yield_value_units;
    }

    public void setYield_value_units(String yield_value_units) {
        this.yield_value_units = yield_value_units;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    

    
}