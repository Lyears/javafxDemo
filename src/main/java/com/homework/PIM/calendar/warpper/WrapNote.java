package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMNote;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author fzm
 * @date 2018/4/10
 **/
public class WrapNote {
    private final StringProperty priority;
    private final StringProperty note;

    public WrapNote(PIMNote note){
        this.priority = new SimpleStringProperty(note.getPriority());
        this.note = new SimpleStringProperty(note.getNote());
    }

    public PIMNote unWrap(){
        PIMNote note = new PIMNote();
        note.setPriority(getPriority());
        note.setNote(getNote());
        return note;
    }

    public String getPriority() {
        return priority.get();
    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }
}
