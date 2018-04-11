package com.homework.PIM.calendar.controller;

import com.homework.PIM.Collection;
import com.homework.PIM.calendar.CalendarMainApp;
import com.homework.PIM.calendar.warpper.WrapContact;
import com.homework.PIM.entity.PIMContact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author fzm
 * @date 2018/4/10
 **/
public class ContactViewController {

    public TableView<WrapContact> contactTable;

    public TableColumn<WrapContact, String> firstNameColumn;
    public TableColumn<WrapContact, String> lastNameColumn;

    @FXML
    private Label priorityLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;

    private CalendarMainApp mainApp;

    private MainCalendarController mainCalendarController;

    private ObservableList<WrapContact> contacts = FXCollections.observableArrayList();

    /**
     * 初始化控制器，将每个联系人的firstName和lastName绑定到TableView上
     */
    public void initialize() {
        // TODO: 2018/4/11 添加对其方式，可以使用CSS
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showContactDetails(null);
        //添加该监视器以便于显示字段
        contactTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showContactDetails(newValue)
        );
    }

    /**
     * 该方法定义了如何展示各个联系人的详细形象
     *
     * @param contact 传入的联系人包装类
     */
    private void showContactDetails(WrapContact contact) {
        if (contact != null) {
            priorityLabel.setText(contact.getPriority());
            firstNameLabel.setText(contact.getFirstName());
            lastNameLabel.setText(contact.getLastName());
            emailLabel.setText(contact.getEmail());
        } else {
            priorityLabel.setText("");
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            emailLabel.setText("");
        }
    }

    public void editContactClick(ActionEvent event) throws Exception {
        MainCalendarController.AlertBox alertBox = mainCalendarController.new AlertBox();
        WrapContact selectedContact = contactTable.getSelectionModel().getSelectedItem();
        alertBox.display("编辑联系人", PIMContact.class, selectedContact.unWrap());
        boolean okClick = alertBox.isOkClicked();
        //如果点击提交，则将原来的属性移除，将新增的属性添加
        //如果点击关闭，则什么事情也不发生
        if (okClick){
            contacts.remove(selectedContact);
            selectedContact = new WrapContact((PIMContact) alertBox.entity);
            contacts.add(selectedContact);
            showContactDetails(selectedContact);
        }
    }

    public CalendarMainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(CalendarMainApp mainApp) {
        //加载MainAPP，将保存在Main类的Collection加载到该控制器上
        this.mainApp = mainApp;
        Collection contactCollection = mainApp.getEntities().getContact();
        for (Object pimContact : contactCollection) {
            //将实体类转化为包装类，便于数据的改动与添加及时与控制器绑定
            WrapContact contact = new WrapContact((PIMContact) pimContact);
            contacts.add(contact);
        }
        contactTable.setItems(contacts);
    }

    public MainCalendarController getMainCalendarController() {
        return mainCalendarController;
    }

    public void setMainCalendarController(MainCalendarController mainCalendarController) {
        this.mainCalendarController = mainCalendarController;
    }

    public ObservableList<WrapContact> getContacts() {
        return contacts;
    }

    public void setContacts(ObservableList<WrapContact> contacts) {
        this.contacts = contacts;
    }
}