package com.homework.PIM.calendar;

import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
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
import java.io.FileWriter;
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
    //定义四个包装集合用于持久化
    private List<WrapContact> wrapContacts = new ArrayList<>();
    private List<WrapNote> wrapNotes = new ArrayList<>();
    private List<WrapTodo> wrapTodos = new ArrayList<>();
    private List<WrapAppointment> wrapAppointments = new ArrayList<>();

    public CalendarMainApp() {
        loadEntities();
//        loadWrapEntities();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadEntities() {
        Collection contactCollection = entities.getContact();
        //将实体类转化为包装类，便于数据的改动与添加及时与控制器绑定
        for (Object pimContact : contactCollection) {
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
    }

    /**
     * 加载包装集合用于持久化项目
     */
    private void loadWrapEntities() {
        //在保存之前先清空持久化包装类，防止重复保存
        wrapAppointments.clear();
        wrapContacts.clear();
        wrapNotes.clear();
        wrapTodos.clear();

        wrapContacts.addAll(contacts);
        wrapNotes.addAll(notes);
        wrapTodos.addAll(todos);
        wrapAppointments.addAll(appointments);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initRootLayout();
        initCalendar();

    }

    private void initRootLayout() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
        this.rootLayout = loader.load();
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setMainApp(this);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        //首先打开加载在注册表中的配置
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

    private void initCalendar() throws Exception {

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

            // 更新标题
            primaryStage.setTitle("日历-个人管理系统 - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("日历-个人管理系统");
        }
    }

    /**
     * 加载持久化文件
     *
     * @param file 选择加载的文件
     */
    public void loadPersonDataFromFile(File file) throws Exception {
        JAXBContext context = JAXBContext
                .newInstance(ItemsListWrapper.class);
        Unmarshaller um = context.createUnmarshaller();

        //读取xml文件并解为对象
        ItemsListWrapper wrapper = (ItemsListWrapper) um.unmarshal(file);

        contacts.clear();
        todos.clear();
        appointments.clear();
        notes.clear();

        //检测每个包装集合是否为空，不为空则将其加载进入集合
        if (wrapper.getTodos() != null) {
            todos.addAll(wrapper.getTodos());
        }
        if (wrapper.getAppointments() != null) {
            appointments.addAll(wrapper.getAppointments());
        }
        if (wrapper.getNotes() != null) {
            notes.addAll(wrapper.getNotes());
        }
        if (wrapper.getContacts() != null) {
            contacts.addAll(wrapper.getContacts());
        }
        // 保存文件配置进入注册表
        setPersonFilePath(file);
    }


    /**
     * 保存持久化文件
     *
     * @param file 选择文件
     */
    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ItemsListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //加载包装集合，用于写入数据
            loadWrapEntities();

            ItemsListWrapper wrapper = new ItemsListWrapper();
            wrapper.setAppointments(wrapAppointments);
            wrapper.setContacts(wrapContacts);
            wrapper.setTodos(wrapTodos);
            wrapper.setNotes(wrapNotes);

            //在保存文件之前先清空数据
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();

            // 将对象编为xml文件
            m.marshal(wrapper, file);

            // 保存文件配置进入注册表
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
