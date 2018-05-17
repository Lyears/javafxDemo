package com.homework.PIM.calendar.controller;

import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.*;
import com.homework.PIM.entity.*;
import com.jfoenix.controls.*;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author fzm
 * @date 2018/3/31
 **/
public class MainCalendarController {

    public JFXButton newAppointment;
    public JFXButton newTodo;
    public JFXButton newNote;
    public JFXButton newContact;
    public JFXButton todayButton;
    public JFXButton lateButton;
    public JFXButton frontMonth;
    public JFXButton lateMonth;
    public JFXButton editButton;
    public JFXButton deleteButton;
    public JFXButton loginAndLogoutButton;
    public JFXButton createButton;

    public Label loginUserLabel;
    public Label loginTimeLabel;

    public Separator separatorY;
    public AnchorPane leftPane;

    public JFXToggleButton calendarButton;
    public JFXToggleButton noteButton;
    public JFXToggleButton contactButton;

    public JFXDatePicker datePicker;
    private DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private StringBinding stringBinding = null;

    private BooleanProperty isLogin = new SimpleBooleanProperty(false);
    @FXML
    private ContactViewController contactViewController;
    @FXML
    private NoteViewController noteViewController;
    @FXML
    private CalendarViewController calendarViewController;
    private CalendarMainApp mainApp;
    private UserAlertBox userAlertBox = new UserAlertBox();

