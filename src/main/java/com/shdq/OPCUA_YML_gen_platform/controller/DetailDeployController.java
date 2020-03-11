package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.model.TransportData;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.ThemeUtil;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shdq-fjy
 */
public class DetailDeployController {

    private MainApp mainApp;
    OpcUaProperties properties;
    Map<String,String> plcDetailProperties;
    @FXML
    private Button save;
    @FXML
    private Button next;
    @FXML
    private Button complete;
    @FXML
    private TextField address;
    @FXML
    private TextField plcNo;
    @FXML
    private TextField ns;
    @FXML
    private ChoiceBox securityMode;
    @FXML
    private ChoiceBox userAuthenticationMode;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField clientListener;
    @FXML
    private TextField sessionTimeOut;

    private ToggleGroup isConnect;
    @FXML
    private RadioButton connect;
    @FXML
    private RadioButton unconnect;

    private ToggleGroup isSubscribe;
    @FXML
    private RadioButton subscribe;
    @FXML
    private RadioButton unsubscribe;

    private boolean noError = true;

    Alert alert = new Alert(Alert.AlertType.WARNING);
    private boolean doubleClick = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize(){
        address.setTooltip(new Tooltip("客户端连接服务端地址，支持opc和http。"));
        plcNo.setTooltip(new Tooltip("正整数，并且不能重复。"));
        ns.setTooltip(new Tooltip("根据opc服务端配置。"));
        securityMode.setTooltip(new Tooltip("安全模式，根据服务端来进行相应配置。"));
        userAuthenticationMode.setTooltip(new Tooltip("用户认证模式，根据服务端来进行相应配置，username模式下，需要配置用户名和密码以便连接服务端时使用。"));
        username.setTooltip(new Tooltip("连接服务端的用户名。"));
        password.setTooltip(new Tooltip("连接服务端的密码。"));
        clientListener.setTooltip(new Tooltip("客户端的监听器，监听当前客户端的所有输入输出，系统默认：com.opc.uaclient.uaclientlistener.MyUaClientListener。"));
        sessionTimeOut.setTooltip(new Tooltip("会话过期时间。"));
        plcDetailProperties = new HashMap<>(20);
        securityMode.setItems(FXCollections.observableArrayList("none",new Separator(),"sign",new Separator(),"sign&encrypt"));
        securityMode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> plcDetailProperties.put("securityMode",String.valueOf(newValue)));
        userAuthenticationMode.setItems(FXCollections.observableArrayList("anonymous",new Separator(),"username"));
        userAuthenticationMode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String userAuthenticationMode = String.valueOf(newValue);
            plcDetailProperties.put("userAuthenticationMode",userAuthenticationMode);
            if (userAuthenticationMode.equalsIgnoreCase("username")){
                username.setDisable(false);
                password.setDisable(false);
            }
            if (userAuthenticationMode.equalsIgnoreCase("anonymous")){
                username.setDisable(true);
                password.setDisable(true);
            }
        });
        isConnect = new ToggleGroup();
        isSubscribe = new ToggleGroup();
        connect.setUserData(true);
        unconnect.setUserData(false);
        subscribe.setUserData(true);
        unsubscribe.setUserData(false);
        connect.setToggleGroup(isConnect);
        unconnect.setToggleGroup(isConnect);
        isConnect.selectedToggleProperty().addListener((observable, oldValue, newValue) -> plcDetailProperties.put("isConnect",isConnect.getSelectedToggle().getUserData().toString()));
        subscribe.setToggleGroup(isSubscribe);
        unsubscribe.setToggleGroup(isSubscribe);
        isSubscribe.selectedToggleProperty().addListener((observable, oldValue, newValue) -> plcDetailProperties.put("isSubscribe",isSubscribe.getSelectedToggle().getUserData().toString()));
        save.setTooltip(new Tooltip("保存当前PLC配置"));
        next.setTooltip(new Tooltip("继续添加下一个PLC的配置"));
        complete.setTooltip(new Tooltip("进入创建配置文件界面"));
    }

    private void validateInput(){
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (StringUtils.isBlank(address.getText())){
            noError = false;
            alert.setContentText(WrongMsg.address_MUST_NOT_NULL);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("address",address.getText());
        }
        if (!WrongMsg.pattern.matcher(plcNo.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.plcNo_MUST_BE_INTEGER);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("plcNo",plcNo.getText());
        }
        if (!WrongMsg.pattern.matcher(ns.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.ns_MUST_BE_INTEGER);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("ns", ns.getText());
        }
        if (StringUtils.isBlank(clientListener.getText())){
            noError = false;
            alert.setContentText(WrongMsg.clientListener_MUST_NOT_NULL);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("clientListener", clientListener.getText());
        }
        if (!WrongMsg.pattern.matcher(sessionTimeOut.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.sessionTimeOut_MUST_BE_INTEGER);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("sessionTimeOut",sessionTimeOut.getText());
        }
        if (plcDetailProperties.get("securityMode") == null){
            plcDetailProperties.put("securityMode","none");
        }
        if (plcDetailProperties.get("userAuthenticationMode") == null){
            plcDetailProperties.put("userAuthenticationMode","anonymous");
        }
        if (plcDetailProperties.get("isConnect") == null){
            plcDetailProperties.put("isConnect","true");
        }
        if (plcDetailProperties.get("isSubscribe") == null){
            plcDetailProperties.put("isSubscribe","true");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        properties = OpcUaProperties.getProperties(mainApp);
        setDefaultData();
    }

    private void setDefaultData(){
        userAuthenticationMode.setValue("anonymous");
        securityMode.setValue("none");
        connect.setSelected(true);
        subscribe.setSelected(true);
        clientListener.setText("com.opc.uaclient.uaclientlistener.MyUaClientListener");
    }

    private void inputPassword() {
        if (StringUtils.isBlank(password .getText())){
            noError = false;
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.password_MUST_NOT_NULL);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("password", password.getText());
        }
    }

    private void inputUsername() {
        if (StringUtils.isBlank(username.getText())){
            noError = false;
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.username_MUST_NOT_NULL);
            alert.showAndWait();
        }else {
            noError = true;
            plcDetailProperties.put("username", username.getText());
        }
    }

    @FXML
    private void nextDetailDeploy(){
        if (!noError){
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.PLEASE_CORRECT_NON_STANDARD_INPUT_FIRST);
            alert.showAndWait();
            return;
        }
        BorderPane rootLayout = mainApp.getRootLayout();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/DetailDeployPage.fxml"));
        try {
            AnchorPane nextDetailDeployPage = loader.load();
            rootLayout.setCenter(nextDetailDeployPage);
            DetailDeployController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void completeDeploy(){
        showBuildProfilePage();
    }

    @FXML
    private void saveDetailDeploy(){
        validateInput();
        if (!noError){
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.PLEASE_CORRECT_NON_STANDARD_INPUT_FIRST);
            alert.showAndWait();
            return;
        }
        //防止重复点击，重复保存当前opc客户端数据
        if (doubleClick){
            return;
        }
        doubleClick = true;
        if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
            inputUsername();
            inputPassword();
        }
        properties.getPlcList().add(plcDetailProperties);
        next.setDisable(false);
        complete.setDisable(false);
    }

    @FXML
    private void addOneListener(){
        showAddListenerPairDialog();
    }

    private void showBuildProfilePage(){
        TransportData data = CommonUtil.STAGE_DATA_MAP.get(mainApp);
        MenuItem save = data.getSave();
        MenuItem saveAs = data.getSaveAs();
        save.setDisable(false);
        saveAs.setDisable(false);
        BorderPane rootLayout = mainApp.getRootLayout();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/BuildProfilePage.fxml"));
        try {
            AnchorPane buildProfilePage = loader.load();
            rootLayout.setCenter(buildProfilePage);
            BuildProfileController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddListenerPairDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/AddOneListenerPairDialog.fxml"));
            AnchorPane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("添加一个监听器对");
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AddOneListenerPairController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPlcDetailProperties(plcDetailProperties);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
