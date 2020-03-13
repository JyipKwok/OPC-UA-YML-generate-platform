package com.shdq.OPCUA_YML_gen_platform.animation;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MyAnimation {

    public static List<ParallelTransition> commonPageEffectsPlay(List<Node> nodes,List<Label> labels){
        List<ParallelTransition> result = new ArrayList<>();
        List<ParallelTransition> parallelTransitions = TextFieldEffects.set(nodes);
        List<SequentialTransition> sequentialTransitions = LabelEffects.set(labels);
        for (int i = 0; i < parallelTransitions.size(); i++) {
            ParallelTransition  parallelTransition = parallelTransitions.get(i);
            SequentialTransition sequentialTransition = sequentialTransitions.get(i);
            ParallelTransition parallelTransition1 = new ParallelTransition(parallelTransition,sequentialTransition);
            parallelTransition1.setCycleCount(1);
            parallelTransition1.setAutoReverse(false);
            result.add(parallelTransition1);
        }
        return result;
    }

    public static List<ParallelTransition> detailPageEffectsPlay(List<Node> nodes, List<Label> labels){
        List<ParallelTransition> result = new ArrayList<>();
        List<ParallelTransition> parallelTransitions = TextFieldEffects.set(nodes);
        List<SequentialTransition> sequentialTransitions = LabelEffects.set(labels);
        for (int i = 0; i < parallelTransitions.size(); i++) {
            ParallelTransition parallelTransition;
            SequentialTransition sequentialTransition;
            if (i == 9 || i ==11){
                parallelTransition = new ParallelTransition(parallelTransitions.get(i),parallelTransitions.get(i+1));
                if (i == 9){
                    sequentialTransition = sequentialTransitions.get(9);
                }else {
                    sequentialTransition = sequentialTransitions.get(10);
                }
            }else if (i == 10 || i ==12){
                break;
            }else {
                parallelTransition = parallelTransitions.get(i);
                sequentialTransition = sequentialTransitions.get(i);
            }
            ParallelTransition parallelTransition1 = new ParallelTransition(parallelTransition,sequentialTransition);
            parallelTransition1.setCycleCount(1);
            parallelTransition1.setAutoReverse(false);
            result.add(parallelTransition1);
        }
        return result;
    }

    private static class TextFieldEffects {
        private static List<ParallelTransition> set(List<Node> nodes){
            return useTransition(nodes);
        }

        /**
         * 使用过渡动画
         */
        private static List<ParallelTransition> useTransition(List<Node> nodes){
            List<ParallelTransition> parallelTransitions = new ArrayList<>();
            nodes.forEach(node -> {
                //淡入效果
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(2500),node);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.setCycleCount(1);
                fadeTransition.setAutoReverse(false);
//        fadeTransition.play();
                //平移效果
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000),node);
                if (node.getLayoutX() == 0){
                    translateTransition.setFromX(400);
                }else {
                    translateTransition.setFromX(node.getLayoutX());
                }
                translateTransition.setToX(0);
                translateTransition.setCycleCount(1);
                translateTransition.setAutoReverse(false);
//        translateTransition.play();
                //旋转效果
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000),node);
                rotateTransition.setByAngle(360);
                rotateTransition.setCycleCount(1);
                rotateTransition.setAutoReverse(false);
//        rotateTransition.play();
                //缩放效果
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000),node);
                scaleTransition.setToX(2);
                scaleTransition.setToY(2);
                scaleTransition.setCycleCount(1);
                scaleTransition.setAutoReverse(false);
//        scaleTransition.play();
                //并行执行动画
                ParallelTransition parallelTransition = new ParallelTransition(fadeTransition,translateTransition);
                parallelTransition.setCycleCount(1);

                parallelTransitions.add(parallelTransition);
//        //顺序执行动画
//        SequentialTransition sequentialTransition = new SequentialTransition(fadeTransition,rotateTransition,translateTransition,scaleTransition);
//        scaleTransition.setCycleCount(Timeline.INDEFINITE);
//        sequentialTransition.play();
            });
            return parallelTransitions;
        }

        /**
         * 使用时间轴动画
         */
        private static void useTimeLine(List<Node> nodes){
            nodes.forEach(node -> {
                //平移
                KeyValue xValue1 = new KeyValue(node.layoutXProperty(),-280);
                KeyValue yValue = new KeyValue(node.layoutYProperty(),node.getLayoutY());
                KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO,xValue1,yValue);
                KeyValue xValue2 = new KeyValue(node.layoutXProperty(),node.getLayoutX());
                KeyFrame keyFrame2 = new KeyFrame(Duration.millis(1000),xValue2,yValue);
                //不透明度
                KeyValue opacityValue = new KeyValue(node.opacityProperty(),0);
                KeyFrame keyFrame3 = new KeyFrame(Duration.ZERO,opacityValue);
                KeyFrame keyFrame4 = new KeyFrame(Duration.millis(1000),new KeyValue(node.opacityProperty(),1));
                Timeline timeline1 = new Timeline();
                timeline1.getKeyFrames().addAll(keyFrame1,keyFrame2,keyFrame3,keyFrame4);
                timeline1.play();
            });
        }
    }

    private static class LabelEffects{
        private static List<SequentialTransition> set(List<Label> labels){
            List<SequentialTransition> sequentialTransitions = new ArrayList<>();
            labels.forEach(label -> {
                SequentialTransition sequentialTransition = new SequentialTransition();
                sequentialTransition.setCycleCount(1);
                sequentialTransition.setAutoReverse(false);
                double height = 235;
                double maxDuration = 800;
                for (double i = 1 ; i <= 10 ; i++){
                    TranslateTransition down = new TranslateTransition(Duration.millis(maxDuration/i),label);
                    down.setFromX(height*-1);
                    down.setToX(0);
                    down.setCycleCount(1);
                    down.setAutoReverse(false);

                    height = height / (i+1.0);
                    TranslateTransition up = new TranslateTransition(Duration.millis(maxDuration/(i+1.0)),label);
                    up.setFromX(0);
                    up.setToX(height*-1);
                    up.setCycleCount(1);
                    up.setAutoReverse(false);
                    sequentialTransition.getChildren().addAll(down,up);
                }
                sequentialTransitions.add(sequentialTransition);
            });
            return sequentialTransitions;
        }
    }
}
