package com.shdq.OPCUA_YML_gen_platform;

import com.shdq.OPCUA_YML_gen_platform.controller.BuildProfileController;
import com.shdq.OPCUA_YML_gen_platform.controller.CommonDeployController;
import com.shdq.OPCUA_YML_gen_platform.controller.RootLayoutController;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.model.TransportData;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.ThemeUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.shdq.OPCUA_YML_gen_platform.util.MySysTray;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * @author shdq-fjy
 */
@Getter
@Slf4j
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.getIcons().add(new Image(CommonUtil.image_url));
        this.primaryStage.setTitle(CommonUtil.primary_stage_title_prefix + " 主窗口");
        //禁止窗口缩放
        this.primaryStage.setResizable(false);
        this.primaryStage.setWidth(607);
        this.primaryStage.setHeight(535);
        CommonUtil.setMainWindowCoordinate(this.primaryStage);
        //添加到系统托盘
        new MySysTray(primaryStage).initSystemTray();
        // (如果不在stage.show()前使用这个，那么点击窗口的关闭按钮后就不能再通过系统托盘显示)多次使用显示和隐藏设置false
        Platform.setImplicitExit(false);
        initRootLayout();
        showCommonDeployPage();
    }

    public void startNewWindow(OpcUaProperties properties){
        this.primaryStage = new Stage();
        this.primaryStage.getIcons().add(new Image(CommonUtil.image_url));
        this.primaryStage.setTitle(CommonUtil.primary_stage_title_prefix+" 副窗口-"+ CommonUtil.windowNum++);
        //禁止窗口缩放
        this.primaryStage.setResizable(false);
        this.primaryStage.setWidth(607);
        this.primaryStage.setHeight(535);
        CommonUtil.setNewWindowCoordinate(this.primaryStage);
        this.primaryStage.setOnCloseRequest(event -> {
            log.debug("on primaryStage close");
            CommonUtil.primaryStage_x -= 30.0;
            CommonUtil.primaryStage_y -= 30.0;
            CommonUtil.windowNum--;
            CommonUtil.unbindData(this);
//            event.consume();//使用此方法可以阻止窗口关闭
        });
        initRootLayout();
        showCommonDeployPage();
        if (properties != null){
            OpcUaProperties.setProperties(this,properties);
            showBuildProfilePage(this);
        }
    }
    /**
     * 在根布局中显示通用项配置页
     */
    public void showCommonDeployPage() {
        OpcUaProperties.bindProperties(this);
        TransportData data = CommonUtil.STAGE_DATA_MAP.get(this);
        data.getSave().setDisable(true);
        data.getSaveAs().setDisable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/CommonDeployPage.fxml"));
        try {
            AnchorPane commonDeployPage = loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(commonDeployPage);

            // Give the controller access to the main app.
            CommonDeployController commonDeployController = loader.getController();
            commonDeployController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示根布局
     */
    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
        try {
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            ThemeUtil.setMainTheme(rootLayout);
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBuildProfilePage(MainApp mainApp){
        TransportData data = CommonUtil.STAGE_DATA_MAP.get(mainApp);
        MenuItem save = data.getSave();
        MenuItem saveAs = data.getSaveAs();
        save.setDisable(false);
        saveAs.setDisable(false);
        BorderPane rootLayout = mainApp.getRootLayout();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/BuildProfilePage.fxml"));
        try {
            AnchorPane buildProfilePage = loader.load();
            rootLayout.setCenter(buildProfilePage);
            BuildProfileController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void myLaunch(String... args){
        launch(args);
    }

    /**
     * 从注册表中获取文件的默认保存路径
     * @return
     */
    public File getFilePath(){
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String lastFile = prefs.get("lastFile",null);
        if (lastFile != null){
            File file = new File(lastFile);
            //Runtime.getRuntime().exec("chmod 777 /home/test3.txt");
            file.setExecutable(true);//设置可执行权限
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
            return file;
        }else {
            return null;
        }
    }

    /**
     * 设置默认文件保存路径到注册表中
     * @param file
     */
    public void setFilePath(File file){
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null){
            prefs.put("lastFile",file.getAbsolutePath());
            primaryStage.setTitle(CommonUtil.primary_stage_title_prefix+" - "+file.getAbsolutePath());
        }else {
            prefs.remove("lastFile");
            primaryStage.setTitle(CommonUtil.primary_stage_title_prefix);
        }
    }
}
