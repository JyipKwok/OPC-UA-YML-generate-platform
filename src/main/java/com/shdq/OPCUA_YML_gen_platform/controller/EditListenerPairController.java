package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.model.ListenerPair;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

public class EditListenerPairController {

    @Setter
    private Stage dialogStage;

    private ListenerPair pair;

    private boolean isOkClicked;

    @FXML
    private TextField listenerName;

    @FXML
    private TextArea nodeString;

    public void setPair(ListenerPair pair) {
        this.pair = pair;
        setData();
    }

    private void setData(){
        listenerName.setText(pair.getListenerName());
        nodeString.setText(pair.getNodeStr());
    }

    @FXML
    private void initialize(){

    }

    @FXML
    private void handleOK(){
        String listenerNameStr = listenerName.getText();
        String nodeStringStr = nodeString.getText();
        if (StringUtils.isBlank(listenerNameStr) || StringUtils.isBlank(nodeStringStr)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(WrongMsg.Validation_Error);
            alert.setHeaderText(WrongMsg.Non_Standard_Input);
            alert.setContentText(WrongMsg.listener_name_and_nodeStr_MUST_NOT_BE_NULL);
            alert.showAndWait();
        }else {
            pair.setListenerName(listenerNameStr);
            pair.setNodeStr(nodeStringStr);
            isOkClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel(){
        isOkClicked = false;
        dialogStage.close();
    }

    public boolean isOkClicked(){
        return isOkClicked;
    }
}
