package modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import models.Field;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import constants.ExcelHeaders;
import models.FieldReaderResponse;
import models.Season;
import models.SeasonResponse;

public class ExcelWriterModule {

    private String filePath = "";
    private static int rowIndex = 0;
    private Workbook book;

    public ExcelWriterModule(){}

    public ExcelWriterModule(String filepath){
        this.filePath = filepath;
        this.book = new XSSFWorkbook();
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
            System.out.println(responses);
            if(responses !=null){
                writeSeasonData(seasonSheet, responses);
            }
        }
        autosizeColumns(seasonSheet);
        clearRow();
    }

    public void writeAllNotes(){

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
            File file = new File(ExcelHeaders.ONESOIL_FIELDS_OUTPUT);
            if(!file.exists()){
                File dir = new File(file.getParent());
                dir.mkdir();
            }
            FileOutputStream stream = new FileOutputStream(filePath);
            book.write(stream);
            book.close();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }    
    }

    private void writeResponseData(Workbook book, Sheet sheet, FieldReaderResponse response){
        Field[] rows = response.getData().getRows();
        for(Field row: rows){
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
            cropCell.setCellValue((row.getCrops().length > 0)?row.getCrops()[0].getCrop():"null");
            sowingDateCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getSowing_date() != null)? row.getCrops()[0].getSowing_date().toString() : "null");
            harvestDateCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getHarvest_date() != null)? row.getCrops()[0].getHarvest_date().toString() : "null");
            yieldValueCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getYield_value() != null)? row.getCrops()[0].getYield_value(): "null");
            yieldValueUnitsCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getYield_value_units() != null)? row.getCrops()[0].getYield_value_units(): "null");
            varietyCell.setCellValue((row.getCrops().length > 0 && row.getCrops()[0].getVariety() != null)? row.getCrops()[0].getVariety(): "null");
            sourceCell.setCellValue((row.getCrops().length > 0)? row.getCrops()[0].getSource(): "null");
            createdAtCell.setCellValue((row.getCrops().length > 0)? row.getCrops()[0].getCreated_at().toString(): "null");
            updatedAtCell.setCellValue( (row.getCrops().length > 0)? row.getCrops()[0].getUpdated_at().toString(): "null");
            seasonIDCell.setCellValue((row.getCrops().length > 0)? row.getCrops()[0].getSeason_id(): 0);
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
            idCell.setCellValue(season.getId());
            titleCell.setCellValue(season.getTitle());
            createdAtCell.setCellValue(season.getStart_date().toString());
            updatedAtCell.setCellValue(season.getEnd_date().toString());
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
