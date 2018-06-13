package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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

    // Variables for selection of a item
    Paint paint_save = null;
    Shape save = null;

    // Event when we select a shape
    EventHandler<MouseEvent> ev_select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Object obj = event.getSource();
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();

            if(r != null && r.getText().equals("Select / Move")) {
                if(obj instanceof Ellipse) {
                    Ellipse ep = (Ellipse) obj;
                    paint_save = ep.getFill();
                    if (paint_save.equals(Color.BLACK))
                        ep.setFill(Color.BLACK);
                    else
                        ep.setFill(Color.GREY);
                    save = ep;
                }
                else if(obj instanceof Rectangle) {
                    Rectangle rect = (Rectangle) obj;
                    paint_save = rect.getFill();
                    if (paint_save != Color.BLACK)
                        rect.setFill(Color.BLACK);
                    else
                        rect.setFill(Color.GREY);
                    save = rect;
                }
                else {
                    Line line = (Line) obj;
                    paint_save = line.getStroke();
                    if (line.getStroke() != Color.BLACK)
                        line.setStroke(Color.BLACK);
                    else
                        line.setStroke(Color.GREY);
                    save = line;
                }
            }
        }
    };

    // Event when we release the shape
    EventHandler<MouseEvent> ev_end_select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Shape obj = (Shape) event.getSource();
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
            if(r != null && r.getText().equals("Select / Move")) {
                if (obj instanceof Ellipse || obj instanceof Rectangle)
                    obj.setFill(paint_save);
                else
                    ((Line) obj).setStroke(paint_save);
            }
        }
    };

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
            // Position of the start point of the line we want to create
            double x = event.getX();
            double y = event.getY();

            // Temp variables
            Rectangle rect;
            Ellipse ellipse;
            Line line;

            // RadioButton selected
            RadioButton rb = (RadioButton) groupRb.getSelectedToggle();

            // Manage the from action from the selected radiobutton
            if(rb != null) {
                switch(rb.getText()) {
                    case "Rectangle":
                        line_draw = false;
                        // Create the Rectangle
                        rect = new Rectangle(x,y,100,50);
                        rect.setFill(colorpicker.getValue());
                        // Event to move it
                        rect.setOnMouseClicked(ev_select);
                        rect.setOnMouseReleased(ev_end_select);
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
                        ellipse.setOnMouseClicked(ev_select);
                        ellipse.setOnMouseReleased(ev_end_select);
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
                            line.setOnMouseClicked(ev_select);
                            line.setOnMouseReleased(ev_end_select);
                            line.setOnMouseDragged(ev -> {
                                RadioButton r = (RadioButton) groupRb.getSelectedToggle();
                                if(r != null && r.getText().equals("Select / Move")) {
                                    Line ln = (Line) ev.getSource();
                                    ln.setTranslateX(ev.getSceneX() - (paneShapes.getLayoutX() + (ln.getStartX() + ln.getEndX()) / 2));
                                    ln.setTranslateY(ev.getSceneY() - (paneShapes.getLayoutY() + (ln.getStartY() + ln.getEndY()) / 2));
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
