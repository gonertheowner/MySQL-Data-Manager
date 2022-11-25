package babich.projects.mysqldatamanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private static int parseStringToInteger(String str) {
        if (str.matches("\\d+")) {
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    private static String getStringAfterSyntaxRules(String string) {
        StringBuilder result = new StringBuilder();
        if (string.split(" ").length > 1) {
            String[] words = string.split(" ");
            result.append(words[0]).append("_").append(words[1]);
        } else {
            result.append(string);
        }
        return result.toString();
    }

    public static ObservableList<String> selectAll(Connection connection, String tableName) {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (tableName == null) {
            items.add("Пожалуйста, выберете таблицу\n");
            return items;
        }

        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + getStringAfterSyntaxRules(tableName));
            StringBuilder result = new StringBuilder();
            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                result.append(metaData.getColumnName(i)).append(" ");
            }
            items.add(result.toString());
            result.delete(0, result.length());

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(resultSet.getString(i)).append(" ");
                }
                items.add(result.toString());
                result.delete(0, result.length());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public static ObservableList<String> delete(Connection connection, String tableName, String id) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (tableName.equals("")) {
            items.add("Пожалуйста, выберете таблицу\n");
            return items;
        }

        if (parseStringToInteger(id) == -1) {
            items.add("Пожалуйста, введите целое положительное число\n");
            return items;
        }

        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet selectionSet = statement.executeQuery("SELECT * FROM " + getStringAfterSyntaxRules(tableName));
        ArrayList<String> ids = new ArrayList<>();
        while (selectionSet.next()) {
            ids.add(selectionSet.getString(1));
        }

        if (!(ids.contains(id))) {
            items.add("Пожалуйста, введите существующий id");
            return items;
        }

        connection.prepareStatement("DELETE FROM " + getStringAfterSyntaxRules(tableName) + " WHERE "
                + getStringAfterSyntaxRules(selectionSet.getMetaData().getColumnName(1)) + " = " + "'" + id + "'").execute();

        items.add("Удаление произошло успешно");
        return items;
    }

    public static ObservableList<String> insert(Connection connection, String tableName, String parameters) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (tableName == null) {
            items.add("Пожалуйста, перед добавлением выберете таблицу\n");
            return items;
        }

        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet selectionSet = statement.executeQuery("SELECT * FROM " + getStringAfterSyntaxRules(tableName));
        String[] insertParameters = parameters.split(",");
        for (int i = 0; i < insertParameters.length; i++) {
            insertParameters[i] = insertParameters[i].trim();
        }
        int columnCount = selectionSet.getMetaData().getColumnCount();
        ArrayList<Integer> dataTypes = new ArrayList<>();
        for (int i = 2; i <= columnCount; i++) {
            dataTypes.add(selectionSet.getMetaData().getColumnType(i));
        }

        if (insertParameters.length != dataTypes.size()) {
            items.add("Количестов параметров не совпадает с количеством столбцов таблицы");
            return items;
        }

        ArrayList<Integer> parametersDataTypes = new ArrayList<>();
        for (String insertParameter : insertParameters) {
            if (parseStringToInteger(insertParameter.trim()) == -1) {
                parametersDataTypes.add(12);
            } else {
                parametersDataTypes.add(4);
            }
        }

        for (int i = 0; i < parametersDataTypes.size(); i++) {
            if (!(parametersDataTypes.get(i).equals(dataTypes.get(i)))) {
                items.add("Типы введенных параметров не совпадают с типами столбцов таблицы");
                return items;
            }
        }

        StringBuilder allColumns = new StringBuilder();
        for (int i = 2; i <= columnCount; i++) {
            allColumns.append(getStringAfterSyntaxRules(selectionSet.getMetaData().getColumnName(i))).append(", ");
        }
        allColumns.delete(allColumns.length() - 2, allColumns.length());

        StringBuilder allParameters = new StringBuilder();
        for (String insertParameter : insertParameters) {
            allParameters.append("'").append(insertParameter).append("', ");
        }
        allParameters.delete(allParameters.length() - 2, allParameters.length());

        String query = "INSERT INTO " + getStringAfterSyntaxRules(tableName) + " (" + allColumns + ") VALUES (" + allParameters + ")";
        connection.prepareStatement(query).execute();

        items.add("Запись успешно добавлена");
        return items;
    }

    public static ObservableList<String> query1(Connection connection) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(ФИО_директора) FROM поставщик");

        items.add("ФИО поставщика\n");
        while (resultSet.next()) {
            items.add(resultSet.getString(1));
        }
        if (items.size() == 1) {
            items.add("Ничего не найдено");
        }
        return items;
    }

    public static ObservableList<String> query2(Connection connection) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Название FROM завод GROUP BY Название ORDER BY Название");

        items.add("Название завода\n");
        while (resultSet.next()) {
            items.add(resultSet.getString(1));
        }
        if (items.size() == 1) {
            items.add("Ничего не найдено");
        }
        return items;
    }

    public static ObservableList<String> query3(Connection connection) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT AVG(Вес) FROM деталь");

        items.add("Срелний вес деталей\n");
        while (resultSet.next()) {
            items.add(resultSet.getString(1));
        }
        if (items.size() == 1) {
            items.add("Ничего не найдено");
        }
        return items;
    }

    public static ObservableList<String> query4(Connection connection, String parameter) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (parameter.equals("")) {
            items.add("Пожалуйста, сначала введите параметр");
            return items;
        }

        if (parseStringToInteger(parameter) == -1) {
            items.add("Параметр должен быть целым положительным числом");
            return items;
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM изделие WHERE Вес_изделия > " + "'" + parameter + "'");

        while (resultSet.next()) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                result.append(resultSet.getString(i)).append(" ");
            }
            items.add(result + "\n");
            result.delete(0, result.length());
        }
        return items;
    }

    public static ObservableList<String> query5(Connection connection, String parameter) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (parameter.equals("")) {
            items.add("Пожалуйста, сначала введите параметр");
            return items;
        }

        if (parseStringToInteger(parameter) == -1) {
            items.add("Параметр должен быть целым положительным числом");
            return items;
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM деталь WHERE Размеры < " + "'" + parameter + "'");

        while (resultSet.next()) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                result.append(resultSet.getString(i)).append(" ");
            }
            items.add(result + "\n");
            result.delete(0, result.length());
        }
        return items;
    }

    public static ObservableList<String> query6(Connection connection, String parameter) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (parameter.equals("")) {
            items.add("Пожалуйста, сначала введите параметр");
            return items;
        }

        if (parseStringToInteger(parameter) == -1) {
            items.add("Параметр должен быть целым положительным числом");
            return items;
        }

        Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM отдел WHERE LENGTH(ФИО_начальника) = " + "'" + parameter + "'");

        while (resultSet.next()) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                result.append(resultSet.getString(i)).append(" ");
            }
            items.add(result + "\n");
            result.delete(0, result.length());
        }
        return items;
    }

    public static ObservableList<String > update(Connection connection, String tableName, String id, String columnNames, String parameters) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (tableName == null) {
            items.add("Пожалуйста, сначала выберете таблицу.");
            return items;
        }

        if (id.equals("")) {
            items.add("Пожалуйста, сначала введите id");
            return items;
        }

        if (parseStringToInteger(id) == -1) {
            items.add("Пожалуйста, введите целое положительное число\n");
            return items;
        }

        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ResultSet selectionSet = statement.executeQuery("SELECT * FROM " + getStringAfterSyntaxRules(tableName));
        ArrayList<String> ids = new ArrayList<>();
        while (selectionSet.next()) {
            ids.add(selectionSet.getString(1));
        }

        if (!(ids.contains(id))) {
            items.add("Пожалуйста, введите существующий id");
            return items;
        }

        if (columnNames.equals("")) {
            items.add("Пожалуйста, сначала введите имена столбцов, которые Вы бы хотели изменить");
            return items;
        }

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + getStringAfterSyntaxRules(tableName));
            var metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] inputColumns = columnNames.split(",");

            if (inputColumns.length > columnCount - 1) {
                items.add("Количество введенных Вами столбцов больше, чем количество столбцов в выбранной таблице");
                return items;
            }

            ArrayList<String> tableColumnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                tableColumnNames.add(metaData.getColumnName(i));
            }

            for (int i = 0; i < inputColumns.length; i++) {
                inputColumns[i] = inputColumns[i].trim();
                if (!(tableColumnNames.contains(inputColumns[i]))) {
                    items.add("Введенное имя столбца: " + inputColumns[i - 1] + " не совпадает ни с одним именем столбца выбранной таблицы");
                    return items;
                }
            }

            if (parameters.equals("")) {
                items.add("Пожалуйста, сначала введите параметры");
                return items;
            }

            String[] inputParameters = parameters.split(",");
            for (int i = 0; i < inputParameters.length; i++) {
                inputParameters[i] = inputParameters[i].trim();
            }

            ArrayList<Integer> dataTypes = new ArrayList<>();
            for (int i = 2; i <= columnCount; i++) {
                dataTypes.add(resultSet.getMetaData().getColumnType(i));
            }

            if (inputColumns.length != inputParameters.length) {
                items.add("Количество изменяемых столбцов должно равняться количеству введенных параметров");
                return items;
            }

            if (inputParameters.length > dataTypes.size()) {
                items.add("Количестов параметров не должно быть больше, чем количество столбцов таблицы");
                return items;
            }

            ArrayList<Integer> parametersDataTypes = new ArrayList<>();
            for (String parameter : inputParameters) {
                if (parseStringToInteger(parameter.trim()) == -1) {
                    parametersDataTypes.add(12);
                } else {
                    parametersDataTypes.add(4);
                }
            }

            for (int i = 0; i < parametersDataTypes.size(); i++) {
                if (!(parametersDataTypes.get(i).equals(dataTypes.get(i)))) {
                    items.add("Типы введенных параметров не совпадают с типами соответствующих столбцов таблицы");
                    return items;
                }
            }

            StringBuilder result = new StringBuilder();
            result.append("UPDATE ").append(getStringAfterSyntaxRules(tableName).toLowerCase()).append(" SET ");
            for (int i = 0; i < inputParameters.length; i++) {
                result.append(inputColumns[i]).append("='").append(inputParameters[i]).append("',");
            }
            result.delete(result.length() - 1, result.length());
            result.append(" WHERE ").append(metaData.getColumnName(1)).append("='").append(id).append("'");
            System.out.println(result);
            connection.prepareStatement(result.toString()).execute();

            items.add("Изменение прошло успешно");
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}