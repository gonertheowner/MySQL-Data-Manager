package babich.projects.mysqldatamanager;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import static babich.projects.mysqldatamanager.ManagerApplication.primaryStage;

public class UpdateFrameController {
    @FXML
    private Button backToMenuButton;

    @FXML
    private TextField columnNamesField;

    @FXML
    private TextField parametersField;

    @FXML
    private ListView<String> resultField;

    @FXML
    private TextField idField;

    @FXML
    private Button updateButton;

    @FXML
    private ToggleGroup tableNames;

    @FXML
    private ListView<String> tableField;

    @FXML
    private Button selectAndViewButton;

    private String currentTableName;

    @FXML
    void initialize() {
        tableNames.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {
            RadioButton selectedButton = (RadioButton) newValue;
            currentTableName = selectedButton.getText().toLowerCase();
        });

        selectAndViewButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(DBManager.selectAll(MenuFrameController.getConnection(), currentTableName));
            tableField.setItems(items);
        });

        backToMenuButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("menu-frame.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            primaryStage.setScene(new Scene(root));
        });

        updateButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.update(MenuFrameController.getConnection(), currentTableName, idField.getText(), columnNamesField.getText(), parametersField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });
    }
}
