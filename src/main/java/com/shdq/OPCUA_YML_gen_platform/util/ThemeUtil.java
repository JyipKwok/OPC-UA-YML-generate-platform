package com.shdq.OPCUA_YML_gen_platform.util;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import javafx.scene.layout.Pane;

import java.util.Iterator;
import java.util.Set;

import static com.shdq.OPCUA_YML_gen_platform.util.CommonUtil.THEME_KEY;

public class ThemeUtil {

    public static void loadTheme(CommonUtil.Theme theme){
        CommonUtil.setDataToPreferences(THEME_KEY, String.valueOf(theme));
        Set<MainApp> set = CommonUtil.STAGE_DATA_MAP.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            MainApp mainApp = ((MainApp)iterator.next());
            ThemeUtil.setMainTheme(mainApp.getRootLayout());
            mainApp.getPrimaryStage().close();
            mainApp.getPrimaryStage().show();
        }
    }

    public static void setMainTheme(Pane pane){
        String theme = CommonUtil.getDataFromPreferences(THEME_KEY);
        if (theme == null || theme.equals(String.valueOf(CommonUtil.Theme.DEFAULT))) {
            pane.getStylesheets().clear();
        } else if (theme.equals(String.valueOf(CommonUtil.Theme.DARK))) {
            pane.getStylesheets().add(MainApp.class.getResource(CommonUtil.DARK_THEME).toExternalForm());
        } else if (theme.equals(String.valueOf(CommonUtil.Theme.WHITE))) {
            pane.getStylesheets().add(MainApp.class.getResource(CommonUtil.WHITE_THEME).toExternalForm());
        }
    }

    public static CommonUtil.Theme getTheme(){
        String theme = CommonUtil.getDataFromPreferences(THEME_KEY);
        if (theme == null || theme.equals(String.valueOf(CommonUtil.Theme.DEFAULT))){
            return CommonUtil.Theme.DEFAULT;
        }else if (theme.equals(String.valueOf(CommonUtil.Theme.DARK))){
            return CommonUtil.Theme.DARK;
        }else if (theme.equals(String.valueOf(CommonUtil.Theme.WHITE))){
            return CommonUtil.Theme.WHITE;
        }
        return CommonUtil.Theme.DEFAULT;
    }
}
