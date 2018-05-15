package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapAppointment;
import com.homework.PIM.calendar.warpper.WrapEntity;
import com.homework.PIM.calendar.warpper.WrapTodo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.DayOfWeek;
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


    private ObservableMap<Label, WrapEntity> labelSelectMap = FXCollections.observableHashMap();

    private CalendarMainApp mainApp;
    private MainCalendarController mainCalendarController;

    /**
     * 格式化输出日期
     *
     * @param i 日期数
     * @return 格式化后的日期数
     */
    private static String formatting(int i) {
        StringBuilder value = new StringBuilder();
        if (i < 10) {
            value.append("0").append(i);
        } else {
            value.append(i);
        }
        return value.toString();
    }

    public void initialize() {
        flowPane.setVgap(0);
    }

    private void loadDateGrip() {

        flowPane.getChildren().clear();
        //得到时间选择器这个月的首日
        LocalDate dayInMonth = labelDate.with(TemporalAdjusters.firstDayOfMonth());
        if (!dayInMonth.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            dayInMonth = dayInMonth.plusDays(-dayInMonth.getDayOfWeek().getValue());
        }
        int cellNumber = 35;
        Color color;
        for (int i = 0; i < cellNumber; i++) {
            if (dayInMonth.getMonthValue() != labelDate.getMonthValue()) {
                //灰色
                color = Color.valueOf("#BEBEBE");
            } else if (dayInMonth.equals(LocalDate.now())) {
                //浅蓝色
                color = Color.valueOf("84C1FF");
            } else if (dayInMonth.equals(labelDate)) {
                //红色
                color = Color.valueOf("#FF5151");
            } else {
                color = Color.WHITE;
            }
            //
            initDateGrip(dayInMonth, color);
            dayInMonth = dayInMonth.plusDays(1);
        }
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

        VBox vBox = new VBox(8);
        //设置日期编号
        Label label = new Label(formatting(labelDate.getDayOfMonth()));
        label.setFont(Font.font(14));
        label.setPrefWidth(20d);
        label.setAlignment(Pos.CENTER);
        label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(100d), null)));

        vBox.getChildren().add(label);

        //打印出当天的todo
        for (WrapTodo todo : thisMonthTodos) {
            if (todo.getTime().equals(labelDate)) {
                Label todoText = new Label("项目: " + todo.getPriority() + "," + todo.getText());
                todoText.setWrapText(true);
                //设置浅灰色背景和圆角
                todoText.setBackground(new Background(new BackgroundFill(Color.valueOf("#E8E8E8"), new CornerRadii(6d), null)));

                //
                todoText.setOnMousePressed(new MouseSelectedEvent(todoText, todo));
                labelSelectMap.put(todoText, todo);

                vBox.getChildren().add(todoText);
            }
        }
        //打印出当天的appointment
        for (WrapAppointment appointment : thisMonthAppointment) {
            if (appointment.getDate().equals(labelDate)) {
                Label appointmentText = new Label("约会: " + appointment.getPriority() + "," + appointment.getDescription());
                appointmentText.setWrapText(true);
                //设置浅蓝色背景和圆角
                appointmentText.setBackground(new Background(new BackgroundFill(Color.valueOf("#0099FF"), new CornerRadii(6d), null)));
                //
                appointmentText.setOnMousePressed(new MouseSelectedEvent(appointmentText, appointment));
                labelSelectMap.put(appointmentText, appointment);

                vBox.getChildren().add(appointmentText);
            }
        }

        pane.getChildren().addAll(vBox);
        pane.getChildren().addAll(separators);
        AnchorPane.setTopAnchor(vBox, 5d);
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
                        return todo.getTime().getMonthValue() == labelDate.getMonthValue();
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

        loadDateGrip();
        mainApp.selectedItemProperty().addListener(
                (observe, oldValue, newValue) -> labelSelectMap.forEach(
                        (k, v) -> {
                            //每当选中的项目被修改时，将其他的样式清除
                            if (!v.equals(newValue)) {
                                k.setEffect(null);
                            }
                        }
                )
        );
    }

    /**
     * 定义选中事件
     */
    private class MouseSelectedEvent implements EventHandler<MouseEvent> {
        Label label;
        WrapEntity wrapEntity;

        MouseSelectedEvent(Label label, WrapEntity wrapEntity) {
            this.label = label;
            this.wrapEntity = wrapEntity;

        }

        @Override
        public void handle(MouseEvent event) {
            //如果左键点击
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                label.setEffect(new DropShadow());
                mainApp.selectedItemProperty().set(wrapEntity);
            }
        }
    }
}
