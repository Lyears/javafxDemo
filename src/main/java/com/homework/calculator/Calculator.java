package com.homework.calculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author fzm
 */
public class Calculator extends Application {
    private String[][] labels = {{"1", "2", "3", "+"}, {"4", "5", "6", "-"}, {"7", "8", "9", "*"}, {"0", "CLR", "=", "/"}};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10, 10, 0, 10));
        Scene scene = new Scene(pane, 400, 200);

        TextField textField = new TextField();
        textField.setPrefColumnCount(15);
        pane.getChildren().add(textField);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                Button button = new Button(labels[i][j]);
                button.setOnAction(new ButtonAction(button, textField));
                button.setPrefWidth(87.5d);
                gridPane.add(button, j, i);
            }
        }

        pane.getChildren().add(gridPane);
        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private class ButtonAction implements EventHandler<ActionEvent> {

        private Button button;
        private TextField textField;

        ButtonAction(Button button, TextField textField) {
            this.button = button;
            this.textField = textField;
        }

        @Override
        public void handle(ActionEvent event) {
            String display = textField.getText();
            switch (button.getText()) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "0":
                    if ("0".equals(display)) {
                        if ("0".equals(button.getText())) {
                            break;
                        } else {
                            textField.setText(button.getText());
                            break;
                        }
                    } else {
                        textField.setText(display + button.getText());
                        break;
                    }
                case "CLR":
                    textField.setText("0");
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                    textField.setText(display + button.getText());
                    break;
                case "=":
                    ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
                    String result;
                    try {
                        result = jse.eval(textField.getText()).toString();
                        textField.setText(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
