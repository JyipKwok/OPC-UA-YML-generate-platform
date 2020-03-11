package com.shdq.OPCUA_YML_gen_platform.concurrent;

import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.YamlConverter;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MyTask extends Task<Integer> {

    private String filePath;

    private OpcUaProperties properties;

    private boolean isRecord;

    private Stage progressBarStage;

    public MyTask(String filePath, OpcUaProperties properties, boolean isRecord, Stage progressStage) {
        this.filePath = filePath;
        this.properties = properties;
        this.isRecord = isRecord;
        this.progressBarStage = progressStage;
    }

    @Override
    protected Integer call() throws Exception {
        log.debug("task start");
        updateMessage("正在准备创建...");
        updateValue(0);
        Thread.sleep(2000);
        try {
            YamlConverter.getInstance().writeToYAMLFile(filePath, properties);
            int i = 0;
            while (i++ < 100) {
                updateProgress(i, 100);
                updateMessage("正在创建，已完成：" + i + "% ！");
                updateValue(i);
                Thread.sleep(50);
            }
            updateMessage("创建成功！");
            Thread.sleep(1000);
            log.debug("task complete");
            return 1;
        } catch (Exception e) {
            updateMessage("创建出错！");
            updateValue(-1);
            updateProgress(-1, 1);
            Thread.sleep(1000);
            log.debug("task exception");
            return -1;
        }
    }

    @Override
    protected void running() {
        super.running();
    }

    @Override
    protected void scheduled() {
        super.scheduled();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        progressBarStage.close();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        progressBarStage.close();
    }

    @Override
    protected void failed() {
        super.failed();
        progressBarStage.close();
    }

    @Override
    protected void updateProgress(long workDone, long max) {
        super.updateProgress(workDone, max);
    }

    @Override
    protected void updateMessage(String message) {
        super.updateMessage(message);
    }

    @Override
    protected void updateTitle(String title) {
        super.updateTitle(title);
    }

    @Override
    protected void updateValue(Integer value) {
        super.updateValue(value);
    }
}
