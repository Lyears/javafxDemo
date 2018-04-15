package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMNote;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author fzm
 * @date 2018/4/10
 **/
public class WrapNote extends WrapEntity {
    private final StringProperty priority;
    private final StringProperty note;

    public WrapNote(PIMNote note) {
        this.priority = new SimpleStringProperty(note.getPriority());
        this.note = new SimpleStringProperty(note.getNote());
    }

    public WrapNote() {
        this.priority = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
    }

    public PIMNote unWrap() {
        PIMNote note = new PIMNote();
        note.setPriority(getPriority());
        note.setNote(getNote());
        return note;
    }

    @Override
    public String getPriority() {
        return priority.get();
    }

    @Override
    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    @Override
    public StringProperty priorityProperty() {
        return priority;
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public StringProperty noteProperty() {
        return note;
    }
}
