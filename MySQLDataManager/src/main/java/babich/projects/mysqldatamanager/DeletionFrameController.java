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
import javafx.scene.text.Text;

import static babich.projects.mysqldatamanager.ManagerApplication.primaryStage;

public class DeletionFrameController {
    @FXML
    private Button deleteButton;

    @FXML
    private Button goToMenuButton;

    @FXML
    private TextField idField;

    @FXML
    private ListView<String> resultField;

    @FXML
    private Text errorTextFlow;

    @FXML
    private Button viewResultButton;

    @FXML
    private ToggleGroup tableNamesAgain;

    private String currentTableName;

    @FXML
    void initialize() {
        errorTextFlow.setVisible(false);

        tableNamesAgain.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {
            RadioButton selectedButton = (RadioButton) newValue;
            currentTableName = selectedButton.getText();
        });

        viewResultButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(DBManager.selectAll(MenuFrameController.getConnection(), currentTableName));
            resultField.setItems(items);
        });

        deleteButton.setOnAction(actionEvent -> {
            if (idField.getText().equals("")) {
                errorTextFlow.setVisible(true);
            } else {
                try {
                    ObservableList<String> items = FXCollections.observableArrayList();
                    items.addAll(DBManager.delete(MenuFrameController.getConnection(), currentTableName, idField.getText()));
                    resultField.setItems(items);
                    errorTextFlow.setVisible(false);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        goToMenuButton.setOnAction(actionEvent -> {
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
    }
}
