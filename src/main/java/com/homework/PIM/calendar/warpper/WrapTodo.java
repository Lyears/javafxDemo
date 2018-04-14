package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMTodo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/4/13
 **/
public class WrapTodo extends WrapEntity {
    private final StringProperty priority;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty text;

    public WrapTodo(PIMTodo todo){
        this.priority = new SimpleStringProperty(todo.getPriority());
        this.date = new SimpleObjectProperty<>(todo.getDate());
        this.text = new SimpleStringProperty(todo.getText());

    }

    public PIMTodo upWrap(){
        PIMTodo todo = new PIMTodo();
        todo.setPriority(this.priority.get());
        todo.setDate(this.date.get());
        todo.setText(this.text.get());
        return todo;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String getPriority() {
        return priority.get();
    }

    @Override
    public StringProperty priorityProperty() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
