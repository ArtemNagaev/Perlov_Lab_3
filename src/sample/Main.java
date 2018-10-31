package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;



public class Main extends Application {

    static  public Double sum(ArrayList<Double> inputList){
        double sum = 0.0;
        for (int i = 0; i < inputList.size(); i++){
            sum+=inputList.get(i);
        }
        return sum;
    }

    static public ArrayList<Double> MovingAverage (ArrayList<Double> inputList, Integer window){
        ArrayList<Double> outputList = new ArrayList<Double>();

        for (int i = 0; i<=inputList.size() - window; i++){
            Double tmpNum = 0.0;
            for (int j=0 ; j < window; j++){
                tmpNum+=inputList.get(i+j);
            }
            outputList.add(tmpNum/window);
        }

        return outputList;
    }

    static public double Average (ArrayList<Double> inputList){
        double sum = 0;

        for (double number:inputList
             ) {
            sum+=number;
        }
        return sum/inputList.size();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }



    public static void main(String[] args) {

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream("src/data/lab3_data.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheet("Лист1");

        ArrayList<Double> data = new ArrayList<Double>();

        for (Row row: sheet) {
            Cell cell = row.getCell(0);

            data.add(cell.getNumericCellValue());
        }

        //Скользящее среднее
        ArrayList<Double> dataMA = new ArrayList<Double>();
        dataMA = MovingAverage(data,12);

        //Скользящее среднее центрированное
        ArrayList<Double> dataMACentric = new ArrayList<Double>();
        dataMACentric = MovingAverage(dataMA,2);

        //Сглаженное скользящее среднее (Сезонная компонента)
        ArrayList<Double> dataMAmean = new ArrayList<Double>();
        for (int i = 6; i < data.size()-6; i++){
            dataMAmean.add(data.get(i)/dataMACentric.get(i-6));
        }

        //Считаем среднюю сезонную компоненту

        //Добавляем нулю в начало и конец для деления по месяцам
        ArrayList<Double> dataMAmeanFixed = new ArrayList<Double>();
        for (int i = 0; i < 6; i++){
            dataMAmeanFixed.add(0.0);
        }

        for (int i = 0; i < dataMAmean.size(); i++){
            dataMAmeanFixed.add(dataMAmean.get(i));
        }

        for (int i = 0; i < 6; i++){
            dataMAmeanFixed.add(0.0);
        }


        //Записываем компоненты по месяцам
        ArrayList<ArrayList<Double>> dataMAmeanMonth = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i<12; i++){
            dataMAmeanMonth.add(new ArrayList<Double>());
            for (int j = 0; j<dataMAmeanFixed.size(); j+=12){
                dataMAmeanMonth.get(i).add(dataMAmeanFixed.get(i+j));
            }
        }


        ArrayList<Double>  seasonComp = new ArrayList<Double>();

        //убираем нули
        for (int i = 0; i < dataMAmeanMonth.size(); i++){
            dataMAmeanMonth.get(i).remove(0.0);
        }

        //Считаем среднюю сезонную компоненту
        for (int i = 0; i < dataMAmeanMonth.size(); i++){
            seasonComp.add(Average(dataMAmeanMonth.get(i)));
        }

        //Считаем корректирующий коэффициент
        Double coef = seasonComp.size()/ sum(seasonComp);

        //Считаем скорректированную сезонную компоненту
        ArrayList<Double>  seasonCompCorr = new ArrayList<Double>();

        for (int i = 0; i < seasonComp.size(); i++){
            seasonCompCorr.add(seasonComp.get(i)*coef);
        }


        //считаем тренд
        Double n = Double.valueOf(data.size());
        Double SumXi = sum(data);

        //находим значение без сезонной компоненты

        //Double SumYi = sum()

        for (Double number:seasonComp
             ) {
            System.out.print(number + " ");

        }
        launch(args);
    }
}
