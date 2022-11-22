module babich.projects.mysqldatamanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens babich.projects.mysqldatamanager to javafx.fxml;
    exports babich.projects.mysqldatamanager;
}