package com.homework.PIM.calendar;

import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
import com.homework.PIM.PIMManager;
import com.homework.PIM.calendar.controller.CalendarViewController;
import com.homework.PIM.calendar.controller.MainCalendarController;
import com.homework.PIM.calendar.warpper.WrapContact;
import com.homework.PIM.calendar.warpper.WrapNote;
import com.homework.PIM.entity.PIMContact;
import com.homework.PIM.entity.PIMEntity;
import com.homework.PIM.entity.PIMNote;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    /**
     * 新建一个集体参数
     */
    private Collection<PIMEntity> entities = new PIMCollection<>();

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
        rootLayout = loader.load();
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
        FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("view/CalendarView.fxml"));
        AnchorPane pane = centerLoader.load();
        CalendarViewController calendarViewController = centerLoader.getController();
        calendarViewController.setMainApp(this);
        calendarViewController.setMainCalendarController(mainCalendarController);
        calendarMainView.setCenter(pane);

        calendarViewController.globalDateLabel.textProperty().bind(mainCalendarController.stringBindingProperty());

    }

    public BorderPane getCalendarMainView() {
        return calendarMainView;
    }

    public void setCalendarMainView(BorderPane calendarMainView) {
        this.calendarMainView = calendarMainView;
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
}
