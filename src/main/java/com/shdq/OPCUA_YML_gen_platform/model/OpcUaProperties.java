package com.shdq.OPCUA_YML_gen_platform.model;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import javafx.stage.Stage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class OpcUaProperties {

    //监听器路径
    private String listenerPath;

    //节点解析器
    private String nodesParser;

    //推送响应速率
    private long publishRate;

    //对应的opc 站点队列
    private List<Map<String, String>> plcList;

    private OpcUaProperties() {
        plcList = new ArrayList<>();
    }

//    private static class SingletonHolder{
//        private static OpcUaProperties properties = new OpcUaProperties();
//    }
//
//    public static OpcUaProperties getInstance(){
//        return SingletonHolder.properties;
//    }

    public static OpcUaProperties getProperties(MainApp mainApp){
        return CommonUtil.STAGE_DATA_MAP.get(mainApp).getProperties();
    }

    public static void bindProperties(MainApp mainApp){
        CommonUtil.STAGE_DATA_MAP.get(mainApp).setProperties(new OpcUaProperties());
    }

    public static void setProperties(MainApp mainApp,OpcUaProperties properties){
        CommonUtil.STAGE_DATA_MAP.get(mainApp).setProperties(properties);
    }
}
