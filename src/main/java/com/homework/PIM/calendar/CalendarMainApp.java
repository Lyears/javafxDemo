package com.homework.PIM.calendar;

import com.homework.PIM.calendar.controller.MainCalendarController;
import com.homework.PIM.calendar.controller.RootLayoutController;
import com.homework.PIM.calendar.warpper.*;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
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
     * 定义四个包装集合用于持久化
     */
    private List<WrapContact> wrapContacts = new ArrayList<>();
    private List<WrapNote> wrapNotes = new ArrayList<>();
    private List<WrapTodo> wrapTodos = new ArrayList<>();
    private List<WrapAppointment> wrapAppointments = new ArrayList<>();

    private Map<String, Object> privateInfoMap = new HashMap<>(7);
    private ObjectProperty<User> loginUser = new SimpleObjectProperty<>();

    public CalendarMainApp() {
//        loadEntities();
//        loadWrapEntities();
    }

    public static void main(String[] args) {
        launch(args);
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
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("picture/note.png")));
        primaryStage.show();

        //为loginUser添加监视器
        loginUser.addListener(
                (observable, oldValue, newValue) -> {
                    //注销则全部清空
                    if (newValue == null) {
                        contacts.clear();
                        notes.clear();
                        todos.clear();
                        appointments.clear();
                    } else {
                        //打开加载在注册表中的配置
                        if (newValue.getFilePath() != null) {
                            try {
                                loadPersonDataFromFile(new File(newValue.getFilePath()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    private void initCalendar() throws Exception {

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
        notes.addListener((ListChangeListener<WrapNote>) c -> {
            mainCalendarController.noteButton.setSelected(false);
            mainCalendarController.noteButton.setSelected(true);
        });
        contacts.addListener((ListChangeListener<WrapContact>) c -> {
            mainCalendarController.contactButton.setSelected(false);
            mainCalendarController.contactButton.setSelected(true);
        });
        todos.addListener((ListChangeListener<WrapTodo>) c -> {
            mainCalendarController.calendarButton.setSelected(false);
            mainCalendarController.calendarButton.setSelected(true);
        });
        appointments.addListener((ListChangeListener<WrapAppointment>) c -> {
            mainCalendarController.calendarButton.setSelected(false);
            mainCalendarController.calendarButton.setSelected(true);
        });

        mainCalendarController.datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> mainCalendarController.setCenterPane("CalendarView", this)
        );
    }

    /**
     * 得到保存在个人信息中的持久化文件路径
     *
     * @return 返回持久化文件，如没有则返回空
     */
    public File getPersonFilePath() {
        if (loginUser.get() == null) {
            return null;
        } else {
            Preferences prefs = Preferences.userNodeForPackage(getClass());
            if (loginUser.get().getFilePath() != null) {
                String filePath = new JSONObject(prefs.get(loginUser.get().getUserName(), null)).getString("filePath");
                if (filePath != null) {
                    return new File(filePath);
                } else {
                    return null;
                }
            } else {
                waringDialog("您还没有选择文件，请选择文件！");
                return null;
            }
        }
    }


    /**
     * 设置持久化文件路径，同时改变标题
     *
     * @param file 持久化文件
     */
    public void setPersonFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        if (file != null) {
            loginUser.get().setFilePath(file.getPath());
            JSONObject userObject = new JSONObject(loginUser.get());
            prefs.put(loginUser.get().getUserName(), userObject.toString());

            // 更新标题
            primaryStage.setTitle("日历-个人管理系统 - " + loginUser.get().getUserName() + " - " + file.getName());
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle("日历-个人管理系统");
        }
    }

    /**
     * 根据name从注册表中得到User信息
     *
     * @param name 姓名
     * @return User对象
     */
    public User getUserInfo(String name) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String userJSON = prefs.get(name, null);
        if (userJSON != null) {
            JSONObject userObject = new JSONObject(prefs.get(name, null));
            String password = userObject.getString("password");
            if (!userObject.isNull("filePath")) {
                String filePath = userObject.getString("filePath");
                if (password != null) {
                    return new User(name, password, filePath);
                } else {
                    return null;
                }
            } else {
                if (password != null) {
                    return new User(name, password);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * 设置User信息到注册表
     *
     * @param userInfo User信息
     */
    public void setUserInfo(User userInfo) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        Objects.requireNonNull(userInfo);
        JSONObject userObject = new JSONObject(userInfo);
        prefs.put(userInfo.getUserName(), userObject.toString());
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
        if (wrapper.getNotes() != null) {
            notes.addAll(wrapper.getNotes());
        }
        if (wrapper.getContacts() != null) {
            contacts.addAll(wrapper.getContacts());
        }
        if (wrapper.getTodos() != null) {
            todos.addAll(wrapper.getTodos());
        }
        if (wrapper.getAppointments() != null) {
            appointments.addAll(wrapper.getAppointments());
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
        if (loginUser.get() == null) {
            waringDialog("您还没有登录，请先登录！");
        } else {
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
    }

    /**
     * 弹出警告窗口
     */
    private void waringDialog(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        StackPane paneDialog = new StackPane();

        JFXDialog dialog = new JFXDialog();
        dialog.setDialogContainer(paneDialog);
        dialog.setOverlayClose(false);
        dialog.setContent(new Label(message));

        final Scene scene = new Scene(paneDialog, 200, 50);
        stage.setScene(scene);
        stage.setTitle("警告");
        dialog.show();
        stage.showAndWait();
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

    public Map<String, Object> getPrivateInfoMap() {
        return privateInfoMap;
    }

    public void setPrivateInfoMap(Map<String, Object> privateInfoMap) {
        this.privateInfoMap = privateInfoMap;
    }

    public User getLoginUser() {
        return loginUser.get();
    }

    public void setLoginUser(User loginUser) {
        this.loginUser.set(loginUser);
    }

    public ObjectProperty<User> loginUserProperty() {
        return loginUser;
    }
}
