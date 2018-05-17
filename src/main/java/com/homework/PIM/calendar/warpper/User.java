package com.homework.PIM.calendar.warpper;

import java.io.File;

/**
 * @author fzm
 * @date 2018/5/16
 **/
public class User {
    private String userName;
    private String password;
    private String filePath;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User(String userName, String password, String filePath) {
        this.userName = userName;
        this.password = password;
        this.filePath = filePath;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }
}
