package constants;

public class ExcelHeaders{
    public static String[] fieldsHeader = new String[]{"ID", "Название", "Площадь", "Культура", "Дата посева", "Дата сбора урожая", "Выходное значение","Единицы выходных значений", "Разнообразие культур", "Источник", "Создано","Обновлено", "ID сезона"};
    public static String[] seasonHeader = new String[]{"ID", "Название", "Создано", "Обновлено"};
    public static String[] notesHeader = new String[]{"ID", "Текст", "Создано", "Обновлено","Создание записки", "ID сезона",};

    public static final String ONESOIL_FIELDS_OUTPUT = "D:/OneSoilPath/onesoil_fields.xlsx";

    public static final int SHEET_ID_INDEX = 0;
    public static final int SHEET_TITLE_INDEX = 1;
    public static final int SHEET_AREA_INDEX = 2;
    public static final int SHEET_CROP_INDEX = 3;
    public static final int SHEET_SOWING_DATE_INDEX = 4;
    public static final int SHEET_HARVEST_DATE_INDEX = 5;
    public static final int SHEET_YIELD_VALUE_INDEX = 6;
    public static final int SHEET_YIELD_VALUE_UNITS_INDEX = 7;
    public static final int SHEET_VARIETY_INDEX = 8;
    public static final int SHEET_SOURCE_INDEX = 9;
    public static final int SHEET_CREATED_AT_INDEX = 10;
    public static final int SHEET_UPDATED_AT_INDEX = 11;
    public static final int SHEET_SEASON_ID_INDEX = 12;

    public static final int SEASON_SHEET_ID_INDEX = 0;
    public static final int SEASON_SHEET_TITLE_INDEX = 1;
    public static final int SEASON_SHEET_CREATED_AT_INDEX = 2;
    public static final int SEASON_SHEET_UPDATED_AT_INDEX = 3;

    public static final String SHEET_FIELD_NAME = "Поля";
    public static final String SHEET_SEASONS_NAME = "Сезоны";
    public static final String SHEET_NOTES_NAME = "Заметки";

}