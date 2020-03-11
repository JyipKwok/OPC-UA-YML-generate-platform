package com.shdq.OPCUA_YML_gen_platform.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * 自定义系统托盘
 * @author shdq-fjy
 */
public class MySysTray {
    private TrayIcon trayIcon = null;
    private Timeline timeline = new Timeline();
    private Stage stage;

    public MySysTray(Stage stage) {
        this.stage = stage;
    }

    public void initSystemTray() {
        // 1、创建托盘按钮
        PopupMenu popupMenu = new PopupMenu();
        MenuItem showItem = new MenuItem("show"); popupMenu.add(showItem);
        MenuItem hideItem = new MenuItem("hidden"); popupMenu.add(hideItem);
        MenuItem quitItem = new MenuItem("exit"); popupMenu.add(quitItem);

        // 2、创建动作事件监听器（awt的古老操作）
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // 多次使用显示和隐藏设置false
                Platform.setImplicitExit(false);

                // 获得按钮并执行相应操作
                MenuItem item = (MenuItem) e.getSource();
                if (item.getLabel().equals("exit")) {
                    // 移除托盘图标
                    SystemTray.getSystemTray().remove(trayIcon);
                    // 关闭应用
                    Platform.exit();
                    // 延迟500毫秒关闭进程
                    Timeline timeline = new Timeline();
                    timeline.setCycleCount(1);
                    timeline.setAutoReverse(false);
                    KeyFrame keyFrame = new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            // 彻底退出进程
                            System.exit(0);
                        }
                    });
                    timeline.getKeyFrames().clear();
                    timeline.getKeyFrames().add(keyFrame);
                    timeline.play();
                }

                else if (item.getLabel().equals("show")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }

                else if (item.getLabel().equals("hidden")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.hide();
                        }
                    });
                }
            }
        };
        // 3、给按钮添加动作事件监听
        showItem.addActionListener(actionListener);
        quitItem.addActionListener(actionListener);
        hideItem.addActionListener(actionListener);

        try {
            File file = new File("src/main/resources/images/tray.png");
            // 4、我们的托盘图标
            trayIcon = new TrayIcon(ImageIO.read(file), "OPC UA YML Generate Platform", popupMenu);
            // 5、鼠标悬浮时的提示信息
            trayIcon.setToolTip("OPC UA YML Generate Platform");
            // 6、添加到系统托盘
            SystemTray.getSystemTray().add(trayIcon);
            // 7、给托盘图标添加鼠标监听
            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 多次使用显示和隐藏设置false
                    Platform.setImplicitExit(false);
                    // 鼠标单击一次
                    if (e.getClickCount() == 2) {
                        timeline.stop();
                        timeline.setCycleCount(1);
                        timeline.setAutoReverse(false);
                        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                if (stage.isShowing()) {
                                    stage.hide();
                                }else{
                                    stage.show();
                                }
                            }
                        });
                        timeline.getKeyFrames().clear();
                        timeline.getKeyFrames().add(keyFrame);
                        timeline.play();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
