package com.homework.PIM.entity;

import com.homework.PIM.entity.PIMEntity;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class PIMContact extends PIMEntity {

    private String priority;
    private String firstName;
    private String lastName;
    private String email;

    public PIMContact() {
        super();
    }

    public PIMContact(String firstName, String lastName, String email) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void fromString(String s) {

    }

    @Override
    public String toString() {
        return "Contact" + " " + (priority == null ? super.getPriority():priority) +
                " " + firstName +
                " " + lastName +
                " " + email +
                ' ';
    }
}
