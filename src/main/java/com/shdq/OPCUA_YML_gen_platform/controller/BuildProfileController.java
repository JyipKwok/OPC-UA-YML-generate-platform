package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.BuildFileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BuildProfileController {
    private MainApp mainApp;
    OpcUaProperties properties;
    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize(){

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        properties = OpcUaProperties.getProperties(this.mainApp);
        List<Tab> tabs = newTabs();
        tabPane.getTabs().addAll(tabs);
    }

    private List<Tab> newTabs() {
        List<Tab> tabs = new ArrayList<>();
        Pane commonDeployPane = loadCommonDeployPane();
        Tab commonDeployTab = new Tab("通用配置参数",commonDeployPane);
        tabs.add(commonDeployTab);
        for (int i = 0; i < properties.getPlcList().size(); i++){
            Pane detailDeployPane = loadDetailDeployPane(properties.getPlcList().get(i),i);
            tabs.add(new Tab("客户端"+ (i+1)+"具体配置参数",detailDeployPane));
        }
        return tabs;
    }

    private Pane loadDetailDeployPane(Map<String, String> stringStringMap,int plcIndex) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/DetailDeployOverviewPage.fxml"));
        try {
            AnchorPane pane = loader.load();
            DetailDeployOverviewController controller = loader.getController();
            controller.setPlcDetailDeployMap(stringStringMap);
            controller.setMainApp(mainApp);
            controller.setPlcIndex(plcIndex);
            return pane;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Pane loadCommonDeployPane() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/CommonDeployOverviewPage.fxml"));
        try {
            AnchorPane pane = loader.load();
            CommonDeployOverviewController controller = loader.getController();
            controller.setMainApp(mainApp);
            return pane;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void buildProfile(){
        BuildFileUtil.buildProfile(mainApp,OpcUaProperties.getProperties(mainApp));
    }

    @FXML
    private void cancelBuild(){
        BuildFileUtil.cancelBuild(mainApp);
    }
}
