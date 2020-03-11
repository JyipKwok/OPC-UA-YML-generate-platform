package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class CommonDeployOverviewController {

    private MainApp mainApp;

    private OpcUaProperties properties;

    @FXML
    private TextField listenerPath;

    @FXML
    private TextField nodesParser;

    @FXML
    private TextField publishRate;

    @FXML
    private Button save;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        properties = OpcUaProperties.getProperties(this.mainApp);
        listenerPath.setText(properties.getListenerPath());
        nodesParser.setText(properties.getNodesParser());
        publishRate.setText(String.valueOf(properties.getPublishRate()));
    }

    @FXML
    private void initialize(){

    }

    @FXML
    private void editCommonDeploy(){
        setDisable(false);
    }

    @FXML
    private void saveCommonDeploy(){
        String listenerPathStr = listenerPath.getText();
        String nodeParserStr = nodesParser.getText();
        String publishRateStr = publishRate.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (StringUtils.isBlank(listenerPathStr) || StringUtils.isBlank(nodeParserStr) || StringUtils.isBlank(publishRateStr)){
            alert.setContentText(WrongMsg.listenerPath_MUST_NOT_NULL+WrongMsg.nodesParser_MUST_NOT_NULL+WrongMsg.publishRate_MUST_NOT_NULL);
            alert.showAndWait();
            return;
        }else if (!listenerPathStr.endsWith(".")){
            alert.setContentText(WrongMsg.listenerPath_MUST_END_WITH_DOT);
            alert.showAndWait();
            return;
        }else if (!WrongMsg.pattern.matcher(publishRateStr).matches()){
            alert.setContentText(WrongMsg.publishRate_MUST_BE_INTEGER);
            alert.showAndWait();
            return;
        }else {
            properties.setPublishRate(Integer.valueOf(publishRateStr));
            properties.setNodesParser(nodeParserStr);
            properties.setListenerPath(listenerPathStr);
        }
        log.debug(properties.toString());
        setDisable(true);
    }

    private void setDisable(boolean b){
        save.setDisable(b);
        listenerPath.setDisable(b);
        nodesParser.setDisable(b);
        publishRate.setDisable(b);
    }
}
