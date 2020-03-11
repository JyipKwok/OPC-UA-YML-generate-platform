package com.shdq.OPCUA_YML_gen_platform.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ListenerPair {

    private final StringProperty id;

    private final StringProperty listenerName;

    private final StringProperty nodeStr;

    public ListenerPair(String id, String listenerName, String nodeStr) {
        this.id = new SimpleStringProperty(id);
        this.listenerName = new SimpleStringProperty(listenerName);
        this.nodeStr = new SimpleStringProperty(nodeStr);
    }

    public ListenerPair() {
        this(null, null,null);
    }

    public String getListenerName() {
        return listenerName.get();
    }

    public String getNodeStr() {
        return nodeStr.get();
    }

    public String getId(){
        return id.get();
    }

    public void setListenerName(String listenerName){
        this.listenerName.set(listenerName);
    }

    public void setNodeStr(String nodeStr){
        this.nodeStr.set(nodeStr);
    }

    public void setId(String id){
        this.id.set(id);
    }

    public StringProperty listenerNameProperty() {
        return listenerName;
    }

    public StringProperty nodeStrProperty() {
        return nodeStr;
    }

    public StringProperty idProperty(){
        return id;
    }
}
