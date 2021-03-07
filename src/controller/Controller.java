package controller;

import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Calculator;
import model.Dao_calc;
import model.ReadFromFile;
import model.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField walls_count_text;      //Поле = количество стен
    public TextField holes_count_text;      //Поле = количество окон и дверей
    public TextField height_text;           //Поле = высота помещения

    public Button submit;       //Кнопка = подтвердить
    public Button reset;        //Кнопка = сброс

    public TabPane calculator;      //Панель = заполнение данных об отделочных материалах

    public ChoiceBox<Rectangle> wallpaperSelector;      //Выбор размера обоев
    public RadioButton wallpaperRadioSelect;                //Выбирать размер обоев
    public RadioButton wallpaperRadioType;                  //Вводить вречную размер обоес
    public TextField wallpaperWidth;                    //Ширина обоев
    public TextField wallpaperLength;                   //Длина обоев
    public TextArea wallpaperResult;                    //Результат

    public ChoiceBox<Rectangle> tileSelector;           //...для Плитки
    public RadioButton tileRadioSelect;                 //
    public RadioButton tileRadioType;                   //
    public TextField tileWidth;                         //
    public TextField tileLength;                        //
    public TextArea tileResult;                         //

    public ChoiceBox<Rectangle> panelSelector;          //...для Панелей
    public RadioButton panelRadioSelect;                //
    public RadioButton panelRadioType;                  //
    public TextField panelWidth;                        //
    public TextField panelLength;                       //
    public TextArea panelResult;                        //

    public int walls_count;         //Количество стен
    public int holes_count;         //Количество отверстий
    public double height;           //Высота

    public List<TextField> walls = new ArrayList<>();           //Список текстовых полей для заполнения длин стен
    public List<TextField> holes_length = new ArrayList<>();    //Список текстовых полей для заполнения длин отверстий
    public List<TextField> holes_height = new ArrayList<>();    //Список текстовых полей для заполнения широт отверстий

    private final Stage dataWindow = new Stage();   //Окно заполнения данных о комнате
    private Dao_calc calc;                          //Калькулятор


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calc = null;    //Для проверки null или not null

        calculator.setVisible(true);

        ReadFromFile reader = ReadFromFile.getInstance();
        ArrayList<Double> wlpprs = reader.getWallpapersSize();

        for (int i = 0; i < wlpprs.size(); i=i+2) {
            wallpaperSelector.getItems().add(new Rectangle(wlpprs.get(i), wlpprs.get(i+1)));
        }
        wallpaperSelector.setValue(wallpaperSelector.getItems().get(0));                                             //Выбор обоев по умолчанию

        wallpaperRadioSelect.setOnAction(e -> {       //При выборе первой радиокнопки (выбирать из имеющахся)
            wallpaperWidth.setEditable(false);
            wallpaperWidth.setDisable(true);
            wallpaperWidth.setText("");
            wallpaperLength.setEditable(false);
            wallpaperLength.setDisable(true);
            wallpaperLength.setText("");
            wallpaperRadioType.setSelected(false);
        });

        wallpaperRadioType.setOnAction(e -> {       //При выборе первой радиокнопки (вводить вручную)
            wallpaperWidth.setEditable(true);
            wallpaperWidth.setDisable(false);
            wallpaperLength.setEditable(true);
            wallpaperLength.setDisable(false);
            wallpaperRadioSelect.setSelected(false);
        });

        //дальше то же самое для плитки и панелей

        ArrayList<Double> tls = reader.getTilesSize();

        for (int i = 0; i < tls.size(); i=i+2) {
            tileSelector.getItems().add(new Rectangle(tls.get(i), tls.get(i+1)));
        }
        tileSelector.setValue(tileSelector.getItems().get(0));                                             //Выбор обоев по умолчанию

        tileRadioSelect.setOnAction(e -> {
            tileWidth.setEditable(false);
            tileWidth.setDisable(true);
            tileWidth.setText("");
            tileLength.setEditable(false);
            tileLength.setDisable(true);
            tileLength.setText("");
            tileRadioType.setSelected(false);
        });

        tileRadioType.setOnAction(e -> {
            tileWidth.setEditable(true);
            tileWidth.setDisable(false);
            tileLength.setEditable(true);
            tileLength.setDisable(false);
            tileRadioSelect.setSelected(false);
        });

        ArrayList<Double> pnls = reader.getPanelsSize();

        for (int i = 0; i < pnls.size(); i=i+2) {
            panelSelector.getItems().add(new Rectangle(pnls.get(i), pnls.get(i+1)));
        }
        panelSelector.setValue(panelSelector.getItems().get(0));                                             //Выбор обоев по умолчанию

        panelRadioSelect.setOnAction(e -> {
            panelWidth.setEditable(false);
            panelWidth.setDisable(true);
            panelWidth.setText("");
            panelLength.setEditable(false);
            panelLength.setDisable(true);
            panelLength.setText("");
            panelRadioType.setSelected(false);
        });

        panelRadioType.setOnAction(e -> {
            panelWidth.setEditable(true);
            panelWidth.setDisable(false);
            panelLength.setEditable(true);
            panelLength.setDisable(false);
            panelRadioSelect.setSelected(false);
        });
    }   //При запуске программного средства

    public void submit() {
        try {
            walls_count = Integer.parseInt(walls_count_text.getText());     //Преобразовать из текста в число
            holes_count = Integer.parseInt(holes_count_text.getText());
            height = Double.parseDouble(height_text.getText());
        } catch (Exception e) {      //Поиск исключений
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Количество стен и отверстий должны быть целыми положительными числами\nВысота должна быть положительным числом");
            alert.showAndWait();
            return;
        }
        if (walls_count >= 3 && holes_count >= 0 && height > 0) {
            if (dataWindow.isShowing())
                dataWindow.close();
            roomDataWindow(walls_count, holes_count);   //Открыть окно ввода данных
        }
        else {       //Ошибка
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Количество стен должно быть больше или равно трём\nКоличество отверстий должно быть неотрицательным\nВысота должна быть положительной");
            alert.showAndWait();
            return;
        }

    }

    public void reset() {
        calc = null;
        walls_count_text.clear();
        holes_count_text.clear();
        height_text.clear();
        holes_height.clear();
        holes_length.clear();
        walls.clear();
        submit.setText("Ввести данные");
        calculator.setVisible(false);
    } //Очистить

    private void roomDataWindow(int walls_count, int holes_count) {
        int gridWidth = 700;
        int rowHeight = 30;
        GridPane grid = new GridPane();         //Создание панели, столбцов и строк
        grid.setPrefHeight(-1);
        grid.setMaxHeight(500);
        grid.setMinHeight(-1);
        grid.setPrefWidth(gridWidth);
        grid.setMaxWidth(gridWidth);
        grid.setMinWidth(gridWidth);
        grid.setVgap(5);
        grid.setHgap(5);
        grid.getColumnConstraints().add(new ColumnConstraints( 100, -1, -1, Priority.SOMETIMES, HPos.CENTER, true));
        grid.getColumnConstraints().add(new ColumnConstraints( 100, -1, -1, Priority.SOMETIMES, HPos.CENTER, true));
        grid.getColumnConstraints().add(new ColumnConstraints( 100, -1, -1, Priority.SOMETIMES, HPos.CENTER, true));
        grid.getRowConstraints().add(new RowConstraints(50, -1, -1, Priority.SOMETIMES, VPos.CENTER, true));
        for (int i = 0; i < walls_count; i++)
            grid.getRowConstraints().add(new RowConstraints(rowHeight, rowHeight, rowHeight, Priority.SOMETIMES, VPos.CENTER, true));
        for (int i = 0; i < holes_count; i++)
            grid.getRowConstraints().add(new RowConstraints(rowHeight, rowHeight, rowHeight, Priority.SOMETIMES, VPos.CENTER, true));
        grid.getRowConstraints().add(new RowConstraints(15, 15, 15, Priority.SOMETIMES, VPos.CENTER, true));
        grid.getRowConstraints().add(new RowConstraints(15, 15, 15, Priority.SOMETIMES, VPos.CENTER, true));
        grid.getRowConstraints().add(new RowConstraints(15, 15, 15, Priority.SOMETIMES, VPos.CENTER, true));
        Scene data = new Scene(grid);

        Text sceneTitle = new Text("Заполните информацию о стенах и отверстиях");
        sceneTitle.setTextAlignment(TextAlignment.CENTER);
        sceneTitle.setWrappingWidth(200);
        sceneTitle.setFont(new Font(18));
        grid.add(sceneTitle, 1, 0);
        walls.clear();
        holes_height.clear();
        holes_length.clear();
        for (int i = 1; i <= walls_count; i++) {
            grid.add(new Label("Длина стены №" + i + ": "), 0, i);
            walls.add(new TextField());
            grid.add(walls.get(i-1), 1, i);
        }
        for (int i = 1; i <= holes_count; i++) {
            grid.add(new Label("Длина и высота отверстия №" + i + ": "), 0, i + walls_count);
            holes_length.add(new TextField());
            holes_height.add(new TextField());
            grid.add(holes_length.get(i - 1), 1, i + walls_count);
            grid.add(holes_height.get(i - 1), 2, i + walls_count);
        }
        Button button = new Button("Подтвердить");
        button.setOnAction(e -> {
            int count = 0;
            List<TextField> unitedList = new ArrayList<>();
            unitedList.addAll(walls);
            unitedList.addAll(holes_length);
            unitedList.addAll(holes_height);
            for (TextField textField : unitedList) {
                double length = -1;
                try {
                    length = Double.parseDouble(textField.getText());
                } catch (Exception exception) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Все вводимые величины должны быть положительными числами");
                    alert.showAndWait();
                    return;
                }
                if (length > 0)
                    count++;

            }
            for (TextField textField : holes_height) {
                double holeHeight = Double.parseDouble(textField.getText());
                if (holeHeight > height) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Высота отверстия не может превышать высоту комнаты");
                    alert.showAndWait();
                    return;
                }
            }
            if (unitedList.size() == count) {
                createCalc(walls, holes_height, holes_length);
                submit.setText("Изменить");
                calculator.setVisible(true);
                dataWindow.close();
            }
        });
        grid.add(button, 2, walls_count + holes_count + 2);

        dataWindow.setTitle("Заполните необходимые данные");
        dataWindow.setScene(data);
        dataWindow.setResizable(true);
        dataWindow.showAndWait();
    } //Окно ввода данных

    private void createCalc(List<TextField> wallsTF, List<TextField> holes_widthTF, List<TextField> holes_lengthTF) {
        List<Rectangle> walls = new ArrayList<>();
        List<Rectangle> holes = new ArrayList<>();

        for (TextField textField : wallsTF) {
            try {
                walls.add(new Rectangle(Double.parseDouble(textField.getText()), height));
            } catch (Exception e) {       //Поиск исключений
                e.printStackTrace();
            }
        }
        for (int i = 0; i < holes_widthTF.size(); i++) {
            holes.add(new Rectangle(Double.parseDouble(holes_widthTF.get(i).getText()), Double.parseDouble(holes_lengthTF.get(i).getText())));
        }
        try {
            calc = new Calculator(height, walls, holes);
        } catch (Exception e) {       //Поиск исключений
            e.printStackTrace();
        }
    } //Создание калькулятора

    public void calculateWallpaper() {
        if (calc != null) {
            double usefulSquare;
            try {
                usefulSquare = calc.getUsefulSquare(); //Расчет боковой площади комнаты без окон и дверей
            } catch (Exception e) {      //Поиск исключений
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Общая площать окон и дверей не может привышать сумарную площать стен. Введенные данные некорректны.");
                alert.showAndWait();
                return;
            }
            if (wallpaperRadioSelect.isSelected())
                wallpaperResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(wallpaperSelector.getValue()));
            if (wallpaperRadioType.isSelected()) {
                Rectangle customWallpaper = null;
                double length;
                double width;
                try {
                    length = Double.parseDouble(wallpaperLength.getText());
                    width = Double.parseDouble(wallpaperWidth.getText());
                    if (length > 0 && width > 0)
                        customWallpaper = new Rectangle(length, width);
                } catch (Exception e) {      //Поиск исключений
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Все вводимые величины должны быть положительными числами");
                    alert.showAndWait();
                    return;
                }
                if (customWallpaper != null)
                    wallpaperResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(customWallpaper));
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Длина и ширина должны быть положительными числами");
                    alert.showAndWait();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);      //Поиск исключений
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Заполните данные о комнате");
            alert.showAndWait();
        }
    } //Рассчитать обои

    public void clearWallpaper() {
        wallpaperWidth.setText("");
        wallpaperLength.setText("");
        wallpaperResult.setText("");
    } //Очистить обои

    public void calculateTile() {
        if (calc != null) {
            double usefulSquare;
            try {
                usefulSquare = calc.getUsefulSquare(); //Расчет боковой площади комнаты без окон и дверей
            } catch (Exception e) {      //Поиск исключений
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Общая площать окон и дверей не может привышать сумарную площать стен. Введенные данные некорректны.");
                alert.showAndWait();
                return;
            }
            if (tileRadioSelect.isSelected())
                tileResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(tileSelector.getValue()));
            if (tileRadioType.isSelected()) {
                Rectangle customTile = null;
                double length;
                double width;
                try {
                    length = Double.parseDouble(tileLength.getText());
                    width = Double.parseDouble(tileWidth.getText());
                    if (length > 0 && width > 0)
                        customTile = new Rectangle(length, width);
                } catch (Exception e) {      //Поиск исключений
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Все вводимые величины должны быть положительными числами");
                    alert.showAndWait();
                    return;
                }
                if (customTile != null)
                    tileResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(customTile));
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Длина и ширина должны быть положительными числами");
                    alert.showAndWait();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);      //Поиск исключений
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Заполните данные о комнате");
            alert.showAndWait();
        }
    } //Рассчитать плитку

    public void clearTile() {
        tileWidth.setText("");
        tileLength.setText("");
        tileResult.setText("");
    } //Очистить плитку

    public void calculatePanel() {
        if (calc != null) {
            double usefulSquare;
            try {
                usefulSquare = calc.getUsefulSquare(); //Расчет боковой площади комнаты без окон и дверей
            } catch (Exception e) {      //Поиск исключений
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Общая площать окон и дверей не может привышать сумарную площать стен. Введенные данные некорректны.");
                alert.showAndWait();
                return;
            }
            if (panelRadioSelect.isSelected())
                panelResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(panelSelector.getValue()));
            if (panelRadioType.isSelected()) {
                Rectangle customPanel = null;
                double length;
                double width;
                try {
                    length = Double.parseDouble(panelLength.getText());
                    width = Double.parseDouble(panelWidth.getText());
                    if (length > 0 && width > 0)
                        customPanel = new Rectangle(length, width);
                } catch (Exception e) {      //Поиск исключений
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Все вводимые величины должны быть положительными числами");
                    alert.showAndWait();
                    return;
                }
                if (customPanel != null)
                    wallpaperResult.setText("Для оклейки всей комнаты вам понадобится " + calc.getCountMaterials(customPanel));
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Длина и ширина должны быть положительными числами");
                    alert.showAndWait();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);      //Поиск исключений
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Заполните данные о комнате");
            alert.showAndWait();
        }
    } //Рассчитать панели

    public void clearPanel() {
        panelWidth.setText("");
        panelLength.setText("");
        panelResult.setText("");
    } //Очистить панели
}
