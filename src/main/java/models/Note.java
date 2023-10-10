package models;

import java.sql.Date;

public class Note {
    private int id;
    private String uuid;
    private String type;
    private String text;
    private Date created_at;
    private Date updated_at;
    private Date noted_at;
    private NotePoint point;
    private String units_per_meter;
    private String color;
    private int season_id;
    private boolean is_deleted;
    private String field_user_season_uuid;
    private String sharing_code;
    private String fieldTitle;

    

    public Note(int id, String uuid, String type, String text, Date created_at, Date updated_at, NotePoint point,
            String units_per_meter, String color, int season_id, boolean is_deleted, String field_user_season_uuid,
            String sharing_code) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.text = text;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.point = point;
        this.units_per_meter = units_per_meter;
        this.color = color;
        this.season_id = season_id;
        this.is_deleted = is_deleted;
        this.field_user_season_uuid = field_user_season_uuid;
        this.sharing_code = sharing_code;
    }

    public Note() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public NotePoint getPoint() {
        return point;
    }

    public void setPoint(NotePoint point) {
        this.point = point;
    }

    public String getUnits_per_meter() {
        return units_per_meter;
    }

    public void setUnits_per_meter(String units_per_meter) {
        this.units_per_meter = units_per_meter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getField_user_season_uuid() {
        return field_user_season_uuid;
    }

    public void setField_user_season_uuid(String field_user_season_uuid) {
        this.field_user_season_uuid = field_user_season_uuid;
    }

    public String getSharing_code() {
        return sharing_code;
    }

    public void setSharing_code(String sharing_code) {
        this.sharing_code = sharing_code;
    }

    public Date getNoted_at() {
        return noted_at;
    }

    public void setNoted_at(Date noted_at) {
        this.noted_at = noted_at;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    
}
