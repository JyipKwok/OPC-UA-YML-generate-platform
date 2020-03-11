package com.shdq.OPCUA_YML_gen_platform.util;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.TransportData;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * @author shdq-fjy
 */
public class CommonUtil {
    public volatile static boolean isOccupation = false;
    private static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    public static final List<String> notListenerNames = Arrays.asList(
            "address", "username", "password", "isConnect",
            "isSubscribe", "plcNo", "ns", "securityMode",
            "userAuthenticationMode", "sessionTimeOut",
            "certificateFileOrURL", "privateKeyFileOrURL",
            "privateKeyPassword", "clientListener");
    public static int windowNum = 1;
    public static double primaryStage_x;
    public static double primaryStage_y;
    //多任务，多窗口，保存每一个窗口主stage和与之对应的数据
    public static final Map<MainApp, TransportData> STAGE_DATA_MAP = new HashMap<>(10);
    public static final String default_file_path = "OPC UA YML PLATFORM";
    public static final String default_file_name = "opcua";
    public static final String default_file_suffix = ".yml";
    public static final String history_record_file_path = "OPC UA YML PLATFORM" + File.separator + "history";
    public static final String primary_stage_title_prefix = "OPC UA YML generate platform";
    public static final String image_url = "/images/platform.png";

    public static final String DARK_THEME = "/css/dark/darkTheme.css";
    public static final String WHITE_THEME = "/css/white/WhiteTheme.css";

    public enum Theme {
        DARK, WHITE, DEFAULT;

        Theme() {
        }
    }

    public static final String THEME_KEY = "theme";
    public static final String DEFAULT_FILE_PATH_KEY = "filePath";
    //保存，当前页面的pane，在切换主题时设置当前pane主题
    public static Pane current_page_pane;

    static {
        File file = new File(history_record_file_path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void unbindData(MainApp mainApp){
        STAGE_DATA_MAP.remove(mainApp);
    }

    public static void bindData(MainApp mainApp,TransportData data){
        STAGE_DATA_MAP.put(mainApp,data);
    }

    public static String getDataFromPreferences(String key) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String value = prefs.get(key, null);
        return value;
    }

    public static void setDataToPreferences(String key, String value) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put(key, value);
    }

    public static void setMainWindowCoordinate(Stage primaryStage){
        primaryStage_x = (screenBounds.getWidth() - primaryStage.getWidth()) / 2;
        primaryStage_y = (screenBounds.getHeight() - primaryStage.getHeight()) / 2;
        primaryStage.setX(primaryStage_x);
        primaryStage.setY(primaryStage_y);
    }

    public static void setNewWindowCoordinate(Stage stage){
        primaryStage_x += 30;
        primaryStage_y += 30;
        stage.setX(primaryStage_x);
        stage.setY(primaryStage_y);
    }

    public static void setDialogWindowCoordinate(Stage primaryStage,Stage dialogStage){
        dialogStage.setX(primaryStage.getX()+30);
        dialogStage.setY(primaryStage.getY()-10);
    }

    public static void setProgressBarWindowCoordinate(Stage primaryStage,Stage progressStage){
        progressStage.setX(primaryStage.getX() + 150);
        progressStage.setY(primaryStage.getY() + 250);
    }
}
