package babich.projects.mysqldatamanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static babich.projects.mysqldatamanager.ManagerApplication.primaryStage;

public class SelectionFrameController {
    public VBox tables;
    @FXML
    private ListView<String> resultField;

    @FXML
    private ToggleGroup tableNames;

    @FXML
    private Button viewButton;

    @FXML
    private Button goToMenuButton;

    private String currentTableName;

    @FXML
    void initialize() {
        tableNames.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {
            RadioButton selectedButton = (RadioButton) newValue;
            currentTableName = selectedButton.getText().toLowerCase();
        });

        viewButton.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(DBManager.selectAll(MenuFrameController.getConnection(), currentTableName));
            resultField.setItems(items);
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

