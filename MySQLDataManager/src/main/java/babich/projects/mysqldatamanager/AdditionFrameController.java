package babich.projects.mysqldatamanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

import static babich.projects.mysqldatamanager.ManagerApplication.primaryStage;

public class AdditionFrameController {
    @FXML
    private Button additionButton;

    @FXML
    private Button backToMenuButton;

    @FXML
    private TextField parametrsField;

    @FXML
    private ListView<String> resultField;

    @FXML
    private ToggleGroup tables;

    @FXML
    private Button viewTableButton;

    private String currentTableName;

    @FXML
    void initialize() {
        tables.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {
            RadioButton selectedButton = (RadioButton) newValue;
            currentTableName = selectedButton.getText().toLowerCase();
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

        viewTableButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(DBManager.selectAll(MenuFrameController.getConnection(), currentTableName));
            resultField.setItems(items);
        });

        additionButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.insert(MenuFrameController.getConnection(), currentTableName, parametrsField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });
    }

}
