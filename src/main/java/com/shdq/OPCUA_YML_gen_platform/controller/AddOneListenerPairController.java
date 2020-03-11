package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;


@Setter
public class AddOneListenerPairController {

    private Stage dialogStage;

    private Map<String,String> plcDetailProperties;

    @FXML
    private TextField listenerName;

    @FXML
    private TextArea nodeString;


    @FXML
    private void initialize(){

    }

    private void validateInput(){
        String listenerNameStr = listenerName.getText();
        String nodeStringStr = nodeString.getText();
        if (StringUtils.isBlank(listenerNameStr) || StringUtils.isBlank(nodeStringStr)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.listener_name_and_nodeStr_MUST_NOT_BE_NULL);
            alert.showAndWait();
        }else {
            plcDetailProperties.put(listenerNameStr,nodeStringStr);
        }
    }
    @FXML
    private void handleOK(){
        validateInput();
        dialogStage.close();
    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }
}
