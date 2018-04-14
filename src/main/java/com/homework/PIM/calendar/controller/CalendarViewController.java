package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapAppointment;
import com.homework.PIM.calendar.warpper.WrapTodo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @author fzm
 * @date 2018/4/12
 **/
public class CalendarViewController {

    public Label globalDateLabel;
    public FlowPane flowPane;

    private ObjectProperty<LocalDate> localDateProperty = new SimpleObjectProperty<>();

    private LocalDate labelDate;

    private ObservableList<WrapTodo> thisMonthTodos;
    private ObservableList<WrapAppointment> thisMonthAppointment;
    private CalendarMainApp mainApp;
    private MainCalendarController mainCalendarController;

    public void initialize() {
        flowPane.setVgap(0);
    }

    private void initDateGrip(LocalDate labelDate) {
        initDateGrip(labelDate, Color.BLACK);
    }

    private void initDateGrip(LocalDate labelDate, Color color) {

        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(150, 109);
        pane.setMaxSize(150, 109);
        pane.setMinSize(150, 109);

        //设置每个日期的分割线
        Separator separator = new Separator();
        separator.setPrefWidth(150);
        separator.setMaxHeight(1);
        separator.setMinHeight(1);
        Separator separator1 = new Separator(Orientation.VERTICAL);
        separator1.setPrefHeight(109);
        separator1.setMaxWidth(1);
        separator1.setMinWidth(1);
        Separator separator2 = new Separator();
        separator2.setPrefWidth(150);
        separator2.setMaxHeight(1);
        separator2.setMinHeight(1);
        separator2.setLayoutY(109);
        Separator separator3 = new Separator(Orientation.VERTICAL);
        separator3.setPrefHeight(109);
        separator3.setMaxWidth(1);
        separator3.setMinWidth(1);
        separator3.setLayoutX(149);
        ObservableList<Node> separators = FXCollections.observableArrayList();
        separators.addAll(separator, separator1, separator2, separator3);

        VBox vBox = new VBox(10);
        //设置日期编号
        Label label = new Label(String.valueOf(labelDate.getDayOfMonth()));
        label.setFont(Font.font(14));
        label.setTextFill(color);
        vBox.getChildren().add(label);
        //打印出当天的todo
        for (WrapTodo todo : thisMonthTodos) {
            if (todo.getDate().equals(labelDate)) {
                Label todoText = new Label("项目: " + todo.getPriority() + "," + todo.getText());
                todoText.setWrapText(true);
                //设置浅灰色背景
                todoText.setBackground(new Background(new BackgroundFill(Color.valueOf("#E8E8E8"), null, null)));
                todoText.setOnMousePressed(
                        (event)->{
                            if (event.getButton().equals(MouseButton.PRIMARY)){
                                System.out.println(todo);
                            }
                        }
                );
                vBox.getChildren().add(todoText);
            }
        }
        //打印出当天的appointment
        for (WrapAppointment appointment : thisMonthAppointment) {
            if (appointment.getDate().equals(labelDate)) {
                Label appointmentText = new Label("约会: " + appointment.getPriority() + "," + appointment.getDescription());
                appointmentText.setWrapText(true);
                //设置浅蓝色背景
                appointmentText.setBackground(new Background(new BackgroundFill(Color.valueOf("0099FF"), null, null)));
                vBox.getChildren().add(appointmentText);
            }
        }

        pane.getChildren().addAll(vBox);
        pane.getChildren().addAll(separators);
        AnchorPane.setLeftAnchor(vBox, 10d);
        AnchorPane.setRightAnchor(vBox, 10d);

        flowPane.getChildren().add(pane);
    }


    public LocalDate getLocalDateProperty() {
        return localDateProperty.get();
    }

    public void setLocalDateProperty(LocalDate localDateProperty) {
        this.localDateProperty.set(localDateProperty);
    }

    public ObjectProperty<LocalDate> localDatePropertyProperty() {
        return localDateProperty;
    }

    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;


    }

    public MainCalendarController getMainCalendarController() {
        return mainCalendarController;
    }

    public void setMainCalendarController(MainCalendarController mainCalendarController) {
        this.mainCalendarController = mainCalendarController;

        localDateProperty.bind(mainCalendarController.datePicker.valueProperty());

        labelDate = localDateProperty.get();

        thisMonthTodos = mainApp.getTodos().filtered(
                (WrapTodo todo) -> {
                    try {
                        return todo.getDate().getMonthValue() == labelDate.getMonthValue();
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        thisMonthAppointment = mainApp.getAppointments().filtered(
                (WrapAppointment appointment) -> {
                    try {
                        return appointment.getDate().getMonthValue() == labelDate.getMonthValue();
                    } catch (Exception e) {
                        return false;
                    }
                }
        );

        //得到时间选择器这个月的首日
        LocalDate dayInMonth = labelDate.with(TemporalAdjusters.firstDayOfMonth());

        int cellNumber = 35;
        Color color;
        for (int i = 0; i < cellNumber; i++) {
            if (dayInMonth.getMonthValue() != labelDate.getMonthValue()) {
                color = Color.GRAY;
            } else if (dayInMonth.equals(LocalDate.now())) {
                color = Color.BLUE;
            } else if (dayInMonth.equals(labelDate)) {
                color = Color.RED;
            } else {
                color = Color.BLACK;
            }
            initDateGrip(dayInMonth, color);
            dayInMonth = dayInMonth.plusDays(1);

        }
    }
}
