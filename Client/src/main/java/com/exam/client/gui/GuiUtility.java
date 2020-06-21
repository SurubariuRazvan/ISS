package com.exam.client.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GuiUtility {
    public static void showError(StackPane rootPane, Node secondaryPane, String title, String message) {
        VBox dialogLayout = new VBox();
        dialogLayout.getStyleClass().add("showError");
        Label errorHeading = new Label(title);
        errorHeading.getStyleClass().add("errorHeading");
        dialogLayout.getChildren().add(errorHeading);
        Label errorBody = new Label(message);
        errorBody.getStyleClass().add("errorBody");
        dialogLayout.getChildren().add(errorBody);

        Button button = new Button("OK");
        dialogLayout.getChildren().add(button);

        BoxBlur blur = new BoxBlur(3, 3, 2);
        secondaryPane.setEffect(blur);
        rootPane.getChildren().add(dialogLayout);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            rootPane.getChildren().remove(dialogLayout);
            secondaryPane.setEffect(null);
        });
    }

    public static void addTextLimiter(TextField tf, int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength)
                tf.setText(tf.getText().substring(0, maxLength));
        });
    }

    public static void addTextLimiter(TextArea ta, int maxLength) {
        ta.textProperty().addListener((ov, oldValue, newValue) -> {
            if (ta.getText().length() > maxLength)
                ta.setText(ta.getText().substring(0, maxLength));
        });
    }

    public static <EE> void addButtonToTable(TableColumn<EE, Void> tc, String styleClass, Supplier<Node> graphic, BiConsumer<Integer, EE> onAction) {
        tc.setCellFactory(new Callback<>() {
            @Override
            public TableCell<EE, Void> call(final TableColumn<EE, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button(" ");

                    {
                        btn.setGraphic(graphic.get());
                        btn.getStyleClass().add(styleClass);
                        btn.setContentDisplay(ContentDisplay.CENTER);
                        btn.setOnAction((ActionEvent event) -> {
                            EE entity = getTableView().getItems().get(getIndex());
                            onAction.accept(getIndex(), entity);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });
    }

    public static void initSpinner(Spinner<Integer> s, Integer start, Integer end) {
        s.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(start, end, start));
        s.getEditor().setAlignment(Pos.CENTER);
        s.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                try {
                    Integer.parseInt(s.getEditor().textProperty().get());
                } catch (NumberFormatException e) {
                    s.getEditor().textProperty().set(start.toString());
                }
        });
    }

    public static void initSpinner(Spinner<Double> s, Double start, Double end) {
        s.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(start, end, start));
        s.getEditor().setAlignment(Pos.CENTER);
        s.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                try {
                    Integer.parseInt(s.getEditor().textProperty().get());
                } catch (NumberFormatException e) {
                    s.getEditor().textProperty().set(start.toString());
                }
        });
    }
}
