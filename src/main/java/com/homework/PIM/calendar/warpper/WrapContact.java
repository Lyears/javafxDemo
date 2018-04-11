package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMContact;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author fzm
 * @date 2018/4/10
 **/
public class WrapContact {
    private final StringProperty priority;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;

    public WrapContact(PIMContact contact){
        this.priority = new SimpleStringProperty(contact.getPriority());
        this.firstName = new SimpleStringProperty(contact.getFirstName());
        this.lastName = new SimpleStringProperty(contact.getLastName());
        this.email = new SimpleStringProperty(contact.getEmail());
    }

    public PIMContact unWrap(){
        PIMContact contact = new PIMContact();
        contact.setPriority(this.getPriority());
        contact.setFirstName(this.getFirstName());
        contact.setLastName(this.getLastName());
        contact.setEmail(this.getEmail());
        return contact;
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

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
