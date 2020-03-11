package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.concurrent.MyTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shdq-fjy
 */
@Slf4j
public class ShowProgressBarController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressMsg;

    @FXML
    private void initialize(){

    }

    public void runTask(MyTask task){
        Service<Integer> service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return task;
            }
        };
        progressBar.progressProperty().bind(service.progressProperty());
        task.messageProperty().addListener((observable, oldValue, newValue) -> {
            progressMsg.setText(service.getMessage());
            if (service.getValue() == null || (service.getValue() < 100 && service.getValue() >0)){
//                progressMsg.setTextFill(Color.rgb(0,0,0));
                //跟随主题css文件设置styleClass：label-bright
            }else if (service.getValue() == 100){
                progressMsg.setTextFill(Color.rgb(0,255,0));
            }else {
                progressMsg.setTextFill(Color.rgb(255,0,0));
            }
        });
        service.restart();
    }
}
