package com.shdq.OPCUA_YML_gen_platform.model;

import javafx.scene.control.MenuItem;
import lombok.Data;


/**
 * @author shdq-fjy
 */
@Data
public class TransportData {

    private MenuItem save;
    private MenuItem saveAs;
    private OpcUaProperties properties;

    public TransportData(MenuItem save,MenuItem saveAs) {
        this.save = save;
        this.saveAs = saveAs;
    }
}
