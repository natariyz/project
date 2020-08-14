package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private Timer timer;
    private TimerTaskSergey timerTaskSergey;

    @Override
    public void start(Stage primaryStage) {
        javafx.scene.control.Label secLabel = new javafx.scene.control.Label("Sec");
        javafx.scene.control.Label minLabel = new javafx.scene.control.Label("Min");

        TextField sec = new TextField();
        sec.setText("0");
        sec.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue && sec.getText().equals("")){
                sec.setText("0");
            }
        });
        sec.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    sec.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        TextField min = new TextField();
        min.setText("10");
        min.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!min.isFocused() && min.getText().equals("")){
                min.setText("10");
            }
        });
        min.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    min.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            timerTaskSergey = new TimerTaskSergey();
            timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTaskSergey,
                    Integer.parseInt(sec.getText()) * 1000 + Integer.parseInt(min.getText()) * 60 * 1000,
                    Integer.parseInt(sec.getText()) * 1000 + Integer.parseInt(min.getText()) * 60 * 1000);
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> {
            timer.cancel();
            timerTaskSergey.cancel();
            Platform.exit();
            System.exit(0);
        });

        VBox root = new VBox();

        root.getChildren().addAll(secLabel, sec, minLabel, min, startButton, exitButton);

        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Timer");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public class TimerTaskSergey extends TimerTask {

        @Override
        public void run() {
            System.out.println("TimerTask начал свое выполнение в:" + new Date());
            try {
                completeTask();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            System.out.println("TimerTask закончил свое выполнение в:" + new Date());
        }

        private void completeTask() throws AWTException {
            Media hit = new Media(new File("timer.mp3").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();

            PopupMenu popup = new PopupMenu();
            MenuItem exitItem = new MenuItem("Выход");
            exitItem.addActionListener(e -> System.exit(0));

            popup.add(exitItem);
            SystemTray systemTray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("image.png");
            TrayIcon trayIcon = new TrayIcon(image, "Hi", popup);
            trayIcon.setImageAutoSize(true);
            systemTray.add(trayIcon);
            trayIcon.displayMessage("Timer", "Timer", TrayIcon.MessageType.INFO);
        }
    }
}
