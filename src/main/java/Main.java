import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.Semaphore;

import org.apache.xmlbeans.impl.common.Mutex;

import constants.Emails;
import constants.ExcelHeaders;
import models.Crops;
import models.Field;
import models.FieldReaderResponse;
import models.NoteResponse;
import models.SeasonResponse;
import modules.AuthModule;
import modules.ExcelWriterModule;
import modules.FieldCreatorModule;
import modules.FieldDataModule;
import threads.SeasonResponseThread;

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
        Semaphore seasonMutex = new Semaphore(0);
        Semaphore noteMutex = new Semaphore(0);
        for (String email : Emails.emails) {
            String token = authModule.getToken(email);
            fieldModule.setEmail(email);
            // Thread thread = new Thread(new Runnable() {
            // public void run() {
            // SeasonResponse seasonResponse = fieldModule.getSeasonResponse(seasons,
            // token);
            // seasonMutex.release();
            // seasonResponse.setEmail(email);
            // seasonResponses.add(seasonResponse);
            // }
            // });
            SeasonResponse seasonResponse = fieldModule.getSeasonResponse(seasons, token);
            seasonMutex.release();
            seasonResponse.setEmail(email);
            seasonResponses.add(seasonResponse);
            ArrayList<FieldReaderResponse> responses = fieldModule.sampleConnection(seasons, token);
            emailFieldResponses.add(responses);
            // Thread threadNote = new Thread(new Runnable() {
            // public void run() {
            // NoteResponse noteResponse = fieldModule.getNoteResponse(notes, token);
            // noteMutex.release();
            // noteResponses.add(noteResponse);
            // }
            // });
            // threadNote.start();

            NoteResponse noteResponse = fieldModule.getNoteResponse(notes, token);
            noteMutex.release();
            noteResponses.add(noteResponse);
        }
        // for (int i = 0; i < Emails.emails.length; i++) {
        // try {
        // // seasonMutex.acquire();
        // // noteMutex.acquire();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // module.writeAllFields(emailFieldResponses);
        module.writeAllSeasons(seasonResponses);
        // module.writeAllNotes(noteResponses);
        // module.writeFile(module.getBook());

        // FieldCreatorModule antellisModule = new FieldCreatorModule();
        // ArrayList<Integer> seasonIDs = getAllSeasonsID(new GregorianCalendar(2023, 1,
        // 1).getTime(),
        // seasonResponses);
        // for (ArrayList<FieldReaderResponse> responses : emailFieldResponses) {
        // for (FieldReaderResponse response : responses) {
        // for (Field field : response.getData().getRows()) {
        // Crops[] crops = field.getCrops();
        // for (Crops crop : crops) {
        // if (checkSeasonValidity(seasonIDs, crop.getSeason_id())) {
        // antellisModule.writeAntellisField(field);
        // }
        // }
        // }
        // }
        // }
        // antellisModule.writeFile();

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