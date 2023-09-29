package models;

public class FieldsData {
    private int next_index_number;
    private Field[] rows;

    public FieldsData(){}

    public FieldsData(int next_index_number, Field[] rows) {
        this.next_index_number = next_index_number;
        this.rows = rows;
    }

    public int getNext_index_number() {
        return next_index_number;
    }

    public void setNext_index_number(int next_index_number) {
        this.next_index_number = next_index_number;
    }

    public Field[] getRows() {
        return rows;
    }

    public void setRows(Field[] rows) {
        this.rows = rows;
    }

    
}
