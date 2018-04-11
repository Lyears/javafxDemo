package com.homework.classroom;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class Course {
    private String name;
    private Book[] books;

    Course(String name,Book... books){
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book[] getBooks() {
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }
}
