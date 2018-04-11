package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapContact;
import com.homework.PIM.calendar.warpper.WrapNote;
import com.homework.PIM.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/3/31
 **/
public class MainCalendarController {

    public Button newAppointment;
    public Button newTodo;
    public Button newNote;
    public Button newContact;

    public Separator separatorY;
    public AnchorPane leftPane;

    public ToggleButton calendarButton;
    public ToggleButton noteButton;
    public ToggleButton contactButton;

    public Accordion accordion;

    private ContactViewController contactViewController = null;
    private NoteViewController noteViewController = null;
    private CalendarMainApp mainApp;
//    private Collection<PIMEntity> entities = mainApp.getEntities();

    public void initialize() {
        //设置水平分割线的模糊效果
        separatorY.setEffect(new DropShadow(5, 6, 4, Color.BLACK));
        setLeftPaneLinearGradient();

        setToggleButtonGroup();
    }

    /**
     * 设置左边栏的背景
     */
    public void setLeftPaneLinearGradient() {
        LinearGradient linearGrad = new LinearGradient(
                0, 1, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0.001, Color.GRAY), new Stop(1.0, Color.WHITE)
        );
        BackgroundFill backgroundFill = new BackgroundFill(linearGrad, new CornerRadii(0.8), null);
        Background background = new Background(backgroundFill);

        this.leftPane.setBackground(background);
    }

    /**
     * 设置左边栏按钮的分组与事件
     */
    public void setToggleButtonGroup() {
        ToggleGroup group = new ToggleGroup();
        calendarButton.setToggleGroup(group);
        calendarButton.setUserData("CalendarView");

        noteButton.setToggleGroup(group);
        noteButton.setUserData("NoteView");

        contactButton.setToggleGroup(group);
        contactButton.setUserData("ContactView");

        group.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> setCenterPane((String) group.getSelectedToggle().getUserData())
        );
    }

    private void setCenterPane(String fileName) {
        try {
            if (fileName != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CalendarMainApp.class.getResource("view/" + fileName + ".fxml"));
                SplitPane pane = loader.load();
                switch (fileName) {
                    case "ContactView": {
                        contactViewController = loader.getController();
                        contactViewController.setMainApp(mainApp);
                        contactViewController.setMainCalendarController(this);
                        break;
                    }
                    case "NoteView": {
                        noteViewController = loader.getController();
                        noteViewController.setMainApp(mainApp);
                        break;
                    }
                    default: {
                    }
                }

                this.mainApp.getCalendarMainView().setCenter(pane);
            } else {
                this.mainApp.getCalendarMainView().setCenter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void newTodoClick(ActionEvent event) throws Exception {
        new AlertBox().display("新建项目", PIMTodo.class);
    }

    public void newAppointmentClick(ActionEvent event) throws Exception {
        new AlertBox().display("新建约会", PIMAppointment.class);
    }

    public void newNoteClick(ActionEvent event) throws Exception {
        AlertBox alertBox = new AlertBox();
        alertBox.display("新建笔记", PIMNote.class);
        noteViewController.getNotes().add(new WrapNote((PIMNote) alertBox.entity));
    }

    public void newContactClick(ActionEvent event) throws Exception {
        AlertBox alertBox = new AlertBox();
        alertBox.display("新建联系人", PIMContact.class);
        //将新建立的联系人添加到联系人集合
        contactViewController.getContacts().add(new WrapContact((PIMContact) alertBox.entity));
    }


    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;
    }

    public class AlertBox {

        PIMEntity entity = null;
        private boolean okClicked = false;
        private StringProperty[] stringProperties = new SimpleStringProperty[5];

        public boolean isOkClicked() {
            return okClicked;
        }

        void display(String title, Class clazz) throws Exception {
            display(title, clazz, null);
        }

        /**
         * 设置编辑项目的弹出框
         * @param title 弹出框标题
         * @param clazz 设置的项目类型
         */
        void display(String title, Class clazz, Object o) throws Exception {


            Stage window = new Stage();
            window.setTitle(title);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinHeight(300);
            window.setMaxHeight(300);
            window.setMinWidth(450);
            window.setMaxWidth(450);

            Button closeButton = new Button("关闭");
            Button submitButton = new Button("提交");

            closeButton.setOnAction(event -> window.close());

            boolean isNewOperation;
            isNewOperation = o == null;
            //提交按钮产生事件
            submitButton.setOnAction(event -> {
                try {
                    entity = (PIMEntity) clazz.newInstance();
                    int i = 0;
                    //实例化每个传入的对象，并为声明的属性赋值
                    for (Field text : clazz.getDeclaredFields()) {
                        text.setAccessible(true);
                        if (text.getType().equals(String.class)) {
                            text.set(entity, stringProperties[i].getValue());
                        } else {
                            text.set(entity, LocalDate.parse(stringProperties[i].getValue()));
                        }
                        i++;

                    }
                    // TODO: 2018/4/10 重构

                    window.close();
                    okClicked = true;
                    if (isNewOperation) {
                        mainApp.getEntities().add(entity);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            VBox vBox = new VBox(15);

            vBox.setAlignment(Pos.CENTER);


            TextField[] textFields = new TextField[clazz.getDeclaredFields().length];

            Class realClazz = null;
            Field realText;
            if (o != null) {
                realClazz = o.getClass();
            }
            int i = 0;
            for (Field text : clazz.getDeclaredFields()) {
                Label label = new Label(text.getName() + ": ");
                textFields[i] = new TextField();
                if(o!=null) {
                    realText = realClazz.getDeclaredFields()[i];
                    realText.setAccessible(true);
                    textFields[i].setText((String) realText.get(o));
                }
                textFields[i].setPromptText(text.getName());
                stringProperties[i] = new SimpleStringProperty();
                stringProperties[i].bind(textFields[i].textProperty());

                //

                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().addAll(label, textFields[i]);
                AnchorPane.setLeftAnchor(label, 60d);
                AnchorPane.setRightAnchor(textFields[i], 60d);

                vBox.getChildren().add(anchorPane);
                i = i + 1;
            }


            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(submitButton, closeButton);
            vBox.getChildren().add(hBox);

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.showAndWait();

        }

    }


}
