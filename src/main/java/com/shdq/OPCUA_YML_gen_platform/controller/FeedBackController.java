package com.shdq.OPCUA_YML_gen_platform.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.apache.commons.lang.StringUtils;

/**
 * @author shdq-fjy
 */
public class FeedBackController {

    @FXML
    private TextArea feedbackContent;
    @FXML
    private Button sent;

    @FXML
    private void initialize(){
        feedbackContent.setOnKeyTyped(event -> {
            String content = feedbackContent.getText();
            if (StringUtils.isNotBlank(content)){
                sent.setDisable(false);
            }else {
                return;
            }
        });
        sent.setOnMouseClicked(event -> {
            sentMsg();
        });
    }

    private void sentMsg() {

    }
}
