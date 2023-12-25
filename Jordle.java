//package com.example.jordle2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class Jordle that defines logic for the Jordle application.
 *
 * @author Anastasiya Masalava
 * @version 1.0
 */
public class Jordle extends Application {
    int num3 = 0;

    /**
     * Start method which is overwritten from the Application class.
     *
     * @param primaryStage -- primary stage.
     */
    @Override
    public void start(Stage primaryStage) {
        Random random = new Random();
        int num = random.nextInt(Words.list.size());
        String randomWord = Words.list.get(num);
        BorderPane pane = new BorderPane();
        Pane textPane = new Pane();
        GridPane gridpane = new GridPane();
        Scene scene = new Scene(pane, 500, 500);

        Text text = new Text(190, 50, "JORDLE");
        Font fontBold = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30);
        text.setFont(fontBold);
        text.setFill(Color.MEDIUMSLATEBLUE);
        textPane.getChildren().add(text);
        pane.setTop(textPane);

        Label label = new Label("Try guessing a word!");

        Button restart = new Button("Restart");
        restart.setStyle("-fx-border-color: #7B68EE; -fx-border-width: 1.5px;");
        restart.setOnAction(e -> {
            primaryStage.hide();
            start(new Stage());
        });
        Button instructions = new Button("Instructions");
        instructions.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage instruction = new Stage();
                        VBox vBox = new VBox(20);
                        Text text0 = new Text("INSTRUCTIONS");
                        text0.setFont(fontBold);
                        text0.setFill(Color.MEDIUMSLATEBLUE);
                        Text text1 = new Text(
                                "JORDLE is a fun word game where you have 6 tries to guess a 5-letter word." + "\n"
                                        + "1. Letter appears green if it is in the correct place." + "\n"
                                        + "2. Letter appears yellow if it is in the wrong place." + "\n"
                                        + "3. Letter appears grey if it DOES NOT exist in the word." + "\n"
                                        + "Good Luck!"
                        );
                        text1.setWrappingWidth(300);
                        vBox.getChildren().add(text0);
                        vBox.getChildren().add(text1);
                        vBox.setSpacing(5);
                        vBox.setPadding(new Insets(15, 15, 15, 15));
                        vBox.setAlignment(Pos.CENTER);
                        Scene instructionScene = new Scene(vBox, 400, 200);
                        instruction.setScene(instructionScene);
                        instruction.show();
                    }
                });
        instructions.setStyle("-fx-border-color: #7B68EE; -fx-border-width: 1.5px;");

        HBox paneForBottom = new HBox(20, label, restart, instructions);
        paneForBottom.setAlignment(Pos.CENTER);
        paneForBottom.setPadding(new Insets(15, 15, 15, 15));
        pane.setBottom(paneForBottom);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                gridpane.add(Jordle.newStack("", Color.THISTLE), col, row);
            }
        }
        AtomicInteger col = new AtomicInteger(0);
        AtomicInteger row = new AtomicInteger(0);
        AtomicReference<String> word = new AtomicReference<>("");
        scene.setOnKeyPressed((KeyEvent e) -> {
            if (col.get() == 5 && e.getCode() != KeyCode.ENTER && row.get() != 6) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Press Enter to check your guess!");
                alert.show();
            } else if (row.get() == 6) {
                label.setText(String.format("Game over. The word was %s.", randomWord));
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Your don't have any more tries! The game is over.");
                alert.show();
            } else if (e.getCode().isLetterKey()) {
                gridpane.add(Jordle.newStack(e.getText(), Color.THISTLE), col.get(), row.get());
                word.set(word.get() + e.getText());
                col.getAndIncrement();
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                col.getAndDecrement();
                if (col.get() < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Your cannot delete letters from the previous row!");
                    alert.show();
                    col.getAndIncrement();
                } else {
                    gridpane.add(Jordle.newStack("", Color.THISTLE), col.get(), row.get());

                }
            } else if (e.getCode() == KeyCode.ENTER) {
                if (word.get().length() < 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Your word less has 5 letters.");
                    alert.show();
                }
                if (row.get() == 5) {
                    if (word.get().equals(randomWord)) {
                        label.setText("Congratulations! You’ve guessed the word!");
                    } else {
                        label.setText(String.format("Game over. The word was %s.", randomWord));
                    }
                }
                if (word.get().length() == 5) {
                    String word2 = word.get();
                    if (word2.equals(randomWord)) {
                        label.setText("Congratulations! You’ve guessed the word!");
                    }
                    for (int i = 0; i < word2.length(); i++) {
                        if (word2.charAt(i) == randomWord.charAt(i)) {
                            gridpane.add(Jordle.newStack(String.valueOf(word2.charAt(i)), Color.MEDIUMSEAGREEN), i,
                                    row.get());
                        }
                        if (word2.charAt(i) != randomWord.charAt(i)
                                && randomWord.contains(String.valueOf(word2.charAt(i)))) {
                            gridpane.add(Jordle.newStack(String.valueOf(word2.charAt(i)), Color.GOLDENROD), i,
                                    row.get());
                        }
                        if (word2.charAt(i) != randomWord.charAt(i)
                                && !randomWord.contains(String.valueOf(word2.charAt(i)))) {
                            gridpane.add(Jordle.newStack(String.valueOf(word2.charAt(i)), Color.LIGHTGRAY), i,
                                    row.get());
                        }
                    }
                    word.set("");
                    row.getAndIncrement();
                    col.set(0);
                }
            }
        });

        gridpane.setVgap(5);
        gridpane.setHgap(5);
        gridpane.setAlignment(Pos.CENTER);
        pane.setCenter(gridpane);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Jordle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * A helper method that creates a stackpane with a rectangle and text.
     *
     * @param letter -- letter to put into cell.
     * @param color  -- color of a cell.
     * @return StackPane.
     */
    public static StackPane newStack(String letter, Color color) {
        StackPane stackPane = new StackPane();
        Text newText = new Text(letter);
        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setFill(color);
        stackPane.getChildren().addAll(rectangle, newText);
        return stackPane;
    }

    /**
     * Main method.
     *
     * @param args --array of Strings.
     */
    public static void main(String[] args) {
        launch(args);
    }
}