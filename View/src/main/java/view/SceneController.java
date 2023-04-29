package view;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import kompo.sudoku.BacktrackingSudokuSolver;
import kompo.sudoku.SudokuBoard;
import kompo.sudoku.SudokuBoardDaoFactory;
import kompo.sudoku.SudokuSolver;
import view.exceptions.DaoFileException;

public class SceneController {

    private Stage stage;

    private static final Logger LOGGER = Logger.getLogger(SceneController.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("MyLogFile.log");
            ConsoleHandler consoleHandler = new ConsoleHandler();
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            consoleHandler.setFormatter(formatter);
            LOGGER.addHandler(fileHandler);
            LOGGER.addHandler(consoleHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int n = 0;
    private ArrayList<ArrayList<TextField>> textFieldList;
    private ArrayList<ArrayList<IntegerProperty>> integrePropertyList;

    private SudokuSolver solver = new BacktrackingSudokuSolver();
    private SudokuBoard sudokuBoard = new SudokuBoard(solver);
    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private RadioButton rb3;

    @FXML
    private RadioButton language_b1;
    @FXML
    private RadioButton language_b2;

    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    private MenuItem menuItem1;

    private MenuItem menuItem2;
    private MenuItem menuItem3;
    private MenuItem menuItem4;


    private void clearBoard(int n) {
        while (n > 0) {
            Random random = new Random();
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            if (sudokuBoard.get(x, y) != 0) {
                sudokuBoard.set(x, y, 0);
                n--;
            }
        }
    }

    private Scene grid() {
        final BorderPane borderPane = new BorderPane();

        GridPane sudokuGrid = new GridPane();
        sudokuGrid.setGridLinesVisible(true);
        sudokuGrid.setPadding(new Insets(50));
        createTextFields();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                StackPane cell = new StackPane();
                TextField field = textFieldList.get(i).get(j);
                if (sudokuBoard.get(i, j) == 0) {
                    field.setText("");
                    cell.getChildren().add(field);
                } else {
                    field.setText(sudokuBoard.getSudokuField(i, j).toString());
                    field.setEditable(false);
                    cell.getChildren().add(field);
                }
                sudokuGrid.add(cell, i, j, 1, 1);
                cell.setPadding(new Insets(8));
                cell.setMinWidth(50);
                cell.setMinHeight(50);
            }
        }

        menuItem1 = new MenuItem(bundle.getString("_Menu1"));
        menuItem2 = new MenuItem(bundle.getString("_Menu2"));
        menuItem3 = new MenuItem(bundle.getString("_Menu3"));
        menuItem4 = new MenuItem(bundle.getString("_Menu4"));
        Menu menu = new Menu(bundle.getString("_Action"));
        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);
        menu.getItems().add(menuItem3);
        menu.getItems().add(menuItem4);
        FileChooser fileChooser = new FileChooser();
        menuItem1.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    save_to_file(file);
                } catch (kompo.sudoku.exceptions.DaoFileException ex) {
                    LOGGER.warning("File failed to save!");
                    throw new RuntimeException(ex);
                }
            }
        });


        menuItem2.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                try {
                    load_from_file(file);
                } catch (DaoFileException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        menuItem3.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText(bundle.getString("dbwrite"));
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String name = result.get();
                try {
                    SudokuBoardDaoFactory.getJdbcDao(name).write(sudokuBoard);
                } catch (kompo.sudoku.exceptions.DaoFileException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        menuItem4.setOnAction(e -> {
            TextInputDialog dialog1 = new TextInputDialog();
            dialog1.setContentText(bundle.getString("dbwrite"));
            Optional<String> result = dialog1.showAndWait();
            if (result.isPresent()) {
                String name = result.get();
                try (SudokuBoard board = SudokuBoardDaoFactory.getJdbcDao(name).read()) {
                    sudokuBoard = board;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                stage.setScene(grid());
                stage.show();
            }
        });
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        borderPane.setTop(menuBar);
        borderPane.setCenter(sudokuGrid);
        return new Scene(borderPane, 590, 580);
    }

    void bind() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                IntegerProperty intProp =
                        new SimpleIntegerProperty(sudokuBoard.getSudokuField(i, j).getValue());
                Bindings.bindBidirectional(textFieldList.get(i).get(j).textProperty(),
                        intProp, new NumberStringConverter());
            }
        }
    }

    @FXML
    void getDifficulty() {
        if (rb1.isSelected()) {
            n = 40;
        } else if (rb2.isSelected()) {
            n = 55;
        } else if (rb3.isSelected()) {
            n = 70;
        }
    }

    @FXML
    void startGame(ActionEvent event) throws DaoFileException {
        LOGGER.info("Game started!");
        sudokuBoard.solveGame();
        clearBoard(n);
        try {
            switchScene(event, grid());
        } catch (IOException e) {
            throw new DaoFileException(bundle.getString("daoinfoW"));
        }
        LOGGER.info(sudokuBoard.toString());
        bind();
    }

    public void switchScene(ActionEvent event, Scene scena) throws DaoFileException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scena);
        stage.show();
    }


    void save_to_file(File file) throws kompo.sudoku.exceptions.DaoFileException {
        SudokuBoardDaoFactory.getFileDao(file).write(sudokuBoard);
    }

    void load_from_file(File file) throws DaoFileException {
        try {
            sudokuBoard = SudokuBoardDaoFactory.getFileDao(file).read();
        } catch (IOException e) {
            throw new DaoFileException("test");
        }
        stage.setScene(grid());
        stage.show();
    }

    @FXML
    public void changeLanguage(ActionEvent event) throws DaoFileException {
        if (language_b2.isSelected()) {
            Locale.setDefault(new Locale("en"));
        } else {
            Locale.setDefault(new Locale("pl"));
        }
        LOGGER.info("Language has been changed!");
        bundle = ResourceBundle.getBundle("Language");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/DifficultyScene.fxml"));
        loader.setResources(bundle);
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            throw new DaoFileException(bundle.getString("daoinfoR"));
        }
        Scene scene1 = new Scene(anchorPane);
        switchScene(event, scene1);
    }

    @FXML
    public void displayAuthors(ActionEvent event) {
        ResourceBundle listBundle = ResourceBundle.getBundle("view.Authors");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("_Authors"));
        LOGGER.info("Authors displayed!");
        alert.setContentText(listBundle.getObject("IMIE1") + " "
                + listBundle.getObject("NAZWISKO1") + " "
                + listBundle.getObject("NRINDEKSU1") + "\n" + listBundle.getObject("IMIE2") + " "
                + listBundle.getObject("NAZWISKO2") + " " + listBundle.getObject("NRINDEKSU2"));
        alert.showAndWait();
    }

    private void createTextFields() {
        textFieldList = new ArrayList<>();
        integrePropertyList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ArrayList<TextField> innerList = new ArrayList<>();
            ArrayList<IntegerProperty> secondInnerList = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                IntegerProperty intProp = new SimpleIntegerProperty();
                intProp.set(sudokuBoard.get(i, j));
                secondInnerList.add(intProp);
                TextField textField = new TextField();
                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String text = change.getText();
                    if (text.matches("[1-9]?") && (text.isEmpty()
                            || Integer.parseInt(text) >= 0 && Integer.parseInt(text) <= 9)
                            && change.getControlNewText().length() <= 1) {
                        return change;
                    }
                    return null;
                };

                TextFormatter<Integer> textFormatter =
                        new TextFormatter<>(new IntegerStringConverter(), null, filter);

                textField.setTextFormatter(textFormatter);
                innerList.add(textField);
            }
            textFieldList.add(innerList);
            integrePropertyList.add(secondInnerList);
        }
        test();
    }

    private void test() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int sudokuFieldValue = sudokuBoard.getSudokuField(i, j).getValue();
                TextField textField = textFieldList.get(i).get(j);
                textField.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        LOGGER.info(String.valueOf(sudokuFieldValue));
                    }
                });
            }
        }
    }
}
