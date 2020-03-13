package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.animation.MyAnimation;
import com.shdq.OPCUA_YML_gen_platform.model.ListenerPair;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.ThemeUtil;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.animation.ParallelTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
public class DetailDeployOverviewController {

    private Map<String,String> plcDetailDeployMap = new HashMap<>();

    @Setter
    private MainApp mainApp;

    @Setter
    private int plcIndex;

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
    @FXML
    private RadioButton connect;
    @FXML
    private RadioButton unconnect;
    @FXML
    private RadioButton subscribe;
    @FXML
    private RadioButton unsubscribe;
    @FXML
    private Button save;

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
    private ToggleGroup isConnect;
    private ToggleGroup isSubscribe;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    private boolean noError = true;
    @FXML
    private void initialize(){
//        playEffects();
        address.setTooltip(new Tooltip("客户端连接服务端地址，支持opc和http。"));
        plcNo.setTooltip(new Tooltip("正整数，并且不能重复。"));
        ns.setTooltip(new Tooltip("根据opc服务端配置。"));
        securityMode.setTooltip(new Tooltip("安全模式，根据服务端来进行相应配置。"));
        userAuthenticationMode.setTooltip(new Tooltip("用户认证模式，根据服务端来进行相应配置，username模式下，需要配置用户名和密码以便连接服务端时使用。"));
        username.setTooltip(new Tooltip("连接服务端的用户名。"));
        password.setTooltip(new Tooltip("连接服务端的密码。"));
        clientListener.setTooltip(new Tooltip("客户端的监听器，监听当前客户端的所有输入输出，系统默认：com.opc.uaclient.uaclientlistener.MyUaClientListener。"));
        sessionTimeOut.setTooltip(new Tooltip("会话过期时间。"));
        securityMode.setItems(FXCollections.observableArrayList("none",new Separator(),"sign",new Separator(),"sign&encrypt"));
        securityMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                plcDetailDeployMap.put("securityMode",String.valueOf(newValue));
            }
        });
        userAuthenticationMode.setItems(FXCollections.observableArrayList("anonymous",new Separator(),"username"));
        userAuthenticationMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String userAuthenticationMode = String.valueOf(newValue);
                plcDetailDeployMap.put("userAuthenticationMode",userAuthenticationMode);
