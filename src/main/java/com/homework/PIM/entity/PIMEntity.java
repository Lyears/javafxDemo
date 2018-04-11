package com.homework.PIM.entity;

import java.io.Serializable;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public abstract class PIMEntity implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private String priority;

    PIMEntity() {
        priority = "normal";
    }

    PIMEntity(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        priority = priority;
    }

    abstract public void fromString(String s);

    @Override
    abstract public String toString();


}
