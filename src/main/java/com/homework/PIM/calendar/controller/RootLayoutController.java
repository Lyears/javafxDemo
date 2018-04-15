package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        // 打开文件选择窗口
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }

    /**
     * 保存文件，如果没有对应的持久化文件，就使用另存为方法。
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
     * 加载一个文件选择器选择文件并保存
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // 设置文件类型
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // 打开一个文件选择窗口
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // 确保文件后缀为.xml
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * 打开一个关于窗口
     */
    @FXML
    private void handleAbout() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION, "作者: 范知名\n 博客: https:lyears.github.io");
        aboutAlert.setTitle("关于");
        aboutAlert.setHeaderText("关于作者");
        aboutAlert.showAndWait();
    }

    /**
     * 关闭应用
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
