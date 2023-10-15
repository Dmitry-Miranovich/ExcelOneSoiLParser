package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import constants.Warnings;
import exceptions.MissingDateException;
import exceptions.MissingFilePathException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class AntelliseController implements Initializable {

    private GregorianCalendar startDate;
    private String filePath;
    private MainController mainController;
    public static Stage stage;

    @FXML
    private VBox antellisBox;

    @FXML
    private Button antellisGenerateButton;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button antellisFilePathButton;
    @FXML
    private TextField antellisFilePathTextField;
    @FXML
    private Button antelliseFieldGenerateButton;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        mainController = new MainController();
    }
    //actions

    @FXML
    public void onClickSetFilePath(ActionEvent event){
        FileChooser dialogFilePath = new FileChooser();
        dialogFilePath.setInitialFileName("one_soil_file.zns");
        ExtensionFilter excelFilter = new ExtensionFilter("Antellise fields", "*.zns");
        ExtensionFilter modernExcelFilter = new ExtensionFilter("Google Earth file", ".kml");
        dialogFilePath.getExtensionFilters().addAll(excelFilter, modernExcelFilter);
        File excelPrecreatedFile = dialogFilePath.showSaveDialog(stage);
        if (excelPrecreatedFile != null) {
            filePath = excelPrecreatedFile.getAbsolutePath();
            antellisFilePathTextField.setText(filePath);
        }
    }

    @FXML
    public void onClickGetStartDate(ActionEvent event){
        DatePicker curDate = (DatePicker) event.getTarget();
        LocalDate date = curDate.getValue();
        startDate = new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth());        
    }
    @FXML
    public void onClickGetEndDate(ActionEvent event){

    }
    @FXML
    public void onClickGenerateFieldFile(){
        try{
            generateFieldFile();
        }catch(MissingDateException dateException){
            mainController.showWarningMessage(dateException.getMessage());
        }catch(MissingFilePathException filePathException){
            mainController.showWarningMessage(filePathException.getMessage());
        }
    }

    private void generateFieldFile() throws MissingFilePathException, MissingDateException{
        if(startDate == null){
            throw new MissingDateException(Warnings.NO_DATE_WARNING);
        }
        if(filePath == null){
            throw new MissingFilePathException(Warnings.NO_FILE_WARNING);
        }
        if(mainController.getSelectedEmail() == null){
            throw new MissingFilePathException(Warnings.NO_SEASONS_WARNING);
        }
    }
}
