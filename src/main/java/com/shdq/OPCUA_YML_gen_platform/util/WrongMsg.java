package com.shdq.OPCUA_YML_gen_platform.util;

import java.util.regex.Pattern;

public class WrongMsg {

    public static final String Validation_Error = "校验错误！";
    public static final String Non_Standard_Input = "输入的内容不规范！";
    public static final String listenerPath_MUST_NOT_NULL = "监听器路径不能为空。\n";
    public static final String nodesParser_MUST_NOT_NULL = "节点解析器不能为空。\n";
    public static final String publishRate_MUST_NOT_NULL = "刷新率不能为空。\n";
    public static final String listenerPath_MUST_END_WITH_DOT = "监听器路径必须以点“.”结尾。\n";
    public static final String publishRate_MUST_BE_INTEGER = "刷新率必须是整数。\n";
    public static final String address_MUST_NOT_NULL = "address不能为空。\n";
    public static final String plcNo_MUST_BE_INTEGER = "plcNo必须是整数。\n";
    public static final String ns_MUST_BE_INTEGER = "ns必须是整数。\n";
    public static final String clientListener_MUST_NOT_NULL = "clientListener不能为空。\n";
    public static final String sessionTimeOut_MUST_BE_INTEGER = "sessionTimeOut必须是整数。\n";
    public static final String password_MUST_NOT_NULL = "password不能为空。\n";
    public static final String username_MUST_NOT_NULL = "username不能为空。\n";
    public static final String listener_name_and_nodeStr_MUST_NOT_BE_NULL = "监听器名称和监听节点字符串不能为空。\n";
    public static final String regex = "^-?[1-9]\\d*$";
    public static final Pattern pattern = Pattern.compile(regex);
    public static final String PLEASE_CORRECT_NON_STANDARD_INPUT_FIRST = "请先修正不符合校验的输入！";
}
