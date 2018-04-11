package com.homework.PIM.entity;

import com.homework.PIM.entity.PIMEntity;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class PIMNote extends PIMEntity {
    private String priority;
    private String note;

    public PIMNote() {
        super();
    }


    public PIMNote(String note) {
        super();
        this.note = note;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return "Note" + " " + (priority == null ? super.getPriority():priority) +
                " " + note +
                ' ';
    }
}
