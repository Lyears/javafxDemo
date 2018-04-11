package com.homework.PIM.entity;

import com.homework.PIM.entity.PIMEntity;

import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class PIMAppointment extends PIMEntity {

    private String priority;
    private LocalDate date;
    private String description;

    public PIMAppointment() {
        super();
    }

    public PIMAppointment(LocalDate date, String description) {
        super();
        this.date = date;
        this.description = description;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return "Appointment " + " " + (priority == null ? super.getPriority():priority) +
                " " + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear() +
                " " + description +
                ' ';
    }
}
