// App.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App extends Application {
    private static int noteCounter = 0;
    private static final String NOTES_DIR = "notes";
    private Map<LocalDate, List<String>> notesByDate = new HashMap<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void start(Stage primaryStage) {
        // Create notes folder if not exists
        File dir = new File(NOTES_DIR);
        if (!dir.exists()) dir.mkdir();

        // Load saved notes
        loadExistingNotes();

        // Menu for Calendar and Analytics
        Button calendarButton = new Button("📅 Calendar");
        Button analyticsButton = new Button("📈 Analytics");
        HBox menuBar = new HBox(10, calendarButton, analyticsButton);
        menuBar.setPadding(new Insets(10));
        menuBar.setStyle("-fx-background-color: #eeeeee;");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(menuBar);

        Scene mainScene = new Scene(mainLayout, 400, 200);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Sticky Notes Dashboard");
        primaryStage.show();

        // Create default empty note
        createNote("");

        // Actions
        calendarButton.setOnAction(e -> openCalendar());
        analyticsButton.setOnAction(e -> openAnalytics());
    }

    private void createNote(String initialContent) {
        Stage noteStage = new Stage();
        noteCounter++;

        TextArea textArea = new TextArea(initialContent);
        textArea.setPromptText("Type your note here...");

        Button newNoteButton = new Button("New Note");
        Button saveButton = new Button("Save");
        Button clearButton = new Button("Clear");
        ColorPicker colorPicker = new ColorPicker(Color.LIGHTYELLOW);

        HBox toolbar = new HBox(10, newNoteButton, saveButton, clearButton, colorPicker);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #dddddd;");

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(textArea);

        Scene scene = new Scene(root, 300, 300);
        noteStage.setScene(scene);
        noteStage.setTitle("Sticky Note " + noteCounter);
        noteStage.setAlwaysOnTop(true);
        noteStage.setResizable(true);
        noteStage.show();

        // Actions
        newNoteButton.setOnAction(e -> createNote(""));
        saveButton.setOnAction(e -> saveNote(textArea.getText(), noteCounter));
        clearButton.setOnAction(e -> textArea.clear());
        colorPicker.setOnAction(e -> {
            Color color = colorPicker.getValue();
            String hexColor = String.format("#%02X%02X%02X",
                    (int)(color.getRed()*255),
                    (int)(color.getGreen()*255),
                    (int)(color.getBlue()*255));
            textArea.setStyle("-fx-control-inner-background: " + hexColor + ";");
        });
    }

    private void saveNote(String content, int noteNumber) {
        try {
            LocalDate today = LocalDate.now();
            FileWriter writer = new FileWriter(NOTES_DIR + "/note" + noteNumber + ".txt");
            writer.write("[DATE]" + today.format(dateFormatter) + "\n");
            writer.write(content);
            writer.close();

            // Save to memory map
            notesByDate.putIfAbsent(today, new ArrayList<>());
            notesByDate.get(today).add(content);

            System.out.println("Note " + noteNumber + " saved!");
        } catch (IOException e) {
            System.out.println("Error saving note " + noteNumber);
            e.printStackTrace();
        }
    }

    private void loadExistingNotes() {
        File folder = new File(NOTES_DIR);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String dateLine = reader.readLine();
                    if (dateLine != null && dateLine.startsWith("[DATE]")) {
                        LocalDate date = LocalDate.parse(dateLine.substring(6), dateFormatter);
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        noteCounter++;
                        createNote(content.toString());

                        // Save to memory map
                        notesByDate.putIfAbsent(date, new ArrayList<>());
                        notesByDate.get(date).add(content.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openCalendar() {
        Stage calendarStage = new Stage();
        calendarStage.initModality(Modality.APPLICATION_MODAL);
        calendarStage.setTitle("Choose Date");

        DatePicker datePicker = new DatePicker();
        Button showNotesButton = new Button("Show Notes");

        VBox vbox = new VBox(10, datePicker, showNotesButton);
        vbox.setPadding(new Insets(20));

        showNotesButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                showNotesForDate(selectedDate);
            }
            calendarStage.close();
        });

        Scene scene = new Scene(vbox, 300, 150);
        calendarStage.setScene(scene);
        calendarStage.show();
    }

    private void showNotesForDate(LocalDate date) {
        Stage notesStage = new Stage();
        notesStage.setTitle("Notes on " + date.format(dateFormatter));

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        List<String> notes = notesByDate.getOrDefault(date, new ArrayList<>());
        if (notes.isEmpty()) {
            vbox.getChildren().add(new Label("No notes found for this date."));
        } else {
            for (String note : notes) {
                TextArea noteArea = new TextArea(note);
                noteArea.setWrapText(true);
                noteArea.setEditable(false);
                noteArea.setStyle("-fx-control-inner-background: #fffacd;"); // Light yellow
                vbox.getChildren().add(noteArea);
            }
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        Scene scene = new Scene(scrollPane, 400, 400);
        notesStage.setScene(scene);
        notesStage.show();
    }

    private void openAnalytics() {
        Stage analyticsStage = new Stage();
        analyticsStage.initModality(Modality.APPLICATION_MODAL);
        analyticsStage.setTitle("Analytics");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        for (LocalDate date : notesByDate.keySet()) {
            int count = notesByDate.get(date).size();
            Label label = new Label(date.format(dateFormatter) + ": " + count + " notes");
            vbox.getChildren().add(label);
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        Scene scene = new Scene(scrollPane, 300, 400);
        analyticsStage.setScene(scene);
        analyticsStage.show();
    }
}
