package com.homework.PIM.calendar.warpper;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * @author fzm
 * @date 2018/4/14
 **/
public abstract class WrapEntity{
    private StringProperty priority;

    public WrapEntity() {
        priority = new SimpleStringProperty("normal");
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WrapEntity)) {
            return false;
        }
        WrapEntity entity = (WrapEntity) o;
        return Objects.equals(entity.priority, this.priority);
    }

    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public StringProperty priorityProperty() {
        return priority;
    }
}
