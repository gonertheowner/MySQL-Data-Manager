package babich.projects.mysqldatamanager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private TextField tableNameField;

    @FXML
    private Button updateButton;

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
    }
}
