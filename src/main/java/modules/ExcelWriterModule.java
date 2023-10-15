package modules;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import models.Field;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import constants.ExcelHeaders;
import models.FieldReaderResponse;
import models.Note;
import models.NoteResponse;
import models.Season;
import models.SeasonResponse;

public class ExcelWriterModule {

    private String filePath = "";
    private static int rowIndex = 0;
    private Workbook book;

    public ExcelWriterModule(){}

    public ExcelWriterModule(String filepath){
        this.filePath = filepath;
        this.book = new HSSFWorkbook();
    }

    public void writeAllFields(ArrayList<ArrayList<FieldReaderResponse>> responses){
        Sheet fieldSheet = book.createSheet(ExcelHeaders.SHEET_FIELD_NAME);
        createHeader(fieldSheet, ExcelHeaders.fieldsHeader);
        for(ArrayList<FieldReaderResponse> response: responses){
            for(FieldReaderResponse subResponse: response){
                writeResponse(subResponse, fieldSheet);
            }
        }
        autosizeColumns(fieldSheet);
        clearRow();
    }

    public void writeAllSeasons(ArrayList<SeasonResponse> seasonsResponses){
        Sheet seasonSheet = book.createSheet(ExcelHeaders.SHEET_SEASONS_NAME);
        createHeader(seasonSheet, ExcelHeaders.seasonHeader);
        for(SeasonResponse responses: seasonsResponses){
            if(responses !=null){
                writeSeasonData(seasonSheet, responses);
            }
        }
        autosizeColumns(seasonSheet);
        clearRow();
    }

    public void writeAllNotes(ArrayList<NoteResponse> noteResponses){
        Sheet noteSheet = book.createSheet(ExcelHeaders.SHEET_NOTES_NAME);
        createHeader(noteSheet, ExcelHeaders.notesHeader);
        for(NoteResponse responses: noteResponses){
            if(responses !=null){
                writeNoteData(noteSheet, responses);
            }
        }
        autosizeColumns(noteSheet);
        clearRow();
    }

    private void createHeader(Sheet sheet, String[] excelHeader){
        Row header = sheet.createRow(useRow());
        int headerItemIndex = 0;
        for(String headerItem: excelHeader){
            Cell cell = header.createCell(headerItemIndex);
            cell.setCellValue(headerItem); 
            headerItemIndex++;
        } 
    }


