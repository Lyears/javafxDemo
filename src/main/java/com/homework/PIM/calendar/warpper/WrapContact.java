package com.homework.PIM.calendar.warpper;

import com.homework.PIM.entity.PIMContact;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * @author fzm
 * @date 2018/4/10
 **/
public class WrapContact extends WrapEntity {
    private final StringProperty priority;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;

    public WrapContact(PIMContact contact) {
        this.priority = new SimpleStringProperty(contact.getPriority());
        this.firstName = new SimpleStringProperty(contact.getFirstName());
        this.lastName = new SimpleStringProperty(contact.getLastName());
        this.email = new SimpleStringProperty(contact.getEmail());
    }

    public WrapContact() {
        this.priority = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public PIMContact unWrap() {
        PIMContact contact = new PIMContact();
        contact.setPriority(this.getPriority());
        contact.setFirstName(this.getFirstName());
        contact.setLastName(this.getLastName());
        contact.setEmail(this.getEmail());
        return contact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, firstName, lastName, email);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WrapContact)) {
            return false;
        }
        WrapContact contact = (WrapContact) o;
        return Objects.equals(contact.priority, priority)
                && Objects.equals(contact.firstName, firstName)
                && Objects.equals(contact.lastName, lastName)
                && Objects.equals(contact.email, email);
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

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
}
