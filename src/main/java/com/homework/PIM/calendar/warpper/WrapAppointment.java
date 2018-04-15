package com.homework.PIM.calendar.warpper;

import com.homework.PIM.calendar.util.LocalDateAdapter;
import com.homework.PIM.entity.PIMAppointment;
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
public class WrapAppointment extends WrapEntity {
    private final StringProperty priority;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty description;

    public WrapAppointment(PIMAppointment appointment) {
        this.priority = new SimpleStringProperty(appointment.getPriority());
        this.date = new SimpleObjectProperty<>(appointment.getDate());
        this.description = new SimpleStringProperty(appointment.getDescription());
    }

    public WrapAppointment() {
        this.priority = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty();
    }

    public PIMAppointment unWrap() {
        PIMAppointment appointment = new PIMAppointment();
        appointment.setPriority(this.getPriority());
        appointment.setDate(this.getDate());
        appointment.setDescription(this.getDescription());
        return appointment;
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
    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