    public void initialize() {
        //设置水平分割线的模糊效果
        separatorY.setEffect(new DropShadow(5, 6, 4, Color.BLACK));
        //设置时间选择器显示星期和将初始值设为当前时间
        datePicker.setValue(LocalDate.now());
        //datePicker不能为空
        ObjectProperty<LocalDate> datePickerProperty = Objects.requireNonNull(datePicker.valueProperty());
        //设置日历显示时间标签的输出格式
        stringBinding = new StringBinding() {
            {
                super.bind(datePickerProperty);
            }

            @Override
            protected String computeValue() {
                return datePickerProperty.get().getYear() + "年" + datePickerProperty.get().getMonthValue() + "月";
            }
        };
        isLogin.addListener(
                (observe, oldValue, newValue) -> {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    if (newValue.equals(true)) {
                        loginAndLogoutButton.setText("注销");
                        imageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("picture/logout.png")));
                        loginAndLogoutButton.setGraphic(imageView);

                    } else {
                        loginAndLogoutButton.setText("登录");
                        imageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream("picture/login.png")));
                        loginAndLogoutButton.setGraphic(imageView);
                    }
                }
        );

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
                new Stop(0.001, Color.valueOf("#BEBEBE")), new Stop(1.0, Color.WHITE)
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
        calendarButton.setSelected(true);


        calendarButton.setUserData("CalendarView");

        noteButton.setToggleGroup(group);
        noteButton.setUserData("NoteView");

        contactButton.setToggleGroup(group);
        contactButton.setUserData("ContactView");

        group.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setCenterPane((String) group.getSelectedToggle().getUserData(), this.mainApp);
                    }
                }
        );
    }

    /**
     * 加载中央视图
     *
     * @param fileName 视图对应的fxml文件名
     * @param mainApp  main函数
     */
    public void setCenterPane(String fileName, CalendarMainApp mainApp) {
        try {
            if (fileName != null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CalendarMainApp.class.getResource("view/" + fileName + ".fxml"));
                Region pane = loader.load();

                switch (fileName) {
                    case "ContactView": {
                        //为了保证编辑与删除按钮的正确选择，需要在加载中间场景时将其他控制类置为null
                        noteViewController = null;
                        contactViewController = loader.getController();
                        contactViewController.setMainApp(mainApp);
                        contactViewController.setMainCalendarController(this);
                        break;
                    }
                    case "NoteView": {
                        contactViewController = null;
                        noteViewController = loader.getController();
                        noteViewController.setMainApp(mainApp);
                        noteViewController.setMainCalendarController(this);
                        break;
                    }
                    case "CalendarView": {
                        noteViewController = null;
                        contactViewController = null;
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

                mainApp.getCalendarMainView().setCenter(pane);
            } else {
                mainApp.getCalendarMainView().setCenter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newTodoClick(ActionEvent event) throws Exception {
        ChangeAlertBox alertBox = new ChangeAlertBox();
        alertBox.display("新建项目", PIMTodo.class);
        boolean isOKClick = alertBox.okClicked;
        if (isOKClick) {
            mainApp.getTodos().add(new WrapTodo((PIMTodo) alertBox.entity));
        }
    }

    public void newAppointmentClick(ActionEvent event) throws Exception {
        ChangeAlertBox alertBox = new ChangeAlertBox();
        alertBox.display("新建约会", PIMAppointment.class);
        boolean isOKClick = alertBox.okClicked;
        if (isOKClick) {
            mainApp.getAppointments().add(new WrapAppointment((PIMAppointment) alertBox.entity));
        }
    }

    public void newNoteClick(ActionEvent event) throws Exception {
        ChangeAlertBox alertBox = new ChangeAlertBox();
        alertBox.display("新建笔记", PIMNote.class);
        boolean isOkClick = alertBox.okClicked;
        if (isOkClick) {
            mainApp.getNotes().add(new WrapNote((PIMNote) alertBox.entity));
        }
    }

    public void newContactClick(ActionEvent event) throws Exception {
        ChangeAlertBox alertBox = new ChangeAlertBox();
        alertBox.display("新建联系人", PIMContact.class);
        //将新建立的联系人添加到联系人集合
        boolean isOkClick = alertBox.okClicked;
        if (isOkClick) {
            mainApp.getContacts().add(new WrapContact((PIMContact) alertBox.entity));
        }
    }

    public void todayButtonClick(ActionEvent event) {
        datePicker.valueProperty().set(LocalDate.now());
    }

    public void lateButtonClick(ActionEvent event) {
        datePicker.valueProperty().set(datePicker.getValue().plusDays(7));
    }

    public void lateMonthClick(ActionEvent event) {
        datePicker.valueProperty().set(datePicker.getValue().plusMonths(1));
    }

    public void frontMonthClick(ActionEvent event) {
        datePicker.valueProperty().set(datePicker.getValue().plusMonths(-1));
    }

    /**
     * 定义编辑按钮的方法
     */
    public void editButtonClick(ActionEvent event) {

        WrapEntity selectedEntity = mainApp.getSelectedItem();
        if (selectedEntity != null) {
            if (contactViewController != null) {
                WrapContact selectedContact = contactViewController.contactTable.getSelectionModel().getSelectedItem();
                edit(selectedContact, contactViewController);
            } else if (noteViewController != null) {
                WrapNote selectedNote = noteViewController.noteTable.getSelectionModel().getSelectedItem();
                edit(selectedNote, noteViewController);
            } else {
                if (selectedEntity instanceof WrapTodo) {
                    WrapTodo wrapTodo = (WrapTodo) selectedEntity;
                    edit(wrapTodo, mainApp.getTodos());
                } else if (selectedEntity instanceof WrapAppointment) {
                    WrapAppointment wrapAppointment = (WrapAppointment) selectedEntity;
                    edit(wrapAppointment, mainApp.getAppointments());
                }
            }
        }
    }

    private void edit(WrapTodo wrapTodo, ObservableList<WrapTodo> todos) {
        try {
            ChangeAlertBox alertBox = new ChangeAlertBox();
            int index = todos.indexOf(wrapTodo);
            alertBox.display("编辑项目", PIMTodo.class, wrapTodo.unWrap());
            boolean okClick = alertBox.isOkClicked();
            if (okClick) {
                wrapTodo = new WrapTodo((PIMTodo) alertBox.entity);
                todos.set(index, wrapTodo);
            }
        } catch (Exception e) {
            // TODO: 2018/4/14
            e.printStackTrace();
        }
    }

    private void edit(WrapAppointment wrapAppointment, ObservableList<WrapAppointment> appointments) {
        try {
            ChangeAlertBox alertBox = new ChangeAlertBox();
            int index = appointments.indexOf(wrapAppointment);
            alertBox.display("编辑项目", PIMAppointment.class, wrapAppointment.unWrap());
            boolean okClick = alertBox.isOkClicked();
            if (okClick) {
                wrapAppointment = new WrapAppointment((PIMAppointment) alertBox.entity);
                appointments.set(index, wrapAppointment);
            }
        } catch (Exception e) {
            // TODO: 2018/4/14
            e.printStackTrace();
        }
    }

    private void edit(WrapContact selectedContact, ContactViewController contactViewController) {
        try {
            ChangeAlertBox alertBox = new ChangeAlertBox();
            ObservableList<WrapContact> contacts = mainApp.getContacts();
            int index = contacts.indexOf(selectedContact);
            alertBox.display("编辑联系人", PIMContact.class, selectedContact.unWrap());
            boolean okClick = alertBox.isOkClicked();
            //如果点击提交，则将原来的属性移除，将新增的属性添加
            //如果点击关闭，则什么事情也不发生
            if (okClick) {
                selectedContact = new WrapContact((PIMContact) alertBox.entity);
                contacts.set(index, selectedContact);
                contactViewController.contactTable.getSelectionModel().select(selectedContact);
                contactViewController.showContactDetails(selectedContact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void edit(WrapNote selectedNote, NoteViewController noteViewController) {
        try {
            ChangeAlertBox alertBox = new ChangeAlertBox();
            ObservableList<WrapNote> notes = mainApp.getNotes();
            int index = notes.indexOf(selectedNote);
            alertBox.display("编辑笔记", PIMNote.class, selectedNote.unWrap());
            boolean okClick = alertBox.isOkClicked();
            //如果点击提交，则将原来的属性移除，将新增的属性添加
            //如果点击关闭，则什么事情也不发生
            if (okClick) {
                selectedNote = new WrapNote((PIMNote) alertBox.entity);
                notes.set(index, selectedNote);
                noteViewController.noteTable.getSelectionModel().select(selectedNote);
                noteViewController.showNoteDetails(selectedNote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonClick(ActionEvent event) {
        WrapEntity selectedEntity = mainApp.getSelectedItem();
        if (selectedEntity != null) {
            if (noteViewController != null) {
                delete(noteViewController);
            } else if (contactViewController != null) {
                delete(contactViewController);
            } else {
                if (selectedEntity instanceof WrapTodo) {
                    WrapTodo selectedTodo = (WrapTodo) selectedEntity;
                    delete(selectedTodo, mainApp.getTodos());
                } else if (selectedEntity instanceof WrapAppointment) {
                    WrapAppointment selectedAppointment = (WrapAppointment) selectedEntity;
                    delete(selectedAppointment, mainApp.getAppointments());
                }
            }
        }

    }

    private void delete(WrapTodo selectedTodo, ObservableList<WrapTodo> todos) {
        todos.remove(selectedTodo);
    }

    private void delete(WrapAppointment selectedAppointment, ObservableList<WrapAppointment> appointments) {
        appointments.remove(selectedAppointment);
    }

    private void delete(ContactViewController contactViewController) {
        int selectedIndex = contactViewController.contactTable.getSelectionModel().getSelectedIndex();
        contactViewController.contactTable.getItems().remove(selectedIndex);
    }

    private void delete(NoteViewController noteViewController) {
        int selectedIndex = noteViewController.noteTable.getSelectionModel().getSelectedIndex();
        noteViewController.noteTable.getItems().remove(selectedIndex);
    }

    public void loginAndLogOutButtonClick(ActionEvent event) {
        userAlertBox.loginAndLogout();
    }

    public void createUserButtonClick(ActionEvent event) {
        userAlertBox.createUser();
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

    public class ChangeAlertBox {

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

            Stage window = new Stage();
            window.setTitle(title);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinHeight(300);
            window.setMaxHeight(300);
            window.setMinWidth(450);
            window.setMaxWidth(450);

            JFXButton closeButton = new JFXButton("关闭");
            closeButton.getStyleClass().add("button-raised");
            closeButton.setPrefSize(45d, 30d);
            closeButton.setMaxSize(45d, 30d);
            closeButton.setMinSize(45d, 30d);
            closeButton.setCancelButton(true);
            JFXButton submitButton = new JFXButton("提交");
            submitButton.getStyleClass().add("button-raised");
            submitButton.setPrefSize(45d, 30d);
            submitButton.setMaxSize(45d, 30d);
            submitButton.setMinSize(45d, 30d);
            submitButton.setDefaultButton(true);

            closeButton.setOnAction(event -> window.close());

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

                    window.close();

                    okClicked = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            VBox vBox = new VBox(15);

            vBox.setAlignment(Pos.CENTER);

            JFXTextField[] textFields = new JFXTextField[clazz.getDeclaredFields().length];

            Class realClazz = null;
            Field realText;
            if (o != null) {
                realClazz = o.getClass();
            }
            int i = 0;
            for (Field text : clazz.getDeclaredFields()) {
                Label label = new Label(text.getName() + ": ");
                label.getStyleClass().add("fx-label");
                textFields[i] = new JFXTextField();
                //设置textFiled的主题颜色
                textFields[i].getStyleClass().add("jfx-text-field");
                //如果传入的对象不为空，将文本框内的值预设为传入的对象对应的值
                if (o != null) {
                    realText = realClazz.getDeclaredFields()[i];
                    realText.setAccessible(true);
                    if (realText.getType().equals(String.class)) {
                        textFields[i].setText((String) realText.get(o));
                    } else if (realText.getType().equals(LocalDate.class)) {
                        textFields[i].setText(((LocalDate) realText.get(o)).format(mdy));
                    }
                }
                textFields[i].setPromptText(text.getName());
                stringProperties[i] = new SimpleStringProperty();
                stringProperties[i].bind(textFields[i].textProperty());

                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().addAll(label, textFields[i]);
                AnchorPane.setLeftAnchor(label, 100d);
                AnchorPane.setRightAnchor(textFields[i], 100d);

                vBox.getChildren().add(anchorPane);
                i = i + 1;
            }


            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(submitButton, closeButton);
            vBox.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/style.css")).toExternalForm());
            vBox.getChildren().add(hBox);

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.showAndWait();

        }

    }

    private class UserAlertBox {
        private boolean okClick = false;

        private void loginAndLogout() {
            if (isLogin.get()) {
                //
                mainApp.setLoginUser(null);
                loginUserLabel.setText("当前登录用户: ");
                loginTimeLabel.setText("本次登录时间: ");
                isLogin.set(false);
            } else {
                if (login()) {
                    isLogin.set(true);
                }
            }
        }

        private void createUser() {
            create();
        }

        private boolean login() {
            boolean validation = false;
            okClick = false;
            Stage window = new Stage();
            window.setTitle("登录");
            LoginDisplay();
            if (okClick){
                validation = true;
            }

            return validation;
        }

        private void create() {


            User user = new User();
            okClick = false;
            CreateDisplay(user);
            if (okClick){
                mainApp.setUserInfo(user);
            }

        }

        private void LoginDisplay(){
            Stage window = new Stage();
            window.setTitle("登录");
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinHeight(300);
            window.setMaxHeight(300);
            window.setMinWidth(450);
            window.setMaxWidth(450);

            JFXButton closeButton = new JFXButton("关闭");
            closeButton.getStyleClass().add("button-raised");
            closeButton.setPrefSize(45d, 30d);
            closeButton.setMaxSize(45d, 30d);
            closeButton.setMinSize(45d, 30d);
            closeButton.setCancelButton(true);
            JFXButton submitButton = new JFXButton("提交");
            submitButton.getStyleClass().add("button-raised");
            submitButton.setPrefSize(45d, 30d);
            submitButton.setMaxSize(45d, 30d);
            submitButton.setMinSize(45d, 30d);
            submitButton.setDefaultButton(true);


            VBox vBox = new VBox(15);
            vBox.setAlignment(Pos.CENTER);

            vBox.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/style.css")).toExternalForm());

            Label userLabel = new Label("用户名: ");
            userLabel.getStyleClass().add("fx-label");
            JFXTextField userNameField = new JFXTextField();
            userNameField.setFocusColor(Color.valueOf("#0099ff"));

            userNameField.setPromptText("请输入用户名");
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().addAll(userLabel, userNameField);
            AnchorPane.setLeftAnchor(userLabel, 100d);
            AnchorPane.setRightAnchor(userNameField, 100d);
            vBox.getChildren().add(anchorPane);

            Label passwordLabel = new Label("密码: ");
            passwordLabel.getStyleClass().add("fx-label");
            JFXPasswordField passwordField = new JFXPasswordField();
            passwordField.setFocusColor(Color.valueOf("#0099ff"));


            passwordField.setPromptText("请输入密码");
            anchorPane = new AnchorPane();
            anchorPane.getChildren().addAll(passwordLabel, passwordField);
            AnchorPane.setLeftAnchor(passwordLabel, 100d);
            AnchorPane.setRightAnchor(passwordField, 100d);
            vBox.getChildren().add(anchorPane);


            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(submitButton, closeButton);
            vBox.getChildren().add(hBox);

            closeButton.setOnAction(event -> window.close());
            submitButton.setOnAction(event -> {
                User existUser = mainApp.getUserInfo(userNameField.getText());
                if (existUser != null){
                    if (existUser.getPassword().equals(passwordField.getText())){
                        okClick = true;
                        loginUserLabel.setText("当前登录用户: " + userNameField.getText());
                        loginTimeLabel.setText("本次登录时间: " + LocalTime.now().withNano(0).toString());
                        mainApp.setLoginUser(existUser);
                        window.close();
                    }else {
                        // TODO: 2018/5/17 fail to login
                    }
                }else {
                    // TODO: 2018/5/17 no user
                }
            });
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.showAndWait();
        }

        private void CreateDisplay(User user) {
            Stage window = new Stage();
            window.setTitle("注册");
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinHeight(300);
            window.setMaxHeight(300);
            window.setMinWidth(450);
            window.setMaxWidth(450);

            JFXButton closeButton = new JFXButton("关闭");
            closeButton.getStyleClass().add("button-raised");
            closeButton.setPrefSize(45d, 30d);
            closeButton.setMaxSize(45d, 30d);
            closeButton.setMinSize(45d, 30d);
            closeButton.setCancelButton(true);
            JFXButton submitButton = new JFXButton("提交");
            submitButton.getStyleClass().add("button-raised");
            submitButton.setPrefSize(45d, 30d);
            submitButton.setMaxSize(45d, 30d);
            submitButton.setMinSize(45d, 30d);
            submitButton.setDefaultButton(true);


            VBox vBox = new VBox(15);
            vBox.setAlignment(Pos.CENTER);

            vBox.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/style.css")).toExternalForm());

            Label userLabel = new Label("用户名: ");
            userLabel.getStyleClass().add("fx-label");
            JFXTextField userNameField = new JFXTextField();
            userNameField.setFocusColor(Color.valueOf("#0099ff"));

            userNameField.setPromptText("请输入用户名");
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().addAll(userLabel, userNameField);
            AnchorPane.setLeftAnchor(userLabel, 100d);
            AnchorPane.setRightAnchor(userNameField, 100d);
            vBox.getChildren().add(anchorPane);

            Label passwordLabel = new Label("密码: ");
            passwordLabel.getStyleClass().add("fx-label");
            JFXPasswordField passwordField = new JFXPasswordField();
            passwordField.setFocusColor(Color.valueOf("#0099ff"));


            passwordField.setPromptText("请输入密码");
            anchorPane = new AnchorPane();
            anchorPane.getChildren().addAll(passwordLabel, passwordField);
            AnchorPane.setLeftAnchor(passwordLabel, 100d);
            AnchorPane.setRightAnchor(passwordField, 100d);
            vBox.getChildren().add(anchorPane);


            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(submitButton, closeButton);
            vBox.getChildren().add(hBox);

            closeButton.setOnAction(event -> window.close());
            submitButton.setOnAction(
                    event -> {
                            if (mainApp.getUserInfo(userNameField.getText()) == null) {
                                user.setUserName(userNameField.getText());
                                user.setPassword(passwordField.getText());
                                okClick = true;
                                window.close();
                            } else {
                                // TODO: 2018/5/16  fail to create
                            }
                    }
            );
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.showAndWait();
        }
    }


}
