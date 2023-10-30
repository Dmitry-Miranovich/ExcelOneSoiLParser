package modules;

import java.util.ArrayList;

import models.Field;
import models.FieldReaderResponse;
import models.Note;
import models.NoteResponse;

public class FieldGuesserController {

    private ArrayList<NoteResponse> noteResponses;
    private ArrayList<ArrayList<FieldReaderResponse>> fieldResponses;

    public FieldGuesserController(ArrayList<NoteResponse> noteResponses,
            ArrayList<ArrayList<FieldReaderResponse>> fieldResponses) {
        this.noteResponses = noteResponses;
        this.fieldResponses = fieldResponses;
    }

    public FieldGuesserController() {
    }

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

    public void appendNearestFieldByNote(String currentSeasonID, boolean isForAllSeason) {
        for (NoteResponse response : noteResponses) {
            for (Note note : response.getData()) {
                FieldPoint nearestPoint = getNearestPoint(note, currentSeasonID, isForAllSeason);
                String fieldTitle = nearestPoint.getField().getTitle();
                note.setFieldTitle(fieldTitle);
                note.setFieldID(nearestPoint.getField().getId());
            }
        }
    }
    //ОБЯЗАТЕЛЬНО ПОПРАВИТЬ!!!!!!!!!
    private FieldPoint getNearestPoint(Note note, String currentSeasonID, boolean isForAllSeason) {
        float nearestDistance = Float.MAX_VALUE;
        float[] nearestPoint = new float[] { 0.0f, 0.0f };
        FieldPoint point = new FieldPoint();
        for (ArrayList<FieldReaderResponse> readerResponses : fieldResponses) {
            for (FieldReaderResponse fieldResponse : readerResponses) {
                if (isForAllSeason) {
                    for (Field field : fieldResponse.getData().getRows()) {
                        for (float[] fieldPoint : field.getRealCoordinates()) {
                            float currentDist = getDistance(fieldPoint, note.getPoint().getCoordinates());
                            if (nearestDistance > currentDist) {
                                nearestDistance = currentDist;
                                nearestPoint = fieldPoint;
                                point.setField(field);
                                point.setPoint(nearestPoint);
                                note.setSeason_id(field.getSeasonID());
                            }
                        }
                    }
                }
                if (Integer.toString(fieldResponse.getSeasonID()).equals(currentSeasonID)) {
                    for (Field field : fieldResponse.getData().getRows()) {
                        for (float[] fieldPoint : field.getRealCoordinates()) {
                            float currentDist = getDistance(fieldPoint, note.getPoint().getCoordinates());
                            if (nearestDistance > currentDist) {
                                nearestDistance = currentDist;
                                nearestPoint = fieldPoint;
                                point.setField(field);
                                point.setPoint(nearestPoint);
                                note.setSeason_id(field.getSeasonID());
                            }
                        }
                    }
                }
            }
        }
        return point;
    }

    private float getDistance(float[] pointOne, float[] pointTwo) {
        return (float) Math.sqrt(Math.pow(pointTwo[0] - pointOne[0], 2) + Math.pow(pointTwo[1] - pointOne[1], 2));
    }

    // public void filterNoteBySeason(String seasonId){

    // }

    private class FieldPoint {
        private Field field;
        private float[] point;

        public FieldPoint() {
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
