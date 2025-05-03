import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class TodoListApp extends Application {

    // ObservableList to store tasks
    private ObservableList<String> tasks;
    private ListView<String> listView;

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }

    // Override method to start the JavaFX application
    @Override
    public void start(Stage primaryStage) {
        // Creating the layout elements
        BorderPane root = new BorderPane(); // Root layout
        Scene scene = new Scene(root, 600, 400); // Scene with width and height

        // Initializing the list to hold tasks
        tasks = FXCollections.observableArrayList();

        // Creating a ListView to display tasks
        listView = new ListView<>(tasks);
        listView.setPrefWidth(500);

        // Input field to add tasks
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a task");
        taskInput.setMaxWidth(500);

        // Button to add tasks
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String newTask = taskInput.getText().trim();
            if (!newTask.isEmpty()) {
                tasks.add(newTask);
                taskInput.clear();
            }
        });

        // HBox to hold input field and add button
        HBox inputBox = new HBox(10, taskInput, addButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        // Button to remove selected task
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                tasks.remove(selectedIndex);
            }
        });

        // Button to mark selected task as completed
        Button completeButton = new Button("Complete");
        completeButton.setOnAction(e -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                String task = tasks.get(selectedIndex);
                tasks.remove(selectedIndex);
                tasks.add("Completed: " + task);
            }
        });

        // Button to clear all tasks
        Button clearAllButton = new Button("Clear All");
        clearAllButton.setOnAction(e -> tasks.clear());

        // Button to save tasks to a file
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Tasks");
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String task : tasks) {
                        writer.write(task);
                        writer.newLine();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Button to load tasks from a file
        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Tasks");
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    tasks.clear();
                    while ((line = reader.readLine()) != null) {
                        tasks.add(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // HBox to hold various action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(removeButton, completeButton, clearAllButton, saveButton, loadButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // VBox to hold the list view and button box
        VBox vbox = new VBox(20, new ScrollPane(listView), buttonBox);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_CENTER);

        // VBox to hold input box and main content
        VBox mainVBox = new VBox(20, vbox, inputBox);
        mainVBox.setPadding(new Insets(10));
        mainVBox.setAlignment(Pos.CENTER);

        // Setting up the layout
        root.setCenter(mainVBox); // Adding main content to the center

        // Setting up the primary stage
        primaryStage.setScene(scene); // Setting the scene
        primaryStage.setTitle("Todo List"); // Setting the title
        primaryStage.show(); // Displaying the stage
    }
}
