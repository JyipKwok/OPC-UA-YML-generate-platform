package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.animation.MyAnimation;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.WrongMsg;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author shdq-fjy
 */
@Setter
public class CommonDeployController {
    private MainApp mainApp;
    boolean listenerPathInputCorrect = false;
    boolean nodesParserInputCorrect = false;
    boolean publishRateInputCorrect = false;
    @FXML
    private TextField listenerPath;
    @FXML
    private TextField nodesParser;
    @FXML
    private TextField publishRate;
    @FXML
    private Button complete;
    @FXML
    private Label listenerPathLabel;
    @FXML
    private Label nodesParserLabel;
    @FXML
    private Label publishRateLabel;
    @FXML
    private ImageView listenerPathPoint;
    @FXML
    private ImageView nodesParserPoint;
    @FXML
    private ImageView publishRatePoint;

    @FXML
    private void initialize() {
        playEffects();
        setTextFieldFocusedPropertyListener();
        setTextFieldOnKeyPressedEvent();
//        setTextFieldOnKeyTypedEvent();
        listenerPath.setTooltip(new Tooltip("监听器类的包路径，最后以“.”结尾。"));
        nodesParser.setTooltip(new Tooltip("自定义节点解析器包名+类名，用于解析后续配置监听器和监听节点对中，对节点字符串的解析。"));
        publishRate.setTooltip(new Tooltip("监听器刷新率（推送响应速率）。"));
    }

    private void setTextFieldOnKeyTypedEvent() {
        listenerPath.setOnKeyTyped(event -> {
            validateInput("listenerPath", listenerPath.getText());
        });
        nodesParser.setOnKeyTyped(event -> {
            validateInput("nodesParser", nodesParser.getText());
        });
        publishRate.setOnKeyTyped(event -> {
            validateInput("publishRate", publishRate.getText());
        });
    }

    private void setTextFieldOnKeyPressedEvent() {
        listenerPath.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB) || event.getCode().equals(KeyCode.ENTER)) {
                validateInput("listenerPath", listenerPath.getText());
            }
        });
        nodesParser.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB) || event.getCode().equals(KeyCode.ENTER)) {
                validateInput("nodesParser", nodesParser.getText());
            }
        });
        publishRate.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.TAB) || event.getCode().equals(KeyCode.ENTER)) {
                validateInput("publishRate", publishRate.getText());
            }
        });
    }

    private void setTextFieldFocusedPropertyListener() {
        listenerPath.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                validateInput("listenerPath", listenerPath.getText());
            }
        });
        nodesParser.focusedProperty().addListener((observable, oldValue, newValue) -> {
            //从有焦点到失去焦点
            if (oldValue && !newValue) {
                validateInput("nodesParser", nodesParser.getText());
            }
        });
        publishRate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                validateInput("publishRate", publishRate.getText());
            }
        });
    }

    private void playEffects() {
        List<Node> nodes = Arrays.asList(listenerPath, nodesParser, publishRate);
        List<Label> labels = Arrays.asList(listenerPathLabel, nodesParserLabel, publishRateLabel);
        List<ParallelTransition> parallelTransitions = MyAnimation.commonPageEffectsPlay(nodes, labels);
        parallelTransitions.forEach(parallelTransition -> {
            parallelTransition.play();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleCommonDeployComplete() {
        OpcUaProperties properties = OpcUaProperties.getProperties(mainApp);
        properties.setListenerPath(listenerPath.getText());
        properties.setNodesParser(nodesParser.getText());
        properties.setPublishRate(Long.valueOf(publishRate.getText()));
        //todo:点击按钮，加载详情页面可能会卡顿，等待页面的加载时间有点长问题，需要优化，点击按钮后本页面淡出，详情页面淡入，营造视觉效果，增强用户体验

        showDetailDeployPage();
    }

    private void validateInput(String name, String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(WrongMsg.Validation_Error);
        alert.setHeaderText(WrongMsg.Non_Standard_Input);
        if (name.equals("listenerPath")) {
            if (StringUtils.isBlank(str) || !str.endsWith(".")) {
                listenerPathInputCorrect = false;
                listenerPathPoint.setImage(new Image("/images/wrong.png"));
                listenerPathPoint.setVisible(true);
                listenerPathPoint.setDisable(false);
                listenerPathPoint.setCursor(Cursor.HAND);
                listenerPathPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.listenerPath_MUST_NOT_NULL + "并且" + WrongMsg.listenerPath_MUST_END_WITH_DOT);
                    alert.showAndWait();
                });
            } else {
                listenerPathInputCorrect = true;
                listenerPathPoint.setImage(new Image("/images/correct.png"));
                listenerPathPoint.setVisible(true);
                listenerPathPoint.setDisable(true);
                listenerPathPoint.setCursor(Cursor.DEFAULT);
                if (listenerPathInputCorrect && nodesParserInputCorrect && publishRateInputCorrect) {
                    complete.setDisable(false);
                }
            }
        }
        if (name.equals("nodesParser")) {
            if (StringUtils.isBlank(str)) {
                nodesParserInputCorrect = false;
                nodesParserPoint.setImage(new Image("/images/wrong.png"));
                nodesParserPoint.setVisible(true);
                nodesParserPoint.setDisable(false);
                nodesParserPoint.setCursor(Cursor.HAND);
                nodesParserPoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.nodesParser_MUST_NOT_NULL);
                    alert.showAndWait();
                });
            } else {
                nodesParserInputCorrect = true;
                nodesParserPoint.setImage(new Image("/images/correct.png"));
                nodesParserPoint.setVisible(true);
                nodesParserPoint.setDisable(true);
                nodesParserPoint.setCursor(Cursor.DEFAULT);
                if (listenerPathInputCorrect && nodesParserInputCorrect && publishRateInputCorrect) {
                    complete.setDisable(false);
                }
            }
        }
        if (name.equals("publishRate")) {
            if (StringUtils.isBlank(str) || !WrongMsg.pattern.matcher(str).matches()) {
                publishRateInputCorrect = false;
                publishRatePoint.setImage(new Image("/images/wrong.png"));
                publishRatePoint.setVisible(true);
                publishRatePoint.setDisable(false);
                publishRatePoint.setCursor(Cursor.HAND);
                publishRatePoint.setOnMouseClicked(event -> {
                    alert.setContentText(WrongMsg.publishRate_MUST_BE_INTEGER);
                    alert.showAndWait();
                });
            } else {
                publishRateInputCorrect = true;
                publishRatePoint.setImage(new Image("/images/correct.png"));
                publishRatePoint.setVisible(true);
                publishRatePoint.setDisable(true);
                publishRatePoint.setCursor(Cursor.DEFAULT);
                if (listenerPathInputCorrect && nodesParserInputCorrect && publishRateInputCorrect) {
                    complete.setDisable(false);
                }
            }
        }
    }

    /**
     * 通用项配置完成，点击按钮展示服务端的配置界面
     */
    private void showDetailDeployPage() {
        BorderPane rootLayout = mainApp.getRootLayout();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/DetailDeployPage.fxml"));
        try {
            AnchorPane detailDeployPage = loader.load();
            rootLayout.setCenter(detailDeployPage);
            DetailDeployController controller = loader.getController();
            controller.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
