package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * @author fzm
 * @date 2018/4/15
 **/
public class RootLayoutController {

    private CalendarMainApp mainApp;

    @FXML
    private void handleNew() {
        mainApp.getTodos().clear();
        mainApp.getAppointments().clear();
        mainApp.getNotes().clear();
        mainApp.getContacts().clear();
        mainApp.setPersonFilePath(null);
    }
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // 设置支持的文件类型
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {

    }
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;
    }
}
