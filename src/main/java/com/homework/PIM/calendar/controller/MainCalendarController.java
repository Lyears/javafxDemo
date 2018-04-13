package com.homework.PIM.calendar.controller;

import com.homework.PIM.Collection;
import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapContact;
import com.homework.PIM.calendar.warpper.WrapNote;
import com.homework.PIM.entity.*;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
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
import java.time.format.DateTimeFormatter;

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

    public DatePicker datePicker;
    public Accordion accordion;
    private DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private StringBinding stringBinding = null;
    private ContactViewController contactViewController = null;
    private NoteViewController noteViewController = null;
    private CalendarViewController calendarViewController = null;
    private CalendarMainApp mainApp;
//    private Collection<PIMEntity> entities = mainApp.getEntities();

    public void initialize() {
        //设置水平分割线的模糊效果
        separatorY.setEffect(new DropShadow(5, 6, 4, Color.BLACK));
        //设置时间选择器显示星期和将初始值设为当前时间
        datePicker.setShowWeekNumbers(true);
        datePicker.setValue(LocalDate.now());
        ObjectProperty<LocalDate> localDateProperty = datePicker.valueProperty();
        //设置日历显示时间标签的输出格式
        stringBinding = new StringBinding() {
            {
                super.bind(localDateProperty);
            }

            @Override
            protected String computeValue() {
                return localDateProperty.get().getYear() + "年" + localDateProperty.get().getMonthValue() + "月";
            }
        };
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
        calendarButton.setSelected(true);

        noteButton.setToggleGroup(group);
        noteButton.setUserData("NoteView");

        contactButton.setToggleGroup(group);
        contactButton.setUserData("ContactView");

        group.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setCenterPane((String) group.getSelectedToggle().getUserData());
                    }
                }
        );
    }

    private void setCenterPane(String fileName) {
        try {
            if (fileName != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CalendarMainApp.class.getResource("view/" + fileName + ".fxml"));
                Region pane = loader.load();

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
                        noteViewController.setMainCalendarController(this);
                        break;
                    }
                    case "CalendarView": {
                        calendarViewController = loader.getController();
                        calendarViewController.setMainApp(mainApp);
                        calendarViewController.setMainCalendarController(this);
                        //将时间选择器上的日期与日历页面上的标签绑定
                        calendarViewController.globalDateLabel.textProperty().bind(stringBinding);
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
        boolean isOkClick = alertBox.okClicked;
        if (isOkClick) {
            mainApp.getNotes().add(new WrapNote((PIMNote) alertBox.entity));
        }
    }

    public void newContactClick(ActionEvent event) throws Exception {
        AlertBox alertBox = new AlertBox();
        alertBox.display("新建联系人", PIMContact.class);
        //将新建立的联系人添加到联系人集合
        boolean isOkClick = alertBox.okClicked;
        if (isOkClick) {
            mainApp.getContacts().add(new WrapContact((PIMContact) alertBox.entity));
        }
    }


    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        this.mainApp = mainApp;
    }

    public String getStringBinding() {
        return stringBinding.get();
    }

    public StringBinding stringBindingProperty() {
        return stringBinding;
    }

    public class AlertBox {

        PIMEntity entity = null;
        private boolean okClicked = false;
        private StringProperty[] stringProperties = new SimpleStringProperty[5];

        public boolean isOkClicked() {
            return okClicked;
        }

        /**
         * 设置新建项目的弹出框，不需要传入待编辑对象
         */
        void display(String title, Class clazz) throws Exception {
            display(title, clazz, null);
        }

        /**
         * 设置编辑项目的弹出框
         *
         * @param title 弹出框标题
         * @param clazz 设置的项目类型
         * @param o     设置传入的待编辑对象
         */
        void display(String title, Class clazz, Object o) throws Exception {
            Collection<PIMEntity> entities = mainApp.getEntities();

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
            //判断是否为新建项目弹出框
            isNewOperation = o == null;
            //提交按钮产生事件
            submitButton.setOnAction(event -> {
                try {
                    entity = (PIMEntity) clazz.newInstance();
                    int i = 0;
                    //实例化传入的对象，并为声明的属性赋值
                    for (Field text : clazz.getDeclaredFields()) {
                        text.setAccessible(true);
                        if (text.getType().equals(String.class)) {
                            text.set(entity, stringProperties[i].getValue());
                        } else if (text.getType().equals(LocalDate.class)) {
                            text.set(entity, LocalDate.parse(stringProperties[i].getValue(), mdy));
                        }
                        i++;

                    }

                    // TODO: 2018/4/10 重构

                    window.close();

                    okClicked = true;
                    //若是新建项目，将产生的对象传入集合,否则将编辑后的对象替换
                    if (isNewOperation) {
                        entities.add(entity);
                    } else {
                        entities.set(entities.indexOf(o), entity);
                    }
                    System.out.println(entities);
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
                //如果传入的对象不为空，将文本框内的值预设为传入的对象对应的值
                if (o != null) {
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
