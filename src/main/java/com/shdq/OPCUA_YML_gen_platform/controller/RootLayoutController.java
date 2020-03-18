package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.model.TransportData;
import com.shdq.OPCUA_YML_gen_platform.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author shdq-fjy
 */
@Slf4j
public class RootLayoutController {

    private MainApp mainApp;

    @FXML
    private CheckMenuItem defaultTheme;

    @FXML
    private CheckMenuItem darkTheme;

    @FXML
    private CheckMenuItem whiteTheme;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveAs;

    @FXML
    private void initialize(){
        CommonUtil.Theme theme = ThemeUtil.getTheme();
        if (theme == CommonUtil.Theme.DEFAULT){
            setSelected(true,false,false);
        }else if (theme == CommonUtil.Theme.DARK){
            setSelected(false,true,false);
        }else if (theme == CommonUtil.Theme.WHITE){
            setSelected(false,false,true);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        TransportData data = new TransportData(save,saveAs);
        CommonUtil.bindData(mainApp,data);
    }

    @FXML
    private void handleNew(){
        new MainApp().startNewWindow(null);
    }

    @FXML
    private void handleOpenFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        fileChooser.setInitialDirectory(new File(PathUtil.getFilePath()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件","*.*"),
                new FileChooser.ExtensionFilter("YAML files (*.yml)","*.yml")
        );
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null){
            if (file.getPath().endsWith(".yml")){
                OpcUaProperties properties = YamlConverter.getInstance().readFromYAMLFile(file.getAbsolutePath(),OpcUaProperties.class);
                if (properties == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("读取失败");
                    alert.setContentText("文件无内容或者文件格式不正确！");
                    alert.showAndWait();
                }else {
                    MainApp mainApp = new MainApp();
                    mainApp.startNewWindow(properties);
                    mainApp.getPrimaryStage().setTitle(file.getAbsolutePath());
                }
            }
        }
    }

    @FXML
    private void handleSave(){
        BuildFileUtil.buildProfile(mainApp, OpcUaProperties.getProperties(mainApp));
    }

    @FXML
    private void handleSaveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("另存为...");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.setInitialFileName(CommonUtil.default_file_name);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("YAML files (*.yml)","*.yml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null){
            if (file.getPath().endsWith(".yml")){
                BuildFileUtil.buildProfileAs(file.getAbsolutePath(),OpcUaProperties.getProperties(mainApp),mainApp);
            }
        }
    }

    @FXML
    private void handleExit(){
        System.exit(0);
    }

    @FXML
    private void handleDefaultTheme(){
        setSelected(true,false,false);
        ThemeUtil.loadTheme(CommonUtil.Theme.DEFAULT);
    }

    @FXML
    private void handleDarkTheme(){
        setSelected(false,true,false);
        ThemeUtil.loadTheme(CommonUtil.Theme.DARK);
    }

    @FXML
    private void handleWhiteTheme(){
        setSelected(false,false,true);
        ThemeUtil.loadTheme(CommonUtil.Theme.WHITE);
    }

    private void setSelected(boolean isDefault,boolean isDark,boolean isWhite){
        defaultTheme.setSelected(isDefault);
        darkTheme.setSelected(isDark);
        whiteTheme.setSelected(isWhite);
    }

    @FXML
    private void handleDefaultFilePath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择默认文件保存路径...");
        directoryChooser.setInitialDirectory(new File("C:\\"));
        File file = directoryChooser.showDialog(mainApp.getPrimaryStage());
        if (file != null){
            String filePath = file.getAbsolutePath();
            log.debug("默认文件路径更改为：{}",filePath);
            PathUtil.setFilePath(filePath);
        }
    }

    @FXML
    private void handleHistoryRecord(){

    }

    @FXML
    private void handleAboutApplication(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/AboutApplication.fxml"));
        try {
            Pane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("关于应用");
            dialogStage.setResizable(false);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.setScene(new Scene(pane));
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAboutAuthor(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/AboutAuthor.fxml"));
        try {
            Pane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("关于作者");
            dialogStage.setResizable(false);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.setScene(new Scene(pane));
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFeedback(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/FeedBack.fxml"));
        try {
            Pane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("反馈");
            dialogStage.setResizable(false);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.setScene(new Scene(pane));
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
