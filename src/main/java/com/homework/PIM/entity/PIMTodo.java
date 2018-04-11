package com.homework.PIM.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class PIMTodo extends PIMEntity {

    private String priority;
    private LocalDate date;
    private String text;

    public PIMTodo() {
        super();
    }

    public PIMTodo(LocalDate date, String text) {
        super();
        this.date = date;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void fromString(String s) {
        String[] args = s.split(" ");
        this.priority = args[0];
        this.date = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        this.text = args[2];
    }

    @Override
    public String toString() {
        return "TODO" + " " + (priority == null ? super.getPriority() : priority) +
                " " + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear() +
                " " + text +
                ' ';
    }
}
