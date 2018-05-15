package com.homework.PIM.calendar.warpper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * @author fzm
 * @date 2018/4/15
 **/
@XmlRootElement(name = "items")
public class ItemsListWrapper {
    private Map<String,Object> privateInfoMap;
    private List<WrapAppointment> appointments;
    private List<WrapContact> contacts;
    private List<WrapTodo> todos;
    private List<WrapNote> notes;

    public Map<String, Object> getPrivateInfoMap() {
        return privateInfoMap;
    }

    public void setPrivateInfoMap(Map<String, Object> privateInfoMap) {
        this.privateInfoMap = privateInfoMap;
    }

    @XmlElement(name = "appointment")
    public List<WrapAppointment> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<WrapAppointment> appointments) {
        this.appointments = appointments;
    }
    @XmlElement(name = "contact")
    public List<WrapContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<WrapContact> contacts) {
        this.contacts = contacts;
    }

    @XmlElement(name = "todo")
    public List<WrapTodo> getTodos() {
        return todos;
    }

    public void setTodos(List<WrapTodo> todos) {
        this.todos = todos;
    }

    @XmlElement(name = "note")
    public List<WrapNote> getNotes() {
        return notes;
    }

    public void setNotes(List<WrapNote> notes) {
        this.notes = notes;
    }
}