    public void writeResponse(FieldReaderResponse response, Sheet sheet){
        try{
        // createSeasonHeader(sheet, response);
        writeResponseData(book, sheet, response);
        }catch(NullPointerException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void writeFile(Workbook book){
        try{
            File file = new File(filePath);
            if(!file.exists()){
                File dir = new File(file.getParent());
                dir.mkdir();
            }
            FileOutputStream stream = new FileOutputStream(filePath);
            book.write(stream);
            book.close();
            stream.flush();
            stream.close();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }    
    }

    private void writeResponseData(Workbook book, Sheet sheet, FieldReaderResponse response){
        Field[] rows = response.getData().getRows();
        for(Field row: rows){
            if(row.getSeasonID() > 0){
                Row sheetRow = sheet.createRow(useRow());
            Cell IDCell = sheetRow.createCell(ExcelHeaders.SHEET_ID_INDEX);
            Cell titleCell = sheetRow.createCell(ExcelHeaders.SHEET_TITLE_INDEX);
            Cell areaCell = sheetRow.createCell(ExcelHeaders.SHEET_AREA_INDEX);
            Cell cropCell = sheetRow.createCell(ExcelHeaders.SHEET_CROP_INDEX);
            Cell sowingDateCell = sheetRow.createCell(ExcelHeaders.SHEET_SOWING_DATE_INDEX); 
            Cell harvestDateCell = sheetRow.createCell(ExcelHeaders.SHEET_HARVEST_DATE_INDEX);
            Cell yieldValueCell = sheetRow.createCell(ExcelHeaders.SHEET_YIELD_VALUE_INDEX);
            Cell yieldValueUnitsCell = sheetRow.createCell(ExcelHeaders.SHEET_YIELD_VALUE_UNITS_INDEX);
            Cell varietyCell = sheetRow.createCell(ExcelHeaders.SHEET_VARIETY_INDEX);
            Cell sourceCell = sheetRow.createCell(ExcelHeaders.SHEET_SOURCE_INDEX);
            Cell createdAtCell = sheetRow.createCell(ExcelHeaders.SHEET_CREATED_AT_INDEX);
            Cell updatedAtCell = sheetRow.createCell(ExcelHeaders.SHEET_UPDATED_AT_INDEX);
            Cell seasonIDCell = sheetRow.createCell(ExcelHeaders.SHEET_SEASON_ID_INDEX);
            IDCell.setCellValue(row.getId());
            titleCell.setCellValue(row.getTitle());
            areaCell.setCellValue(row.getArea());
            cropCell.setCellValue((row.getCrops().length > 0)?row.getCrops()[0].getCrop():"");
            sowingDateCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getSowing_date() != null)? row.getCrops()[0].getSowing_date().toString() : "");
            harvestDateCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getHarvest_date() != null)? row.getCrops()[0].getHarvest_date().toString() : "");
            yieldValueCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getYield_value() != null)? row.getCrops()[0].getYield_value(): "");
            yieldValueUnitsCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getYield_value_units() != null)? row.getCrops()[0].getYield_value_units(): "");
            varietyCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getVariety() != null)? row.getCrops()[0].getVariety(): "");
            sourceCell.setCellValue((row.getCrops().length > 0)? row.getCrops()[0].getSource(): "");
            createdAtCell.setCellValue((row.getCrops().length > 0)? row.getCrops()[0].getCreated_at().toString(): "");
            updatedAtCell.setCellValue( (row.getCrops().length > 0)? row.getCrops()[0].getUpdated_at().toString(): "");
            seasonIDCell.setCellValue(row.getSeasonID());
            }else{
                System.out.println("bad Field founded");
            }
        }        
    }

    private void writeSeasonData(Sheet sheet, SeasonResponse seasonResponse){
        Season[] seasons = seasonResponse.getData();
        for(Season season: seasons){
            Row row = sheet.createRow(useRow());
            Cell idCell = row.createCell(ExcelHeaders.SEASON_SHEET_ID_INDEX);
            Cell titleCell = row.createCell(ExcelHeaders.SEASON_SHEET_TITLE_INDEX);
            Cell createdAtCell = row.createCell(ExcelHeaders.SEASON_SHEET_CREATED_AT_INDEX);
            Cell updatedAtCell = row.createCell(ExcelHeaders.SEASON_SHEET_UPDATED_AT_INDEX);
            Cell emailCell = row.createCell(ExcelHeaders.SEASON_SHEET_EMAIL_INDEX);
            idCell.setCellValue(season.getId());
            titleCell.setCellValue(season.getTitle());
            createdAtCell.setCellValue(season.getStart_date().toString());
            updatedAtCell.setCellValue(season.getEnd_date().toString());
            emailCell.setCellValue(seasonResponse.getEmail());
        }
    }

    private void writeNoteData(Sheet sheet, NoteResponse noteResponse){
        Note[] notes = noteResponse.getData();
        for(Note note: notes){
            Row row = sheet.createRow(useRow());
            Cell idCell = row.createCell(ExcelHeaders.NOTE_SHEET_ID_INDEX);
            Cell typCell = row.createCell(ExcelHeaders.NOTE_SHEET_TYPE_INDEX);
            Cell textCell = row.createCell(ExcelHeaders.NOTE_SHEET_TEXT_INDEX);
            Cell createdAtCell = row.createCell(ExcelHeaders.NOTE_SHEET_CREATED_AT_INDEX);
            Cell updatedAtCell = row.createCell(ExcelHeaders.NOTE_SHEET_UPDATED_AT_INDEX);
            Cell unitsPerMeterCell = row.createCell(ExcelHeaders.NOTE_SHEET_UNITS_PER_METER_INDEX);
            Cell seasonIDCell = row.createCell(ExcelHeaders.NOTE_SHEET_SEASON_ID_INDEX);
            Cell sharingCodeCell = row.createCell(ExcelHeaders.NOTE_SHEET_SHARING_CODE_INDEX);
            Cell fieldTitleCell = row.createCell(ExcelHeaders.NOTE_SHEET_FIELDNAME_INDEX);
            Cell fieldIDCell = row.createCell(ExcelHeaders.NOTE_SHEET_FIELD_ID_INDEX);
            idCell.setCellValue((note.getId() > 0)?note.getId(): 0);
            typCell.setCellValue((note.getType() != null)?note.getType(): "");
            textCell.setCellValue((note.getText() != null)?note.getText(): "");
            createdAtCell.setCellValue((note.getCreated_at() != null)?note.getCreated_at().toString(): "");
            updatedAtCell.setCellValue((note.getUpdated_at() != null)?note.getUpdated_at().toString(): "");
            unitsPerMeterCell.setCellValue((note.getUnits_per_meter() != null)? note.getUnits_per_meter(): "");
            seasonIDCell.setCellValue((note.getSeason_id() >0)? note.getSeason_id(): 0);
            sharingCodeCell.setCellValue((note.getSharing_code() != null)? note.getSharing_code(): "");
            fieldTitleCell.setCellValue((note.getFieldTitle() != null)? note.getFieldTitle(): "");
            fieldIDCell.setCellValue((note.getFieldID() > 0)? note.getFieldID(): -1);
        }
    }

    private void createSeasonHeader(Sheet sheet, FieldReaderResponse response){
        String season = response.getSeasonName();
        Row row = sheet.createRow(useRow());
        Cell seasonIntro = row.createCell(0);
        Cell seasonData = row.createCell(1);
        seasonIntro.setCellValue("Текущий сезон: ");
        seasonData.setCellValue(season);
    }

    private void autosizeColumns(Sheet sheet){
        for(int i = 0; i< ExcelHeaders.ONESOIL_FIELDS_OUTPUT.length(); i++){
            sheet.autoSizeColumn(i);
        }
    }
    private int useRow(){
        return rowIndex++;
    }

    private void clearRow(){
        rowIndex = 0;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static int getRowIndex() {
        return rowIndex;
    }

    public static void setRowIndex(int rowIndex) {
        ExcelWriterModule.rowIndex = rowIndex;
    }

    public Workbook getBook() {
        return book;
    }

    public void setBook(Workbook book) {
        this.book = book;
    }
}
