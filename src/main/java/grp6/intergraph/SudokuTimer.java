package grp6.intergraph;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class SudokuTimer {
    private static Label timerLabel;
    private static Timeline timeline;
    private static int elapsedTime;
    private static int bestTime;
    private static boolean isPaused;  // Variable pour savoir si le timer est en pause

    public SudokuTimer(int bestTime) {
        this.bestTime = bestTime;
        this.elapsedTime = 0;
        this.timerLabel = new Label("00:00");
        this.isPaused = false; // Initialiser en non-paused
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public HBox getTimerDisplay() {
        HBox timerBox = new HBox(10);
        timerBox.getChildren().add(timerLabel);
        return timerBox;
    }

    public static void startTimer() {
        elapsedTime = 0;
        isPaused = false;  // Assurer que le chrono démarre sans être en pause
        updateLabel();
        timeline.play();
    }

    public static void stopTimer() {
        timeline.stop();
    }

    public static void resetTimer() {
        stopTimer();
        elapsedTime = 0;
        updateLabel();
        startTimer();
    }

    // Fonction pour mettre en pause le timer
    public void pauseTimer() {
        if (!isPaused) {
            timeline.pause();
            isPaused = true;
        }
    }

    // Fonction pour reprendre le timer
    public static void resumeTimer() {
        if (isPaused) {
            timeline.play();
            isPaused = false;
            updateLabel();
        }
    }

    private static void updateTimer() {
        elapsedTime++;
        updateLabel();
    }

    private static void updateLabel() {
        timerLabel.setText(formatTime(elapsedTime));
    }

    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", minutes, sec);
    }
}