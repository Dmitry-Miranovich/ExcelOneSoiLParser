package modules;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import constants.ExcelHeaders;
import constants.KmlEnum;
import interfaces.IOKmlCreatorController;

public class KmlCreatorController implements IOKmlCreatorController{

    private ArrayList<String> kmlStringValues = new ArrayList<>();

    public KmlCreatorController(){
        createHeader();
    }

    @Override
    public void addProp(KmlEnum token, String value) {
        String tempToken = token.openAction() + value + token.closeAction();
        kmlStringValues.add(tempToken + "\n");
    }

    @Override
    public void addProp(KmlEnum token, String[] values) {
        String tempToken = token.openAction();
        for(String value: values){
            tempToken += value;
        }
        tempToken+=String.format("%s%n", token.closeAction());
        kmlStringValues.add(tempToken);
    }

    @Override
    public void setProp(KmlEnum token, String value) {
        String tempToken = token.openAction().replace(">", String.format(" %s>%n", value));
        kmlStringValues.add(tempToken); 
    }

    public ArrayList<String> getKmlStringValues() {
        return kmlStringValues;
    }

    public void setKmlStringValues(ArrayList<String> kmlStringValues) {
        this.kmlStringValues = kmlStringValues;
    }
    public void createSpecialFieldPlacemark(String name, String description, String styleURL, String[] coordinates){
        kmlStringValues.add(KmlEnum.PLACEMARK.openAction() + "\n");
        addProp(KmlEnum.NAME, name);
        addProp(KmlEnum.DESCRIPTION, description);
        addProp(KmlEnum.STYLEURL, styleURL);
        kmlStringValues.add(KmlEnum.POLYGON.openAction() + "\n");
        kmlStringValues.add(KmlEnum.OUTERBOUNDARYIS.openAction() + "\n");
        kmlStringValues.add(KmlEnum.LINEARRING.openAction() + "\n");
        addProp(KmlEnum.COORDINATES, coordinates);
        kmlStringValues.add(KmlEnum.LINEARRING.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.OUTERBOUNDARYIS.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.POLYGON.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.PLACEMARK.closeAction() + "\n");
    }

    public void createKmlStyle(String styleID, String color, String width, String polyColor, String fill, String outline){
        kmlStringValues.add(KmlEnum.DOCUMENT.openAction() + "\n");
        setProp(KmlEnum.KML.STYLE, String.format("id=\"%s\"", styleID));
        kmlStringValues.add(KmlEnum.LINESTYLE.openAction() + "\n");
        addProp(KmlEnum.COLOR, color);
        addProp(KmlEnum.WIDTH, width);
        kmlStringValues.add(KmlEnum.LINESTYLE.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.POLYSTYLE.openAction() + "\n");
        addProp(KmlEnum.COLOR, polyColor);
        addProp(KmlEnum.FILL, fill);
        addProp(KmlEnum.OUTLINE, outline);
        kmlStringValues.add(KmlEnum.POLYSTYLE.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.STYLE.closeAction() + "\n");
    }
    public void endKML(){
        kmlStringValues.add(KmlEnum.DOCUMENT.closeAction() + "\n");
        kmlStringValues.add(KmlEnum.KML.closeAction() + "\n");
        
    }
    public String getFullKMLString(){
        StringBuilder kmlFile = new StringBuilder();
        for(String kmlPart: kmlStringValues){
            kmlFile.append(kmlPart);
        }
        return kmlFile.toString();
    }

    public void writeFile(String text){
        try{
            String path = ExcelHeaders.ONESOIL_FIELDS_OUTPUT_KML;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
            writer.write(text);
            writer.flush();
            writer.close();
        }catch(IOException ioEx){

        }
    }

    public void createHeader(){
        kmlStringValues.add("<?xml version='1.0' encoding='UTF-8' ?>\n");
    }
    
}
