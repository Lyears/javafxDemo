package com.homework.PIM.calendar.controller;

import com.homework.PIM.Collection;
import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapNote;
import com.homework.PIM.entity.PIMNote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public TableColumn<WrapNote,String> noteColumn;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label noteLabel;

    private CalendarMainApp mainApp;

    private ObservableList<WrapNote> notes = FXCollections.observableArrayList();

    public void initialize() {

        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());

        showNoteDetails(null);

        noteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)->showNoteDetails(newValue)
        );
    }

    private void showNoteDetails(WrapNote note){
        if (note!=null){
            priorityLabel.setText(note.getPriority());
            noteLabel.setText(note.getNote());
        }else {
            priorityLabel.setText("");
            noteLabel.setText("");
        }
    }

    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;
        Collection contactCollection = mainApp.getEntities().getNotes();
        for (Object pimNote : contactCollection) {
            WrapNote note = new WrapNote((PIMNote) pimNote);
            notes.add(note);
        }
        noteTable.setItems(notes);
    }

    public ObservableList<WrapNote> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<WrapNote> notes) {
        this.notes = notes;
    }
}
