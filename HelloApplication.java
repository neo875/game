package com.example.demo9;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class HelloApplication extends Application {

    private Label questionLabel;
    private ImageView imageView;
    private Button[] optionButtons;
    private Button nextButton;
    private Label feedbackLabel;
    private Label timerLabel;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Timeline timer;
    private int timeRemaining;

    public static final String[] Questions = {
            "What is the capital of Lesotho?",
            "What is the highest point in Lesotho?",
            "Which river forms part of the border between Lesotho and South Africa?",
            "What are colours on Lesotho flag?",
            "How many districts are in Lesotho?"
    };

    public static final String[] ImagePaths = {
            "/com/example/demo9/mas.jpg",
            "/com/example/demo9/thaba.jpg",
            "/com/example/demo9/mohokare.jpg",
            "/com/example/demo9/flag.jpg",
            "/com/example/demo9/dis.jpg"
    };

    public static final String[][] Options = {
            {"A) Maseru", "B) Leribe", "C) Mafeteng", "D) Quthing"},
            {"A) Thabana Ntlenyana", "B) Mount Qiloane", "C) Mount Mafadi", "D) Mount Afadja"},
            {"A) Orange River", "B) Limpopo River", "C) Congo River", "D) Zambezi River"},
            {"A) Blue White Green", "B) Blue Red Blue", "C) Black Green", "D) White Black Red"},
            {"A) 5", "B) 10", "C) 13", "D) 7"}
    };

    public static final char[] Answers = {'A', 'A', 'A', 'A', 'B'};

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: skyblue; -fx-border-radius: 10px;");

        timerLabel = new Label();

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);

        questionLabel = new Label();
        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        feedbackLabel = new Label();
        nextButton = new Button("Next Question");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-border-color: #008CBA; -fx-color: white; -fx-font-size: 14px; -fx-border-radius: 10px; -fx-padding: 2px 4px; -fx-text-fill: white;");
        nextButton.setDisable(true);

        centerBox.getChildren().addAll(questionLabel, imageView, feedbackLabel, nextButton);
        root.setCenter(centerBox);

        optionButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new Button();
            int finalI = i; // Need to make variable final for lambda
            optionButtons[i].setOnAction(event -> evaluateAnswer(finalI));
            optionButtons[i].setStyle("-fx-background-color: #4CAF50; -fx-border-color: #008CBA; -fx-color: white; -fx-font-size: 12px; -fx-border-radius: 10px; -fx-padding: 4px 6px; -fx-text-fill: white;");
        }

        VBox rightBox = new VBox(10);
        rightBox.getChildren().addAll(optionButtons);
        root.setRight(rightBox);

        nextButton.setOnAction(event -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < Questions.length) {
                showQuestion(currentQuestionIndex);
                nextButton.setDisable(true);
                feedbackLabel.setText("");
            } else {
                endGame();
            }
        });

        timeRemaining = 20; // 20 seconds per question
        timerLabel.setText("Time remaining: " + timeRemaining + " seconds");
        timer = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeRemaining--;
                timerLabel.setText("Time remaining: " + timeRemaining + " seconds");
                if (timeRemaining <= 0) {
                    timer.stop();
                    nextQuestion();
                }
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        root.setLeft(timerLabel);




        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Lesotho Quiz");
        stage.setScene(scene);
        stage.show();

        showQuestion(currentQuestionIndex);
        startTimer();
    }

    private void startTimer() {
        timeRemaining = 20;
        timer.play();
    }


    private void showQuestion(int index) {
        questionLabel.setText(Questions[index]);
        String imagePath = ImagePaths[index];

        try {
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                imageView.setImage(image);
                imageView.setFitWidth(300); // Adjust width as needed
                imageView.setPreserveRatio(true);
            } else {
                System.err.println("Error: Image not found - " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(Options[index][i]);
        }
    }

    private void evaluateAnswer(int selectedIndex) {
        char correctAnswer = Answers[currentQuestionIndex];
        char selectedAnswer = (char) ('A' + selectedIndex);
        if (selectedAnswer == correctAnswer) {
            score++;
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Incorrect! The correct answer is: " + correctAnswer);
        }
        nextButton.setDisable(false);
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < Questions.length) {
            showQuestion(currentQuestionIndex);
            nextButton.setDisable(true);
            feedbackLabel.setText("");
            startTimer();
        } else {
            endGame();
        }
    }

    private void endGame() {
        questionLabel.setText("Quiz Over! Your Score: " + score + " / " + Questions.length);
        for (Button optionButton : optionButtons) {
            optionButton.setDisable(true);
        }
        nextButton.setDisable(true);
    }

    public static void main(String[] args) {
        launch();
    }
}