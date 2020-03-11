package com.shdq.OPCUA_YML_gen_platform.util;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.concurrent.MyTask;
import com.shdq.OPCUA_YML_gen_platform.controller.ShowProgressBarController;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author shdq-fjy
 */
@Slf4j
public class BuildFileUtil {

    public static void buildProfileAs(String filePath,OpcUaProperties opcUaProperties,MainApp mainApp){
        showProgressBar(filePath,opcUaProperties,false,mainApp);
    }

    public static void buildProfile(MainApp mainApp,OpcUaProperties properties){
        String path = PathUtil.getFilePath();
        String filePath = path + File.separator+CommonUtil.default_file_name+CommonUtil.default_file_suffix;
        File file = new File(filePath);
        if (file.exists()){
            if (buildRecordFile(true,properties,mainApp,filePath)){
                return;
            }
        }
        if (buildYAMLFileInBackground(filePath,properties,false,mainApp)){
            return;
        }
        mainApp.showCommonDeployPage();
    }

    public static void cancelBuild(MainApp mainApp){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 创建一个确认对话框
        alert.setHeaderText("温馨提示");
        alert.setContentText("确定放弃保存当前配置？");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            mainApp.showCommonDeployPage();
        } else { // 单击了取消按钮CANCEL_CLOSE
            return;
        }
    }

    private static boolean buildRecordFile(boolean exists,OpcUaProperties properties,MainApp mainApp,String filePath){
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-mm-ss"));
        String fileName = CommonUtil.default_file_name+"-"+dateStr+CommonUtil.default_file_suffix;
        String historyPath;
        if (CommonUtil.default_file_path.equals(new File(filePath).getParent())){
            historyPath = CommonUtil.history_record_file_path;
        }else {
            historyPath = new File(filePath).getParent();
        }
        log.debug(historyPath);
        if (exists){
            OpcUaProperties propertiesOld = YamlConverter.getInstance().readFromYAMLFile(CommonUtil.default_file_path+ File.separator+CommonUtil.default_file_name+CommonUtil.default_file_suffix,OpcUaProperties.class);
            return buildYAMLFileInBackground(historyPath + File.separator+fileName,propertiesOld,true,mainApp);
        }else {
            return buildYAMLFileInBackground(historyPath + File.separator+fileName,properties,true,mainApp);
        }
    }

    /**
     * 后台生成文件，事实上生成文件速度很快，根本用不上后台任务，只是为了熟悉
     * @param filePath
     * @param properties
     * @param isRecord
     */
    private static boolean buildYAMLFileInBackground(String filePath,OpcUaProperties properties,boolean isRecord,MainApp mainApp){
        if (CommonUtil.isOccupation){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("资源正在使用");
            alert.setContentText("有窗口正在将之前生成的文件转存为历史文件，并生成新的默认配置文件。\n"+
                    "您可以等待占用资源窗口的任务完成，或者使用另存为。");
            alert.showAndWait();
            return true;
        }
        CommonUtil.isOccupation = true;
        showProgressBar(filePath,properties,isRecord,mainApp);
        CommonUtil.isOccupation = false;
        return false;
    }

    private static void showProgressBar(String filePath, OpcUaProperties properties, boolean isRecord, MainApp mainApp) {
        if (isRecord) {
            YamlConverter.getInstance().writeToYAMLFile(filePath, properties);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ShowProgressBar.fxml"));
            AnchorPane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            // 窗口父子关系
            Stage progressStage = new Stage();
            progressStage.initStyle(StageStyle.UNDECORATED);
            progressStage.initStyle(StageStyle.TRANSPARENT);
            progressStage.initModality(Modality.WINDOW_MODAL);
            Stage primaryStage = mainApp.getPrimaryStage();
            progressStage.initOwner(primaryStage);
            CommonUtil.setProgressBarWindowCoordinate(mainApp.getPrimaryStage(),progressStage);
            Scene scene = new Scene(pane);
            progressStage.setScene(scene);
            ShowProgressBarController controller = loader.getController();
            controller.runTask(new MyTask(filePath, properties, isRecord, progressStage));
            progressStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
