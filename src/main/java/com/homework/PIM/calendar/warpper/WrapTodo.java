package com.homework.PIM.calendar.warpper;

import com.homework.PIM.calendar.util.LocalDateAdapter;
import com.homework.PIM.entity.PIMTodo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/4/13
 **/
public class WrapTodo extends WrapEntity {
    private final StringProperty priority;
    private final ObjectProperty<LocalDate> time;
    private final StringProperty text;

    public WrapTodo(PIMTodo todo) {
        this.priority = new SimpleStringProperty(todo.getPriority());
        this.time = new SimpleObjectProperty<>(todo.getDate());
        this.text = new SimpleStringProperty(todo.getText());

    }

    public WrapTodo() {
        this(null);
    }

    public PIMTodo unWrap() {
        PIMTodo todo = new PIMTodo();
        todo.setPriority(this.priority.get());
        todo.setDate(this.time.get());
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
    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    @Override
    public StringProperty priorityProperty() {
        return priority;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getTime() {
        return time.get();
    }

    public void setTime(LocalDate date) {
        this.time.set(date);
    }

    public ObjectProperty<LocalDate> timeProperty() {
        return time;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }
}
