package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.animation.MyAnimation;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.model.TransportData;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.ThemeUtil;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.animation.ParallelTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    @FXML
    private Label addressLabel;
    @FXML
    private Label plcNoLabel;
    @FXML
    private Label nsLabel;
    @FXML
    private Label securityModeLabel;
    @FXML
    private Label userAuthenticationModeLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label sessionTimeOutLabel;
    @FXML
    private Label clientListenerLabel;
    @FXML
    private Label isConnectLabel;
    @FXML
    private Label isSubscribeLabel;
    @FXML
    private ImageView addressPoint;
    @FXML
    private ImageView plcNoPoint;
    @FXML
    private ImageView nsPoint;
    @FXML
    private ImageView usernamePoint;
    @FXML
    private ImageView passwordPoint;
    @FXML
    private ImageView clientListenerPoint;
    @FXML
    private ImageView sessionTimeOutPoint;
    private boolean addressNoError = false;
    private boolean plcNoNoError = false;
    private boolean nsNoError = false;
    private boolean usernameNoError = false;
    private boolean passwordNoError = false;
    private boolean clientListenerNoError = true;//默认有值
    private boolean sessionTimeOutNoError = false;
    private boolean doubleClick = false;
    Alert alert = new Alert(Alert.AlertType.WARNING);
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize(){
        playEffects();
        setTextFieldFocusedPropertyListener();
        setTextFieldOnKeyPressedEvent();
//        setTextFieldOnKeyTypedEvent();
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
                usernameNoError = false;
                passwordNoError = false;
                usernamePoint.setImage(new Image("/images/wrong.png"));
                usernamePoint.setDisable(false);
                usernamePoint.setCursor(Cursor.HAND);
                usernamePoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.username_MUST_NOT_NULL);
                    alert.showAndWait();
                });
                passwordPoint.setImage(new Image("/images/wrong.png"));
                passwordPoint.setDisable(false);
                passwordPoint.setCursor(Cursor.HAND);
                passwordPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.password_MUST_NOT_NULL);
                    alert.showAndWait();
                });
                usernamePoint.setVisible(false);
                passwordPoint.setVisible(false);
            }
            if (userAuthenticationMode.equalsIgnoreCase("anonymous")){
                username.setDisable(true);
                password.setDisable(true);
                usernameNoError = true;
                passwordNoError = true;
                usernamePoint.setVisible(false);
                passwordPoint.setVisible(false);
            }
            setSaveButtonEnable();
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

    private void setTextFieldOnKeyTypedEvent() {
        address.setOnKeyTyped(event -> {
            validateInput("address",address.getText());
        });
        plcNo.setOnKeyTyped(event -> {
            validateInput("plcNo",plcNo.getText());
        });
        ns.setOnKeyTyped(event -> {
            validateInput("ns",ns.getText());
        });
        username.setOnKeyTyped(event -> {
            if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                validateInput("username",username.getText());
            }
        });
        password.setOnKeyTyped(event -> {
            if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                validateInput("password",password.getText());
            }
        });
        clientListener.setOnKeyTyped(event -> {
            validateInput("clientListener",clientListener.getText());
        });
        sessionTimeOut.setOnKeyTyped(event -> {
            validateInput("sessionTimeOut",sessionTimeOut.getText());
        });
    }

    private void setTextFieldOnKeyPressedEvent() {
        address.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                validateInput("address",address.getText());
            }
        });
        plcNo.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                validateInput("plcNo",plcNo.getText());
            }
        });
        ns.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                validateInput("ns",ns.getText());
            }
        });
        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                    validateInput("username",username.getText());
                }
            }
        });
        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                    validateInput("password",password.getText());
                }
            }
        });
        clientListener.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                validateInput("clientListener",clientListener.getText());
            }
        });
        sessionTimeOut.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.TAB)){
                validateInput("sessionTimeOut",sessionTimeOut.getText());
            }
        });
    }

    private void setTextFieldFocusedPropertyListener() {
        address.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                validateInput("address",address.getText());
            }
        });
        plcNo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                validateInput("plcNo",plcNo.getText());
            }
        });
        ns.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                validateInput("ns",ns.getText());
            }
        });
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                    validateInput("username",username.getText());
                }
            }
        });
        password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                if (!plcDetailProperties.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
                    validateInput("password",password.getText());
                }
            }
        });
        clientListener.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                validateInput("clientListener",clientListener.getText());
            }
        });
        sessionTimeOut.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                validateInput("sessionTimeOut",sessionTimeOut.getText());
            }
        });
    }

    private void playEffects() {
        List<Node> nodes = Arrays.asList(address,plcNo,ns,securityMode,userAuthenticationMode,username,password,clientListener,sessionTimeOut,
                connect,unconnect,subscribe,unsubscribe);
        List<Label> labels = Arrays.asList(addressLabel,plcNoLabel,nsLabel,securityModeLabel,userAuthenticationModeLabel,usernameLabel,passwordLabel,clientListenerLabel,sessionTimeOutLabel,
                isConnectLabel,isSubscribeLabel);
        List<ParallelTransition> parallelTransitions = MyAnimation.detailPageEffectsPlay(nodes,labels);
        parallelTransitions.forEach(parallelTransition -> {
            parallelTransition.play();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void setSelectedInput(){
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

    private void validateInput(String name,String str){
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (name.equals("address")){
            //todo:还需添加url正则表达式比较，校验是否是合法的地址
            if (StringUtils.isBlank(str)){
                addressNoError = false;
                addressPoint.setImage(new Image("/images/wrong.png"));
                addressPoint.setVisible(true);
                addressPoint.setDisable(false);
                addressPoint.setCursor(Cursor.HAND);
                addressPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.address_MUST_NOT_NULL);
                    alert.showAndWait();
                });
            }else {
                addressNoError = true;
                addressPoint.setImage(new Image("/images/correct.png"));
                addressPoint.setVisible(true);
                addressPoint.setDisable(true);
                addressPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,address.getText());
            }
        }
        if (name.equals("plcNo")){
            //todo：检查是否有重复的编号
            if (StringUtils.isBlank(str) || !WrongMsg.pattern.matcher(str).matches()){
                plcNoNoError = false;
                plcNoPoint.setImage(new Image("/images/wrong.png"));
                plcNoPoint.setVisible(true);
                plcNoPoint.setDisable(false);
                plcNoPoint.setCursor(Cursor.HAND);
                plcNoPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.plcNo_MUST_NOT_NULL +"并且"+WrongMsg.plcNo_MUST_BE_INTEGER);
                    alert.showAndWait();
                });
            }else {
                plcNoNoError = true;
                plcNoPoint.setImage(new Image("/images/correct.png"));
                plcNoPoint.setVisible(true);
                plcNoPoint.setDisable(true);
                plcNoPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,plcNo.getText());
            }
        }
        if (name.equals("ns")){
            if (StringUtils.isBlank(str) || !WrongMsg.pattern.matcher(str).matches()){
                nsNoError = false;
                nsPoint.setImage(new Image("/images/wrong.png"));
                nsPoint.setVisible(true);
                nsPoint.setDisable(false);
                nsPoint.setCursor(Cursor.HAND);
                nsPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.ns_MUST_NOT_NULL+"并且"+WrongMsg.ns_MUST_BE_INTEGER);
                    alert.showAndWait();
                });
            }else {
                nsNoError = true;
                nsPoint.setImage(new Image("/images/correct.png"));
                nsPoint.setVisible(true);
                nsPoint.setDisable(true);
                nsPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,ns.getText());
            }
        }
        if (name.equals("username")){
            if (StringUtils.isBlank(str)){
                usernameNoError = false;
                usernamePoint.setImage(new Image("/images/wrong.png"));
                usernamePoint.setVisible(true);
                usernamePoint.setDisable(false);
                usernamePoint.setCursor(Cursor.HAND);
                usernamePoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.username_MUST_NOT_NULL);
                    alert.showAndWait();
                });
            }else {
                usernameNoError = true;
                usernamePoint.setImage(new Image("/images/correct.png"));
                usernamePoint.setVisible(true);
                usernamePoint.setDisable(true);
                usernamePoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,username.getText());
            }
        }
        if (name.equals("password")){
            if (StringUtils.isBlank(str)){
                passwordNoError = false;
                passwordPoint.setImage(new Image("/images/wrong.png"));
                passwordPoint.setVisible(true);
                passwordPoint.setDisable(false);
                passwordPoint.setCursor(Cursor.HAND);
                passwordPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.password_MUST_NOT_NULL);
                    alert.showAndWait();
                });
            }else {
                passwordNoError = true;
                passwordPoint.setImage(new Image("/images/correct.png"));
                passwordPoint.setVisible(true);
                passwordPoint.setDisable(true);
                passwordPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,password.getText());
            }
        }
        if (name.equals("clientListener")){
            if (StringUtils.isBlank(str)){
                clientListenerNoError = false;
                clientListenerPoint.setImage(new Image("/images/wrong.png"));
                clientListenerPoint.setVisible(true);
                clientListenerPoint.setDisable(false);
                clientListenerPoint.setCursor(Cursor.HAND);
                clientListenerPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.clientListener_MUST_NOT_NULL);
                    alert.showAndWait();
                });
            }else {
                clientListenerNoError = true;
                clientListenerPoint.setImage(new Image("/images/correct.png"));
                clientListenerPoint.setVisible(true);
                clientListenerPoint.setDisable(true);
                clientListenerPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,clientListener.getText());
            }
        }
        if (name.equals("sessionTimeOut")){
            if (StringUtils.isBlank(str) || !WrongMsg.pattern.matcher(str).matches()){
                sessionTimeOutNoError = false;
                sessionTimeOutPoint.setImage(new Image("/images/wrong.png"));
                sessionTimeOutPoint.setVisible(true);
                sessionTimeOutPoint.setDisable(false);
                sessionTimeOutPoint.setCursor(Cursor.HAND);
                sessionTimeOutPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.sessionTimeOut_MUST_NOT_NULL +"并且"+WrongMsg.sessionTimeOut_MUST_BE_INTEGER);
                    alert.showAndWait();
                });
            }else {
                sessionTimeOutNoError = true;
                sessionTimeOutPoint.setImage(new Image("/images/correct.png"));
                sessionTimeOutPoint.setVisible(true);
                sessionTimeOutPoint.setDisable(true);
                sessionTimeOutPoint.setCursor(Cursor.DEFAULT);
                plcDetailProperties.put(name,sessionTimeOut.getText());
            }
        }
        setSaveButtonEnable();
    }

    private void setSaveButtonEnable() {
        if (addressNoError && plcNoNoError && nsNoError && usernameNoError && passwordNoError && clientListenerNoError && sessionTimeOutNoError){
            save.setDisable(false);
        }else {
            save.setDisable(true);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        properties = OpcUaProperties.getProperties(mainApp);
        setDefaultData();
        setSelectedInput();
    }

    private void setDefaultData(){
        userAuthenticationMode.setValue("anonymous");
        securityMode.setValue("none");
        connect.setSelected(true);
        subscribe.setSelected(true);
        clientListener.setText("com.opc.uaclient.uaclientlistener.MyUaClientListener");
    }

    @FXML
    private void nextDetailDeploy(){
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
        //防止重复点击，重复保存当前opc客户端数据
        if (doubleClick){
            return;
        }
        doubleClick = true;
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
