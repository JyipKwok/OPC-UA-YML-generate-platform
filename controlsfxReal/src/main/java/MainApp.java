import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {
    private ProgressIndicator progressBar;
    private Label label;
    private Button button;
    private Stage primaryStage;
    private GridPane grid;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        grid = new GridPane();
        grid.setPadding(new Insets(5, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        button = new Button();
        button.setText("点击开始任务");
        button.setOnMouseClicked(event -> {
            runTask(new MyTask());
            System.out.println("执行任务");
        });
        progressBar = new ProgressBar();
//        progressBar = new ProgressIndicator();
        label = new Label();
        progressBar.setVisible(false);
        progressBar.setDisable(true);
        label.setVisible(false);
        label.setDisable(true);
        grid.addColumn(0,button,progressBar,label);
        GridPane.setMargin(button, new Insets(5, 0, 0, 0));
        GridPane.setMargin(progressBar, new Insets(5, 0, 0, 0));
        GridPane.setMargin(label, new Insets(5, 0, 0, 0));
        grid.setAlignment(Pos.CENTER);
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth() * 0.75);
        primaryStage.setHeight(screenBounds.getHeight() * .75);
        primaryStage.setTitle("Async task");
        primaryStage.show();
    }

    /**
     * 异步执行任务
     * @param task
     */
    private void runTask(Task task) {
        //开始任务前先显示进度条
        progressBar.setVisible(true);
        progressBar.setDisable(false);
        label.setVisible(true);
        label.setDisable(false);
        label.setText("正在准备任务。。。");
        Service<Integer> service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return task;
            }
        };
        progressBar.progressProperty().bind(service.progressProperty());
        task.messageProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(service.getMessage());
            if (service.getValue() == null || (service.getValue() < 100 && service.getValue() >0)){
                label.setTextFill(Color.gray(0.3));
            }else if (service.getValue() == 100){
                label.setTextFill(Color.rgb(0,255,0));
                taskComplete();
            }else {
                label.setTextFill(Color.rgb(255,0,0));
                taskOnError();
            }
        });
        service.restart();
    }

    /**
     * 执行任务出错
     */
    private void taskOnError() {

    }

    /**
     * 完成
     */
    private void taskComplete() {
        progressBar.setDisable(true);
        progressBar.setVisible(false);
        label.setDisable(true);
        label.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class MyTask extends Task{
        @Override
        protected Object call() throws Exception {
            //更新任务信息
            updateMessage("任务开始...");
            //更新任务返回值
            updateValue(0);
            Thread.sleep(2000);
            try {
                //这里使用while循环表示执行的任务，每循环一次更新一次label
                int i = 0;
                while (i++ < 100) {
                    updateProgress(i, 100);
                    updateMessage("任务正在执行，已完成：" + i + "% ！");
                    updateValue(i);
                    Thread.sleep(50);
                }
                updateMessage("创建成功！");
                Thread.sleep(1000);
                return 1;
            } catch (Exception e) {
                updateMessage("任务出错！");
                updateValue(-1);
                updateProgress(-1, 1);
                Thread.sleep(1000);
                return -1;
            }
        }
    }
}
