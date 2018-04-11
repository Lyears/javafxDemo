package com.homework.PIM.calendar;

import com.homework.PIM.Collection;
import com.homework.PIM.PIMCollection;
import com.homework.PIM.PIMManager;
import com.homework.PIM.calendar.controller.MainCalendarController;
import com.homework.PIM.entity.PIMEntity;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    /**
     * 新建一个集体参数
     */
    private Collection<PIMEntity> entities = new PIMCollection<>();

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
        //在初始化Calendar页面时加载持久化对象
        this.entities.addAll(PIMManager.load());
        // TODO: 2018/4/10 重构持久化加载方式 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainCalendar.fxml"));
        this.calendarMainView = loader.load();
        MainCalendarController controller = loader.getController();
        controller.setMainApp(this);

        rootLayout.setCenter(calendarMainView);
        primaryStage.setTitle("日历-个人管理系统");
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
}