//                if (userAuthenticationMode.equalsIgnoreCase("username")){
//                    username.setDisable(false);
//                    password.setDisable(false);
//                }
//                if (userAuthenticationMode.equalsIgnoreCase("anonymous")){
//                    username.setDisable(true);
//                    password.setDisable(true);
//                }
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
        isConnect.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                plcDetailDeployMap.put("isConnect",isConnect.getSelectedToggle().getUserData().toString()));

        subscribe.setToggleGroup(isSubscribe);
        unsubscribe.setToggleGroup(isSubscribe);
        isSubscribe.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                plcDetailDeployMap.put("isSubscribe",isSubscribe.getSelectedToggle().getUserData().toString()));
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

    public void setPlcDetailDeployMap(Map<String, String> plcDetailDeployMap) {
        this.plcDetailDeployMap = plcDetailDeployMap;
        setData();
    }

    private void setData(){
        if (plcDetailDeployMap != null || plcDetailDeployMap.size() != 0){
            plcDetailDeployMap.forEach((key,value)->{
                if (key.equalsIgnoreCase("address")){
                    address.setText(value);
                }else if (key.equalsIgnoreCase("plcNo")){
                    plcNo.setText(value);
                }else if (key.equalsIgnoreCase("ns")){
                    ns.setText(value);
                }else if (key.equalsIgnoreCase("securityMode")){
                    securityMode.setValue(value);
                }else if (key.equalsIgnoreCase("userAuthenticationMode")){
                    userAuthenticationMode.setValue(value);
                    if (value.equalsIgnoreCase("username")){
                        username.setDisable(false);
                        password.setDisable(false);
                    }
                    if (value.equalsIgnoreCase("anonymous")){
                        username.setDisable(true);
                        password.setDisable(true);
                    }
                }else if (key.equalsIgnoreCase("username")){
                    username.setText(value);
                }else if (key.equalsIgnoreCase("password")){
                    password.setText(value);
                }else if (key.equalsIgnoreCase("clientListener")){
                    clientListener.setText(value);
                }else if (key.equalsIgnoreCase("sessionTimeOut")){
                    sessionTimeOut.setText(value);
                }else if (key.equalsIgnoreCase("isConnect")){
                    if (Boolean.valueOf(value)){
                        connect.setSelected(true);
                    }else {
                        unconnect.setSelected(true);
                    }
                }else if (key.equalsIgnoreCase("isSubscribe")){
                    if (Boolean.valueOf(value)){
                        subscribe.setSelected(true);
                    }else {
                        unsubscribe.setSelected(true);
                    }
                }
            });
        }
    }


    @FXML
    private void lookOverListeners(){
        List<ListenerPair> pairs = new ArrayList<>();
        plcDetailDeployMap.forEach((key,value)->{
            if (!CommonUtil.notListenerNames.contains(key)){
                pairs.add(new ListenerPair(String.valueOf(pairs.size()+1),key,value));
            }
        });
        showListenerPairsDialog(pairs);
    }

    private void showListenerPairsDialog(List<ListenerPair> pairs) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ShowListenerPairDialog.fxml"));
            AnchorPane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("查看监听器对");
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ShowListenerPairController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPairs(pairs);
            controller.setMainApp(mainApp);
            controller.setPlcIndex(plcIndex);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editDetailDeploy(){
        setDisable(false);
    }

    @FXML
    private void saveDetailDeploy(){
        validateInput();
        if (!noError){
            return;
        }
        setDisable(true);
    }

    private void validateInput(){
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (StringUtils.isBlank(address.getText())){
            alert.setContentText(WrongMsg.address_MUST_NOT_NULL);
            alert.showAndWait();
            return;
        }else {
            plcDetailDeployMap.put("address",address.getText());
        }
        if (!WrongMsg.pattern.matcher(plcNo.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.plcNo_MUST_BE_INTEGER);
            alert.showAndWait();
            return;
        }else {
            plcDetailDeployMap.put("plcNo",plcNo.getText());
        }
        if (!WrongMsg.pattern.matcher(ns.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.ns_MUST_BE_INTEGER);
            alert.showAndWait();
            return;
        }else {
            plcDetailDeployMap.put("ns", ns.getText());
        }
        if (StringUtils.isBlank(clientListener.getText())){
            noError = false;
            alert.setContentText(WrongMsg.clientListener_MUST_NOT_NULL);
            alert.showAndWait();
            return;
        }else {
            plcDetailDeployMap.put("clientListener", clientListener.getText());
        }
        if (!WrongMsg.pattern.matcher(sessionTimeOut.getText()).matches()) {
            noError = false;
            alert.setContentText(WrongMsg.sessionTimeOut_MUST_BE_INTEGER);
            alert.showAndWait();
            return;
        }else {
            plcDetailDeployMap.put("sessionTimeOut",sessionTimeOut.getText());
        }
        if (!plcDetailDeployMap.get("userAuthenticationMode").equalsIgnoreCase("anonymous")){
            if (StringUtils.isBlank(password .getText())){
                noError = false;
                alert.setTitle(WrongMsg.Validation_Error);
                alert.setHeaderText(WrongMsg.Non_Standard_Input);
                alert.setContentText(WrongMsg.password_MUST_NOT_NULL);
                alert.showAndWait();
                return;
            }else {
                plcDetailDeployMap.put("password", password.getText());
            }
            if (StringUtils.isBlank(username.getText())){
                noError = false;
                alert.setTitle(WrongMsg.Validation_Error);
                alert.setHeaderText(WrongMsg.Non_Standard_Input);
                alert.setContentText(WrongMsg.username_MUST_NOT_NULL);
                alert.showAndWait();
                return;
            }else {
                plcDetailDeployMap.put("username", username.getText());
            }
        }
    }

    private void setDisable(boolean b){
        address.setDisable(b);
        plcNo.setDisable(b);
        ns.setDisable(b);
        securityMode.setDisable(b);
        userAuthenticationMode.setDisable(b);
        username.setDisable(b);
        password.setDisable(b);
        clientListener.setDisable(b);
        sessionTimeOut.setDisable(b);
        connect.setDisable(b);
        unconnect.setDisable(b);
        subscribe.setDisable(b);
        unsubscribe.setDisable(b);
        save.setDisable(b);
    }
}
