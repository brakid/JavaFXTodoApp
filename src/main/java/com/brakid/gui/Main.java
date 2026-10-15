package com.brakid.gui;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.layout.BorderStrokeStyle.SOLID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brakid.logic.TodoManager;
import com.brakid.types.Todo;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private final static TodoManager TODO_MANAGER = new TodoManager();

    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("JavaFX GUI Application");
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        final ObservableList<Text> todos
                = observableArrayList(TODO_MANAGER.getAll().stream()
                        .map(Main::convertTodoToText)
                        .collect(toList()));

        Label label = new Label("TODO List App");
        label.setFont(Font.font(18));

        TextField newTodoText = new TextField();
        newTodoText.setMinWidth(300);
        newTodoText.setMaxWidth(500);
        newTodoText.setPrefWidth(300);
        newTodoText.setAlignment(Pos.CENTER_LEFT);
        EventHandler<ActionEvent> newTodoHandler = (event) -> {
            if (newTodoText.getText().length() > 0) {
                LOGGER.info("New TODO: {}", newTodoText.getText());
                final Todo newTodo = TODO_MANAGER.add(newTodoText.getText());
                newTodoText.setText("");
                todos.add(convertTodoToText(newTodo));
            }
        };
        newTodoText.setOnAction(newTodoHandler);
        Button newTodoButton = new Button("Create TODO");
        newTodoButton.setAlignment(Pos.CENTER_RIGHT);
        newTodoButton.setOnAction(newTodoHandler);
        HBox newTodoLayout = new HBox();
        newTodoLayout.setSpacing(20);
        newTodoLayout.setBorder(new Border(
                new BorderStroke(
                        Color.DARKGRAY,
                        SOLID,
                        CornerRadii.EMPTY,
                        BorderWidths.DEFAULT)));
        newTodoLayout.getChildren().addAll(newTodoText, newTodoButton);

        ListView<Text> todoList = new ListView<>();
        todoList.setItems(todos);
        todoList.setOnMouseClicked(event -> {
            LOGGER.info("Click on: {}", event.getTarget());
            try {
                final Text target = ((Text) event.getTarget());
                final int todoId = Integer.parseInt(target.getId());
                TODO_MANAGER.delete(todoId);
                todos.removeIf(text -> text.getId().equals(target.getId()));
            } catch (ClassCastException ex) {
                LOGGER.info("Click was not targeted at list element");
            }
        });

        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(label, newTodoLayout, todoList);

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        LOGGER.info("All Open TODOs: {}", TODO_MANAGER.getAll());
        TODO_MANAGER.storeTodos();
    }

    private static Text convertTodoToText(Todo todo) {
        final Text todoElement = new Text(todo.toGuiString());
        todoElement.setId("" + todo.id());
        return todoElement;
    }
}
