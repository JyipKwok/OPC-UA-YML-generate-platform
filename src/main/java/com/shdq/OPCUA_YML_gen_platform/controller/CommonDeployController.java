package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * @author shdq-fjy
 */
@Setter
public class CommonDeployController {

    private MainApp mainApp;

    String listenerPathStr;
    String nodesParserStr;
    String publishRateStr;

    boolean listenerPathHasInput = false;
    boolean nodesParserHashInput = false;
    boolean publishRateHashInput = false;
    @FXML
    private TextField listenerPath;

    @FXML
    private TextField nodesParser;

    @FXML
    private TextField publishRate;

    @FXML
    private Button complete;

    @FXML
    private void initialize(){
        listenerPath.setTooltip(new Tooltip("监听器类的包路径，最后以“.”结尾。"));
        nodesParser.setTooltip(new Tooltip("自定义节点解析器包名+类名，用于解析后续配置监听器和监听节点对中，对节点字符串的解析。"));
        publishRate.setTooltip(new Tooltip("监听器刷新率（推送响应速率）。"));
    }

    @FXML
    private void handleCommonDeployComplete(){
        listenerPathStr = listenerPath.getText();
        nodesParserStr = nodesParser.getText();
        publishRateStr = publishRate.getText();
        if (validateInput()){
            setValue();
            showDetailDeployPage();
        }
    }

    @FXML
    private void handleListenerPathOnInputTextChange(){
        listenerPathHasInput = true;
        if (listenerPathHasInput && nodesParserHashInput && publishRateHashInput){
            complete.setDisable(false);
            complete.setDefaultButton(true);
        }
    }

    @FXML
    private void handleNodesParserOnInputTextChange(){
        nodesParserHashInput = true;
        if (listenerPathHasInput && nodesParserHashInput && publishRateHashInput){
            complete.setDisable(false);
            complete.setDefaultButton(true);
        }
    }

    @FXML
    private void handlePublishRateOnInputTextChange(){
        publishRateHashInput = true;
        if (listenerPathHasInput && nodesParserHashInput && publishRateHashInput){
            complete.setDisable(false);
            complete.setDefaultButton(true);
        }
    }

    private boolean validateInput(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (StringUtils.isBlank(listenerPathStr) || StringUtils.isBlank(nodesParserStr) || StringUtils.isBlank(publishRateStr)){
            if (StringUtils.isBlank(listenerPathStr)) publishRateHashInput = false;
            if (StringUtils.isBlank(nodesParserStr)) nodesParserHashInput = false;
            if (StringUtils.isBlank(publishRateStr)) publishRateHashInput = false;
            alert.setContentText(WrongMsg.listenerPath_MUST_NOT_NULL+WrongMsg.nodesParser_MUST_NOT_NULL+WrongMsg.publishRate_MUST_NOT_NULL);
            alert.showAndWait();
            return false;
        }else if (!listenerPathStr.endsWith(".")){
            listenerPathHasInput = false;
            alert.setContentText(WrongMsg.listenerPath_MUST_END_WITH_DOT);
            alert.showAndWait();
            return false;
        }else if (!WrongMsg.pattern.matcher(publishRateStr).matches()){
            publishRateHashInput = false;
            alert.setContentText(WrongMsg.publishRate_MUST_BE_INTEGER);
            alert.showAndWait();
            return false;
        }else {
            return true;
        }
    }

    /**
     * 设置通用属性到properties对象中
     */
    private void setValue(){
        OpcUaProperties properties = OpcUaProperties.getProperties(mainApp);
        properties.setListenerPath(listenerPathStr);
        properties.setNodesParser(nodesParserStr);
        properties.setPublishRate(Long.valueOf(publishRateStr));
    }

    /**
     * 通用项配置完成，点击按钮展示服务端的配置界面
     */
    private void showDetailDeployPage() {
        BorderPane rootLayout = mainApp.getRootLayout();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/DetailDeployPage.fxml"));
        try {
            AnchorPane detailDeployPage = loader.load();
            rootLayout.setCenter(detailDeployPage);
            DetailDeployController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
