package com.homework.classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class main {
    public static void main(String[] args) {
        Book ThinkInJava = new Book("Think in Java");
        Book Java8 = new Book("Java8");
        Book webEngineering = new Book("WebEngineering");
        Course WebEngineering = new Course("WebEngineering",webEngineering);
        Course java = new Course("java",ThinkInJava,Java8);

        Course [] arrays = {WebEngineering,java};
        Course [] courses = getCourse(args,arrays);

        Student student = new Student(args[0],courses);

    }
//    private static Course getCourse(String name,Course... courses){
//        for (Course course:courses){
//            if (name.equals(course.getName())){
//                return course;
//            }
//        }
//        return null;
//    }

    private static Course[] getCourse(String[] names,Course... courses){
        List<Course> courseList = new ArrayList<>();
        for(String name:names) {
            for (Course course : courses) {
                if (name.equals(course.getName())) {
                    courseList.add(course);
                }
            }
        }
        return courseList.toArray(new Course[0]);
    }
}
