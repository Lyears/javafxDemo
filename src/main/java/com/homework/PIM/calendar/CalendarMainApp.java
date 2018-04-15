package com.homework.PIM.calendar;

import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
import com.homework.PIM.PIMManager;
import com.homework.PIM.calendar.controller.MainCalendarController;
import com.homework.PIM.calendar.controller.RootLayoutController;
import com.homework.PIM.calendar.warpper.*;
import com.homework.PIM.entity.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author fzm
 * @date 2018/3/31
 */
public class CalendarMainApp extends Application {

    private BorderPane rootLayout;
    private BorderPane calendarMainView;
    private Stage primaryStage;

    private ObservableList<WrapContact> contacts = FXCollections.observableArrayList();
    private ObservableList<WrapNote> notes = FXCollections.observableArrayList();
    private ObservableList<WrapTodo> todos = FXCollections.observableArrayList();
    private ObservableList<WrapAppointment> appointments = FXCollections.observableArrayList();
    private ObjectProperty<WrapEntity> selectedItem = new SimpleObjectProperty<>();

    /**
     * 新建一个集体参数
     */
    private Collection<PIMEntity> entities = new PIMCollection<>();
    private List<WrapContact> wrapContacts = new ArrayList<>();
    private List<WrapNote> wrapNotes = new ArrayList<>();
    private List<WrapTodo> wrapTodos = new ArrayList<>();
    private List<WrapAppointment> wrapAppointments = new ArrayList<>();

    public CalendarMainApp() throws Exception {
        //在初始化Calendar页面时加载持久化对象
        entities.addAll(PIMManager.load());

        Collection contactCollection = entities.getContact();
        for (Object pimContact : contactCollection) {
            //将实体类转化为包装类，便于数据的改动与添加及时与控制器绑定
            WrapContact contact = new WrapContact((PIMContact) pimContact);
            contacts.add(contact);
        }

        Collection noteCollection = entities.getNotes();
        for (Object pimNote : noteCollection) {
            WrapNote note = new WrapNote((PIMNote) pimNote);
            notes.add(note);
        }
        Collection todoCollection = entities.getTodos();
        for (Object pimTodo : todoCollection) {
            WrapTodo todo = new WrapTodo((PIMTodo) pimTodo);
            todos.add(todo);
        }
        Collection appointmentCollection = entities.getAppointments();
        for (Object pimAppointment : appointmentCollection) {
            WrapAppointment appointment = new WrapAppointment((PIMAppointment) pimAppointment);
            appointments.add(appointment);
        }
        wrapContacts.addAll(contacts);
        wrapNotes.addAll(notes);
        wrapTodos.addAll(todos);
        wrapAppointments.addAll(appointments);

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initRootLayout();
        initCalendar();

    }

    public void initRootLayout() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
        this.rootLayout = loader.load();
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setMainApp(this);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initCalendar() throws Exception {

        // TODO: 2018/4/10 重构持久化加载方式
        //加载主视图
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainCalendar.fxml"));
        this.calendarMainView = loader.load();
        MainCalendarController mainCalendarController = loader.getController();
        mainCalendarController.setMainApp(this);
        rootLayout.setCenter(calendarMainView);
        primaryStage.setTitle("日历-个人管理系统");

        //加载日历视图
        mainCalendarController.setCenterPane("CalendarView", this);
        //为每个更改和删除操作添加监听
        todos.addListener((ListChangeListener<WrapTodo>) c -> mainCalendarController.setCenterPane("CalendarView", this));
        appointments.addListener((ListChangeListener<WrapAppointment>) c -> mainCalendarController.setCenterPane("CalendarView", this));

        mainCalendarController.datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> mainCalendarController.setCenterPane("CalendarView", this)
        );
    }

    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("日历-个人管理系统 - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("日历-个人管理系统");
        }
    }

    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ItemsListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ItemsListWrapper wrapper = (ItemsListWrapper) um.unmarshal(file);

            wrapContacts.clear();
            wrapTodos.clear();
            wrapAppointments.clear();
            wrapNotes.clear();
            wrapTodos.addAll(wrapper.getTodos());
            wrapAppointments.addAll(wrapper.getAppointments());
            wrapNotes.addAll(wrapper.getNotes());
            wrapContacts.addAll(wrapper.getContacts());


            // Save the file path to the registry.
            setPersonFilePath(file);

        } catch (Exception e) { // catches ANY exception
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ItemsListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ItemsListWrapper wrapper = new ItemsListWrapper();
            wrapper.setAppointments(wrapAppointments);
            wrapper.setContacts(wrapContacts);
            wrapper.setTodos(wrapTodos);
            wrapper.setNotes(wrapNotes);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public BorderPane getCalendarMainView() {
        return calendarMainView;
    }

    public void setCalendarMainView(BorderPane calendarMainView) {
        this.calendarMainView = calendarMainView;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Collection<PIMEntity> getEntities() {
        return entities;
    }

    public void setEntities(Collection<PIMEntity> entities) {
        this.entities = entities;
    }

    public ObservableList<WrapContact> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<WrapContact> contacts) {
        this.contacts = contacts;
    }

    public ObservableList<WrapNote> getNotes() {
        return notes;
    }

    public void setNotes(ObservableList<WrapNote> notes) {
        this.notes = notes;
    }

    public ObservableList<WrapTodo> getTodos() {
        return todos;
    }

    public void setTodos(ObservableList<WrapTodo> todos) {
        this.todos = todos;
    }

    public ObservableList<WrapAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ObservableList<WrapAppointment> appointments) {
        this.appointments = appointments;
    }

    public WrapEntity getSelectedItem() {
        return selectedItem.get();
    }

    public void setSelectedItem(WrapEntity selectedItem) {
        this.selectedItem.set(selectedItem);
    }

    public ObjectProperty<WrapEntity> selectedItemProperty() {
        return selectedItem;
    }
}
