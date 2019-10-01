package jenkins.extensions;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static jenkins.extensions.BuildsDiffer.DB_URI_PREFIX;

public class BuildTrendGUIApp extends Application {
    private static final String SELECT_REGIONS_TREND = "select * from UPDATE_REGIONS_TREND";
    private static final String SELECT_PRODUCTS_TREND = "select * from PRODUCTS_TREND";

    private static final ToIntFunction<String> TO_BUILD_NUMBER = column -> Integer.parseInt(column.replaceAll("[^\\d]", ""));


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Build Time per Product Chart");
//        final NumberAxis yAxis = new NumberAxis(0, 5000000, 1);
        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();

//        XYChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        yAxis.setLabel("Build time");
        xAxis.setLabel("Build Number");
        chart.setTitle("Build Time per Product");

        getData().forEach((productName, trend) ->
        {
            XYChart.Series<String, Number> product = new XYChart.Series<>();
            product.setName(productName);
            trend.forEach((buildNumber, buildTime) -> product.getData().add(new XYChart.Data<>(buildNumber.toString(), buildTime)));
            chart.getData().add(product);
        });
        Scene scene = new Scene(chart, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private Map<String, Map<Integer, Integer>> getData() {
        Path buildsTrendPath = new BuildsDiffer().getOutputFilePath();
        try (Connection connection = DriverManager.getConnection(DB_URI_PREFIX + buildsTrendPath.toString());
             PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCTS_TREND);
             ResultSet resultSet = statement.executeQuery()) {
            Map<String, Map<Integer, Integer>> buildTimePerProduct = new TreeMap<>();
            List<String> columns = getColumnsFromTable(connection, "PRODUCTS_TREND");
            List<Integer> buildNumbers = columns.subList(1, columns.size()).stream()
                    .mapToInt(TO_BUILD_NUMBER)
                    .boxed()
                    .collect(Collectors.toList());
            while (resultSet.next()) {
                String productName = resultSet.getString(1);
                for (int i = 0; i < buildNumbers.size(); i++) {
                    int buildNumber = buildNumbers.get(i);
                    // probably convert to some other time unit
                    int buildTime = resultSet.getInt(i + 2);
                    buildTimePerProduct.computeIfAbsent(productName, value -> new TreeMap<>()).put(buildNumber, buildTime);
                }
            }
            return buildTimePerProduct;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getColumnsFromTable(Connection connection, String tableName) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getColumns(null, null, tableName, null)) {
            List<String> columns = new ArrayList<>();
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
            return columns.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

