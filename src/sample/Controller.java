package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private RadioButton rbSelect;
    @FXML private RadioButton rbEllipse;
    @FXML private RadioButton rbRectangle;
    @FXML private RadioButton rbLine;
    @FXML private ColorPicker colorpicker;
    @FXML private Button btnDelete;
    @FXML private Button btnClone;
    @FXML private Pane paneShapes;

    // RadioButton group
    private ToggleGroup groupRb = new ToggleGroup();

    // All variables that concern the creation of a line
    private boolean line_draw = false;
    private double line_startX = 0, line_startY = 0;

    public void initialize(URL location, ResourceBundle resources) {
        // Adding RadioButtons to a ToggleGroup
        rbSelect.setToggleGroup(groupRb);
        rbEllipse.setToggleGroup(groupRb);
        rbRectangle.setToggleGroup(groupRb);
        rbLine.setToggleGroup(groupRb);

        // Init Buttons
        btnClone.setDisable(true);
        btnDelete.setDisable(true);

        // Init ColorPicker
        colorpicker.setValue(Color.BLUE);

        // Event on Radiobutton
        groupRb.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(oldValue != null)
                    oldValue.setSelected(false);
            }
        });

        // Event when click on pane
        paneShapes.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            Rectangle rect;
            Ellipse ellipse;
            Line line;

            RadioButton rb = (RadioButton) groupRb.getSelectedToggle();
            if(rb != null) {
                switch(rb.getText()) {
                    case "Rectangle":
                        line_draw = false;
                        // Create the Rectangle
                        rect = new Rectangle(x,y,50,50);
                        rect.setFill(colorpicker.getValue());
                        // Event to move it
                        rect.setOnMouseDragged(ev -> {
                            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
                            if(r != null && r.getText().equals("Select / Move")) {
                                Rectangle rg = (Rectangle) ev.getSource();
                                rg.setX(ev.getX());
                                rg.setY(ev.getY());
                            }
                        });
                        paneShapes.getChildren().add(rect);
                        break;
                    case "Ellipse":
                        line_draw = false;
                        // Create the ellipse
                        ellipse = new Ellipse(x,y,50,50);
                        ellipse.setFill(colorpicker.getValue());
                        // Event to move it
                        ellipse.setOnMouseDragged(ev -> {
                            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
                            if(r != null && r.getText().equals("Select / Move")) {
                                Ellipse e = (Ellipse) ev.getSource();
                                e.setCenterX(ev.getX());
                                e.setCenterY(ev.getY());
                            }
                        });
                        paneShapes.getChildren().add(ellipse);
                        break;
                    case "Line":
                        if(line_draw) {
                            // Create the line
                            line = new Line(line_startX, line_startY, event.getX(), event.getY());
                            line.setStroke(colorpicker.getValue());
                            // Event to move it
                            line.setOnMouseDragged(ev -> {
                                RadioButton r = (RadioButton) groupRb.getSelectedToggle();
                                if(r != null && r.getText().equals("Select / Move")) {
                                    Line ln = (Line) ev.getSource();
                                    System.out.println("X " + ln.getStartX());
                                    System.out.println("Y " + ln.getStartY());
                                    ln.setTranslateX((ev.getX() - ln.getStartX());
                                    ln.setStartY(ev.getY() - ln.getStartY());

                                }
                            });
                            paneShapes.getChildren().add(line);
                            line_draw = false;
                        }
                        else {
                            line_startX = event.getX();
                            line_startY = event.getY();
                            line_draw = true;
                        }
                        break;
                    case "Select / Move":
                        line_draw = false;
                        break;
                }
            }
        });
    }
}
