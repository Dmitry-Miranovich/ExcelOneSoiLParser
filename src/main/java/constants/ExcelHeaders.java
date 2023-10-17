package constants;

public class ExcelHeaders{
    public static String[] fieldsHeader = new String[]{"ID п", "Номер поля","Урочища", "Площадь", "Культура", "Дата посева", "Дата сбора урожая", "Выходное значение","Единицы выходных значений", "Разнообразие культур", "Источник", "Создано","Обновлено", "ID сезона"};
    public static String[] seasonHeader = new String[]{"ID с", "Название", "С", "По", "Email"};
    public static String[] notesHeader = new String[]{"ID з","Тип", "Текст", "Создано", "Обновлено","Единицы на метр", "ID сезона","Код сезона", "Имя поля", "Код поля"};

    public static final String ONESOIL_FIELDS_OUTPUT = "D:/OneSoilPath/onesoil_fields.xls";
    public static final String ONESOIL_FIELDS_OUTPUT_COORDINATES = "D:/OneSoilPath/onesoil_fields.zns";
    public static final String ONESOIL_FIELDS_OUTPUT_KML = "D:/OneSoilPath/onesoil_fields.kml";
    public static final String ONESOIL_FIELDS_OUTPUT_TXT = "D:/OneSoilPath/onesoil_fields.txt";

    public static final int SHEET_ID_INDEX = 0;
    public static final int SHEET_FIELD_NUMBER_INDEX = 1;
    public static final int SHEET_NAME_INDEX = 2;
    public static final int SHEET_AREA_INDEX = 3;
    public static final int SHEET_CROP_INDEX = 4;
    public static final int SHEET_SOWING_DATE_INDEX = 5;
    public static final int SHEET_HARVEST_DATE_INDEX = 6;
    public static final int SHEET_YIELD_VALUE_INDEX = 7;
    public static final int SHEET_YIELD_VALUE_UNITS_INDEX = 8;
    public static final int SHEET_VARIETY_INDEX = 9;
    public static final int SHEET_SOURCE_INDEX = 10;
    public static final int SHEET_CREATED_AT_INDEX = 11;
    public static final int SHEET_UPDATED_AT_INDEX = 12;
    public static final int SHEET_SEASON_ID_INDEX = 13;

    public static final int SEASON_SHEET_ID_INDEX = 0;
    public static final int SEASON_SHEET_TITLE_INDEX = 1;
    public static final int SEASON_SHEET_CREATED_AT_INDEX = 2;
    public static final int SEASON_SHEET_UPDATED_AT_INDEX = 3;
    public static final int SEASON_SHEET_EMAIL_INDEX = 4;

     public static final int NOTE_SHEET_ID_INDEX = 0;
     public static final int NOTE_SHEET_TYPE_INDEX = 1;
     public static final int NOTE_SHEET_TEXT_INDEX = 2;
     public static final int NOTE_SHEET_CREATED_AT_INDEX = 3;
     public static final int NOTE_SHEET_UPDATED_AT_INDEX = 4;
     public static final int NOTE_SHEET_UNITS_PER_METER_INDEX = 5;
     public static final int NOTE_SHEET_SEASON_ID_INDEX = 6;
     public static final int NOTE_SHEET_SHARING_CODE_INDEX = 7;
     public static final int NOTE_SHEET_FIELDNAME_INDEX = 8;
     public static final int NOTE_SHEET_FIELD_ID_INDEX = 9;
    /*
     * private int id;
    private String uuid;
    private String type;
    private String text;
    private Date created_at;
    private Date update_at;
    private NotePoint point;
    private String units_per_meter;
    private String color;
    private int season_id;
    private boolean is_deleted;
    private String field_user_season_uuid;
    private String sharing_code;
     */

    public static final String SHEET_FIELD_NAME = "Поля";
    public static final String SHEET_SEASONS_NAME = "Сезоны";
    public static final String SHEET_NOTES_NAME = "Заметки";

}