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

    private void showNoteDetails(WrapNote note) {
        if (note != null) {
            priorityLabel.setText(note.getPriority());
            noteLabel.setText(note.getNote());
        } else {
            priorityLabel.setText("");
            noteLabel.setText("");
        }
    }

    public void editNoteClick(ActionEvent event) throws Exception {
        MainCalendarController.AlertBox alertBox = mainCalendarController.new AlertBox();
        WrapNote selectNote = noteTable.getSelectionModel().getSelectedItem();
        ObservableList<WrapNote> notes = mainApp.getNotes();
        int index = notes.indexOf(selectNote);
        alertBox.display("编辑联系人", PIMNote.class, selectNote.unWrap());
        boolean okClick = alertBox.isOkClicked();
        //如果点击提交，则将原来的属性替换
        //如果点击关闭，则什么事情也不发生
        if (okClick) {
            selectNote = new WrapNote((PIMNote) alertBox.entity);
            notes.set(index, selectNote);
            noteTable.getSelectionModel().select(selectNote);
            showNoteDetails(selectNote);
        }
    }

    public void deleteNoteClick(ActionEvent event) {
        int selectedIndex = noteTable.getSelectionModel().getSelectedIndex();
        PIMNote note = noteTable.getSelectionModel().getSelectedItem().unWrap();
        noteTable.getItems().remove(selectedIndex);
        mainApp.getEntities().remove(note);
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
