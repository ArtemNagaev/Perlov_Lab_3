package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class Controller {
    @FXML
    private LineChart Graph_2;

    public void Controller(){
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (int i = 0; i<Main.forecast.size(); i++){
            Graph_2.getData().add(new XYChart.Data<Number, Number>(i+1, Main.forecast.get(i)));
        }

    }


    public void initialize(){
        XYChart.Series forecast_series = new XYChart.Series();
        XYChart.Series data_series = new XYChart.Series();
        XYChart.Series trend_series = new XYChart.Series();
        forecast_series.setName("Прогноз");
        data_series.setName("Исходные данные");
        trend_series.setName("Линия тренда");

        for (int i = 0; i<Main.forecast.size(); i++){
            forecast_series.getData().add(new XYChart.Data(String.valueOf(i+1), Main.forecast.get(i)));
        }

        for (int i = 0; i<Main.data.size(); i++){
            data_series.getData().add(new XYChart.Data(String.valueOf(i+1), Main.data.get(i)));
        }

        for (int i = 0; i<Main.data.size(); i++){
            trend_series.getData().add(new XYChart.Data(String.valueOf(i+1), Main.Trend.get(i)));
        }

        ObservableList<XYChart.Series> chart = FXCollections.observableArrayList();
        chart.add(data_series);
        chart.add(forecast_series);
        chart.add(trend_series);

        Graph_2.setData(chart);
    }

}
