package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMAppointment;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/4/13
 **/
public class WrapAppointment {
    private final StringProperty priority;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty description;

    public WrapAppointment(PIMAppointment appointment) {
        this.priority = new SimpleStringProperty(appointment.getPriority());
        this.date = new SimpleObjectProperty<>(appointment.getDate());
        this.description = new SimpleStringProperty(appointment.getDescription());
    }

    public PIMAppointment unWrap(){
        PIMAppointment appointment = new PIMAppointment();
        appointment.setPriority(this.getPriority());
        appointment.setDate(this.getDate());
        appointment.setDescription(this.getDescription());
        return appointment;
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

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
