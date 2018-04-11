package com.homework.classroom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class Student {
    private String id;

    private Course[] courses = new Course[0];

    Student(String id) {

    }

    Student(String id, Course course) {
        this.id = id;
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        this.courses = courses.toArray(this.courses);
        System.out.println("java CRS" + " " + id + " " + course.getName());
        System.out.print(id + " select " + course.getName() + " with books: ");
        for (Book book : course.getBooks()) {
            System.out.print(" " + book.getName() + ",");
        }
    }

    Student(String id, Course... courses) {
        this.id = id;
        this.courses = courses;
        System.out.print("java CRS " + " " + id + " ");
        for (Course course : courses) {
            System.out.print(course.getName() + " ");
        }
        System.out.println();
        System.out.print(id + " select ");
        for (Course course : courses) {
            System.out.print(course.getName() + " with books");
            for (Book book : course.getBooks()) {
                System.out.print(" " + book.getName());
            }
            System.out.print(",");
        }
    }

//    public  void A(String name ){
//
//        for (Course course : this.courses){
//            if(name.equals(course.getName())) {
//                for (Book book : course.getBooks()) {
//                    System.out.println(book);
//                }
//            }
//        }
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }
}
