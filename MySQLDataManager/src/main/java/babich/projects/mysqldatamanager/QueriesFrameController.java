package babich.projects.mysqldatamanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import static babich.projects.mysqldatamanager.ManagerApplication.primaryStage;

public class QueriesFrameController {
    @FXML
    private Button backToMenuButton;

    @FXML
    private TextField parameterField;

    @FXML
    private Button queryButton1;

    @FXML
    private Button queryButton2;

    @FXML
    private Button queryButton3;

    @FXML
    private Button queryButton4;

    @FXML
    private Button queryButton5;

    @FXML
    private Button queryButton6;

    @FXML
    private ListView<String> resultField;

    @FXML
    void initialize() {
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

        queryButton1.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query1(MenuFrameController.getConnection()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });

        queryButton2.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query2(MenuFrameController.getConnection()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });

        queryButton3.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query3(MenuFrameController.getConnection()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });

        queryButton4.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query4(MenuFrameController.getConnection(), parameterField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });

        queryButton5.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query5(MenuFrameController.getConnection(), parameterField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });

        queryButton6.setOnAction(actionEvent -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            try {
                items.addAll(DBManager.query6(MenuFrameController.getConnection(), parameterField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resultField.setItems(items);
        });
    }

}
