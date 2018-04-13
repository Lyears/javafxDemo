package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * @author fzm
 * @date 2018/4/12
 **/
public class CalendarViewController {

    public Label globalDateLabel;
    public FlowPane flowPane;

    private CalendarMainApp mainApp;
    private MainCalendarController mainCalendarController;

    public void initialize(){
        flowPane.setVgap(0);
        for (int i = 1; i < 36; i++) {

            initDateGrip(String.valueOf(i));
        }

    }
    private void initDateGrip(String labelText){
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(150,109);
        pane.setMaxSize(150,109);
        pane.setMinSize(150,109);

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

        HBox hBox = new HBox();
        Label label = new Label(labelText);
        label.setFont(Font.font(14));
        hBox.getChildren().add(label);
        pane.getChildren().addAll(hBox,separator,separator1,separator2,separator3);
        AnchorPane.setLeftAnchor(hBox,10d);
        AnchorPane.setRightAnchor(hBox,10d);

        flowPane.getChildren().add(pane);
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
    }
}
