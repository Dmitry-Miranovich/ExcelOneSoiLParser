package modules;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import constants.ExcelHeaders;
import models.AntellisField;
import models.Field;
import models.FieldCoordinate;

public class FieldCreatorModule {

    private static int size = 0;
    private ArrayList<String> antellisFields = new ArrayList<>();

    public FieldCreatorModule() {
    }

    public ArrayList<FieldCoordinate> createCoordinates(Field field) {
        ArrayList<FieldCoordinate> coordinates = new ArrayList<>();
        for (float[] realCoordinates : field.getRealCoordinates()) {
            coordinates.add(new FieldCoordinate(realCoordinates[1], realCoordinates[0]));
        }
        return coordinates;
    }

    public void writeAntellisField(Field field) {
        try {
            AntellisField a_field = new AntellisField();
            ObjectMapper mapper = new ObjectMapper();
            a_field.setGroup(String.format("%s + группа + %d, поля", field.getTitle(), getSize()));
            a_field.setName(String.format("%s, поля", field.getTitle()));
            ArrayList<FieldCoordinate> f_Coordinates = createCoordinates(field);
            a_field.setPoints(f_Coordinates.toArray(new FieldCoordinate[f_Coordinates.size()]));
            String json = mapper.writeValueAsString(a_field);
            setSize(++size);
            antellisFields.add(json);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeFile() {
        try (FileOutputStream stream = new FileOutputStream(ExcelHeaders.ONESOIL_FIELDS_OUTPUT_COORDINATES)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8));
            for (String s : antellisFields) {
                writer.write(String.format("%s%n", s));
            }
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        FieldCreatorModule.size = size;
    }

    public ArrayList<String> getAntellisFields() {
        return antellisFields;
    }

    public void setAntellisFields(ArrayList<String> antellisFields) {
        this.antellisFields = antellisFields;
    }

}
