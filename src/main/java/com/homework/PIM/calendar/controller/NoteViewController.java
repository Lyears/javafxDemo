package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapNote;
import com.homework.PIM.entity.PIMNote;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 * @author fzm
 * @date 2018/4/10
 **/
public class NoteViewController {
    public TableView<WrapNote> noteTable;
    public TableColumn<WrapNote, String> noteColumn;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label noteLabel;

    private CalendarMainApp mainApp;


    private MainCalendarController mainCalendarController;

    public void initialize() {

        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());

        showNoteDetails(null);

        noteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showNoteDetails(newValue)
        );
    }

    public void showNoteDetails(WrapNote note) {
        if (note != null) {
            priorityLabel.setText(note.getPriority());
            noteLabel.setText(note.getNote());
        } else {
            priorityLabel.setText("");
            noteLabel.setText("");
        }
    }

    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;

        noteTable.setItems(mainApp.getNotes());
    }

    public MainCalendarController getMainCalendarController() {
        return mainCalendarController;
    }

    public void setMainCalendarController(MainCalendarController mainCalendarController) {
        this.mainCalendarController = mainCalendarController;
    }
}
