import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

import constants.Emails;
import constants.ExcelHeaders;
import constants.KmlEnum;
import models.Crops;
import models.Field;
import models.FieldReaderResponse;
import models.NoteResponse;
import models.SeasonResponse;
import modules.AuthModule;
import modules.ExcelWriterModule;
import modules.FieldCreatorModule;
import modules.FieldDataModule;
import modules.FieldGuesserController;
import modules.KmlCreatorController;

public class Main {
    public static void main(String[] args) {
        // String email = "Loshnickijkrai@gmail.com";
        // String password = "TXyu7c";

        AuthModule authModule = new AuthModule();
        ExcelWriterModule module = new ExcelWriterModule(ExcelHeaders.ONESOIL_FIELDS_OUTPUT);
        FieldDataModule fieldModule = new FieldDataModule(module);
        String seasons = "https://platform-api.onesoil.ai/ru/v1/seasons";
        String notes = "https://platform-api.onesoil.ai/en/v2/notes";
        ArrayList<ArrayList<FieldReaderResponse>> emailFieldResponses = new ArrayList<>();
        ArrayList<SeasonResponse> seasonResponses = new ArrayList<>();
        ArrayList<NoteResponse> noteResponses = new ArrayList<>();
        for (String email : Emails.emails) {
            try {
                String token = authModule.getToken(email);
                fieldModule.setEmail(email);
                SeasonResponse seasonResponse = fieldModule.getSeasonResponse(seasons, token);
                seasonResponse.setEmail(email);
                seasonResponses.add(seasonResponse);
                ArrayList<FieldReaderResponse> responses = fieldModule.sampleConnection(seasonResponse, seasons, token);
                emailFieldResponses.add(responses);
                NoteResponse noteResponse = fieldModule.getNoteResponse(notes, token);
                noteResponses.add(noteResponse);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        module.writeAllFields(emailFieldResponses);
        module.writeAllSeasons(seasonResponses);
        module.writeAllNotes(noteResponses);
        module.writeFile(module.getBook());
        FieldGuesserController fieldGuesser = new FieldGuesserController(noteResponses, emailFieldResponses);
        
        fieldGuesser.getNearestFieldByNote();

        FieldCreatorModule antellisModule = new FieldCreatorModule();
        ArrayList<Integer> seasonIDs = getAllSeasonsID(new GregorianCalendar(2023, 1,
                1).getTime(),
                seasonResponses);
        KmlCreatorController controller = new KmlCreatorController();
        String headerProp = "xmlns=\"http://www.opengis.net/kml/2.2\"";
        String styleURL = "#AREA_FFFFFFFF";
        String styleID = "AREA_FFFFFFFF";
        String styleColorLine = "FFFFFFFF";
        String styleColorPoly = "80FFFFFF";
        String widthStyle = "2";
        String fillStyle = "1";
        String outlineStyle = "1";
        controller.setProp(KmlEnum.KML, headerProp);
        controller.createKmlStyle(styleID, styleColorLine, widthStyle, styleColorPoly, fillStyle, outlineStyle);
        for (ArrayList<FieldReaderResponse> responses : emailFieldResponses) {
            for (FieldReaderResponse response : responses) {
                for (Field field : response.getData().getRows()) {
                    Crops[] crops = field.getCrops();
                    for (Crops crop : crops) {
                        if (checkSeasonValidity(seasonIDs, crop.getSeason_id())) {
                            antellisModule.writeAntellisField(field);
                            String[] fieldCoordinates = new String[field.getRealCoordinates().length];
                            int index = 0;
                            for (float[] coords : field.getRealCoordinates()) {
                                fieldCoordinates[index] = (String.format((index != fieldCoordinates.length - 1 ? " %s,%s," : " %s,%s"), coords[0],coords[1]));
                                index++;
                            }
                            controller.createSpecialFieldPlacemark(field.getTitle(),
                            String.format("%s группа", field.getTitle()), styleURL, fieldCoordinates);
                        }
                    }
                }
            }
        }
        antellisModule.writeFile();
        controller.endKML();
        controller.writeFile(controller.getFullKMLString());
    }

    private static ArrayList<Integer> getAllSeasonsID(Date date, ArrayList<SeasonResponse> responses) {
        ArrayList<Integer> seasonIDs = new ArrayList<>();
        for (SeasonResponse response : responses) {
            ArrayList<Integer> seasons = response.getSeasonID(date);
            for (int season : seasons) {
                seasonIDs.add(season);
            }
        }
        return seasonIDs;
    }

    private static boolean checkSeasonValidity(ArrayList<Integer> seasonIDs, int season) {
        boolean isSeasonExist = false;
        for (int id : seasonIDs) {
            if (id == season) {
                isSeasonExist = true;
                break;
            }
        }
        return isSeasonExist;
    }
}