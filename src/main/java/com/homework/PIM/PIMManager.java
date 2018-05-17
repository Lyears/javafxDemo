package com.homework.PIM;

import com.homework.PIM.collection.Collection;
import com.homework.PIM.collection.PIMCollectionImp;
import com.homework.PIM.entity.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * @author fzm
 * @date 2018/3/27
 **/
public class PIMManager {
    private static DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        boolean running = true;
        Collection<PIMEntity> pimEntities = new PIMCollectionImp<>();
        while (running) {
            String in;
            System.out.println("---Enter a command (suported commands are List Create Save Load Quit)---");
            Scanner sc = new Scanner(System.in);
            in = sc.nextLine();
            if ("Quit".equals(in)) {
                running = false;
            }
            if ("Save".equals(in)) {
                save(pimEntities);
            }
            if ("Load".equals(in)) {
                pimEntities.addAll(load());
            }
            if ("List".equals(in)) {
                Out(pimEntities, "items");
            }
            if ("Notes".equals(in)) {
                Collection notes = pimEntities.getNotes();
                Out(notes, "notes");
            }
            if ("Todos".equals(in)) {
                Collection todos = pimEntities.getTodos();
                Out(todos, "todos");
            }
            if ("Appointments".equals(in)) {
                Collection appointments = pimEntities.getAppointments();
                Out(appointments, "appointments");
            }
            if ("Contacts".equals(in)) {
                Collection contacts = pimEntities.getContact();
                Out(contacts, "contacts");
            }
            if ("ForDate".equals(in)) {
                System.out.println("Enter date for Search:");
                String forDate = sc.nextLine();
                LocalDate date = LocalDate.parse(forDate, mdy);
                Collection result = pimEntities.getItemsForDate(date);
                Out(result, "items");
            }
            if ("Create".equals(in)) {
                String itemType = null,
                        priority;
                System.out.println("Enter an item type ( todo, note, contact or appointment )");
                itemType = sc.nextLine();
                switch (itemType) {
                    case "todo": {
                        System.out.println("Enter date for todo item:");
                        String date = sc.nextLine();
                        LocalDate localDate = null;
                        boolean flag = true;
                        localDate = getLocalDate(mdy, sc, date, localDate, flag);
                        System.out.println("Enter todo text:");
                        String text = sc.nextLine();
                        System.out.println("Enter todo priority:");
                        priority = sc.nextLine();
                        PIMTodo pimTodo = new PIMTodo(localDate, text);
                        if (!"".equals(priority)) {
                            pimTodo.setPriority(priority);
                        }
                        pimEntities.add(pimTodo);
                    }
                    break;
                    case "note": {
                        System.out.println("Enter note text:");
                        String text = sc.nextLine();
                        System.out.println("Enter note priority:");
                        priority = sc.nextLine();
                        PIMNote pimNote = new PIMNote(text);
                        if (!"".equals(priority)) {
                            pimNote.setPriority(priority);
                        }
                        pimEntities.add(pimNote);
                    }
                    break;
                    case "contact": {
                        System.out.println("Enter contact first name:");
                        String firstName = sc.nextLine();
                        System.out.println("Enter contact last name:");
                        String lastName = sc.nextLine();
                        System.out.println("Enter contact email name:");
                        String email = sc.nextLine();
                        System.out.println("Enter contact priority:");
                        priority = sc.nextLine();
                        PIMContact pimContact = new PIMContact(firstName, lastName, email);
                        if (!"".equals(priority)) {
                            pimContact.setPriority(priority);
                        }
                        pimEntities.add(pimContact);
                    }
                    break;
                    case "appointment": {
                        System.out.println("Enter date for appointment item:");
                        String date = sc.nextLine();
                        LocalDate localDate = null;
                        boolean flag = true;
                        localDate = getLocalDate(mdy, sc, date, localDate, flag);
                        System.out.println("Enter appointment description:");
                        String description = sc.nextLine();
                        System.out.println("Enter appointment priority:");
                        priority = sc.nextLine();
                        PIMAppointment pimAppointment = new PIMAppointment(localDate, description);
                        if (!"".equals(priority)) {
                            pimAppointment.setPriority(priority);
                        }
                        pimEntities.add(pimAppointment);

                    }
                    break;
                    default: {
                    }
                    break;
                }
            }
        }
    }

    private static void Out(Collection<PIMEntity> result, String name) {
        if (result.size() == 0) {
            System.out.println("There are 0 " + name + ".");
        } else {
            System.out.println();
            int index = 0;
            for (PIMEntity item : result) {
                System.out.println(name + " " + (index + 1) + ": " + item.toString());
                index++;
            }
        }
    }

    private static LocalDate getLocalDate(DateTimeFormatter mdy, Scanner sc, String date, LocalDate localDate, boolean flag) {
        while (flag) {
            try {
                localDate = LocalDate.parse(date, mdy);
                flag = false;
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter again:");
                date = sc.nextLine();
            }
        }
        return localDate;
    }

    public static void save(List<PIMEntity> t) throws IOException {
        String filePath = "/Users/fzm/Desktop/PIM.ser";
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(t);
        out.close();
        outputStream.close();
        System.out.println("Items have been saved.");
    }

    public static List<PIMEntity> load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("/Users/fzm/Desktop/PIM.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (List<PIMEntity>) objectInputStream.readObject();
    }

}
