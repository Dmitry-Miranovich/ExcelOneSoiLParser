package modules;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import constants.ExcelHeaders;
import constants.KmlEnum;
import models.Field;
import models.FieldReaderResponse;
import models.Note;
import models.NoteResponse;

public class FieldGuesserController {

    private ArrayList<NoteResponse> noteResponses;
    private ArrayList<ArrayList<FieldReaderResponse>> fieldResponses;

    public FieldGuesserController(ArrayList<NoteResponse> noteResponses, ArrayList<ArrayList<FieldReaderResponse>> fieldResponses){
        this.noteResponses = noteResponses;
        this.fieldResponses = fieldResponses;
    }

    public FieldGuesserController(){}



    public ArrayList<NoteResponse> getNoteResponses() {
        return noteResponses;
    }

    public void setNoteResponses(ArrayList<NoteResponse> noteResponses) {
        this.noteResponses = noteResponses;
    }

    public ArrayList<ArrayList<FieldReaderResponse>> getFieldResponses() {
        return fieldResponses;
    }

    public void setFieldResponses(ArrayList<ArrayList<FieldReaderResponse>> fieldResponses) {
        this.fieldResponses = fieldResponses;
    }

    public void appendNearestFieldByNote(){
        // String fieldName = "";
        // StringBuilder builder = new StringBuilder();
        for(NoteResponse response : noteResponses){
            for(Note note : response.getData()){
                FieldPoint nearestPoint = getNearestPoint(note);
                // float[] fieldPoint = nearestPoint.getPoint();
                String fieldTitle = nearestPoint.getField().getTitle();
                // builder.append(String.format("Current note is %s%nNearest point to the note mark is x:%f and y:%f of the field %s%n",note.getText(),fieldPoint[0], fieldPoint[1], fieldTitle));
            }
        }
        // try{
        //     String filePath = ExcelHeaders.ONESOIL_FIELDS_OUTPUT_TXT;
        //     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
        //     writer.write(builder.toString());
        //     writer.flush();
        //     writer.close();
        // }catch(IOException exception){
        //     System.out.println(exception.getMessage());
        // }

        /**
         * Три этапа для нахождения, входит ли точка в поле
         * 1) Определение ближайшей точки среди всех полей
         * 2) Определение нормали полученной точки
         * 3) Вычисление скалярного умножения двух векторов
         *      1. В случае, если угол меньше 90 градусов - внутри
         *      2. В противном случае - снаружи
         */
        // return fieldName;
    }

    private FieldPoint getNearestPoint(Note note){
        float nearestDistance = Float.MAX_VALUE;
        float[] nearestPoint = new float[]{0.0f,0.0f};
        FieldPoint point = new FieldPoint();
        for(ArrayList<FieldReaderResponse> readerResponses : fieldResponses){
            for(FieldReaderResponse fieldResponse : readerResponses){
                for(Field field : fieldResponse.getData().getRows()){
                    for(float[] fieldPoint : field.getRealCoordinates()){
                        float currentDist = getDistance(fieldPoint, note.getPoint().getCoordinates());
                        if(nearestDistance > currentDist){
                            nearestDistance = currentDist;
                            nearestPoint = fieldPoint;
                            point.setField(field);
                            point.setPoint(nearestPoint);
                        }
                    }
                }
            }
        }
        return point;
    }

    private float getDistance(float[] pointOne, float[] pointTwo){
        return (float) Math.sqrt(Math.pow(pointTwo[0]-pointOne[0], 2) + Math.pow(pointTwo[1]-pointOne[1], 2));
    }

    private class FieldPoint{
        private Field field;
        private float[] point;

        public FieldPoint(){}

        public FieldPoint(Field field, float[] point){
            this.field = field;
            this.point = point;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public float[] getPoint() {
            return point;
        }

        public void setPoint(float[] point) {
            this.point = point;
        } 
    }

}
