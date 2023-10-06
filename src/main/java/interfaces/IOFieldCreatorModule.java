package interfaces;

import java.util.ArrayList;

import models.AntellisField;
import models.FieldCoordinate;

public interface IOFieldCreatorModule{
    public ArrayList<FieldCoordinate> createCoordinates();
    public void createHeader();
    public AntellisField createAntellisField();
}