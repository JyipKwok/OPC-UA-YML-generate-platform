package com.shdq.OPCUA_YML_gen_platform;

import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.JavaCMDUtil;

import java.io.File;

public class MainStart {

    public static void main(String[] args) {
        File file = new File(CommonUtil.REG_FILE_PATH);
        if (!file.exists()){
            JavaCMDUtil.runBatWithoutCmd(CommonUtil.BAT_FILE_PATH);
        }
        MainApp.myLaunch(args);
    }
}
