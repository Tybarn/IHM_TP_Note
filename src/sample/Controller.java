package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    Paint paint_save = null; // Used only to give back to the shape its first color after clicked on it
    Shape saved_shape = null;

    // Event when we select a shape --------------------------------------------------
    EventHandler<MouseEvent> ev_select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node obj = (Node) event.getSource();
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();

            if(r != null && r.getText().equals("Select / Move")) {
                obj.setCursor(Cursor.MOVE);
                if(obj instanceof Ellipse || obj instanceof Rectangle) {
                    Shape sh = (Shape) obj;
                    paint_save = sh.getFill();
                    if (!paint_save.equals(Color.BLACK))
                        sh.setFill(Color.BLACK);
                    else
                        sh.setFill(Color.GREY);
                    saved_shape = sh;
                }
                else {
                    Line line = (Line) obj;
                    paint_save = line.getStroke();
                    if (!paint_save.equals(Color.BLACK))
                        line.setStroke(Color.BLACK);
                    else
                        line.setStroke(Color.GREY);
                    saved_shape = line;
                }
                btnDelete.setDisable(false);
                btnClone.setDisable(false);
            }
        }
    };

    // Event when we release the shape ------------------------------------------------
    EventHandler<MouseEvent> ev_end_select = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Shape obj = (Shape) event.getSource();
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
            if(r != null && r.getText().equals("Select / Move")) {
                if (obj instanceof Ellipse || obj instanceof Rectangle)
                    obj.setFill(paint_save);
                else
                    obj.setStroke(paint_save); // Line
                obj.setCursor(Cursor.DEFAULT);
            }
        }
    };

    // Event when we want to move a shape ----------------------------------------------
    EventHandler<MouseEvent> ev_drag_rect = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
            if (event.getSceneX() >= paneShapes.getLayoutX() && event.getSceneX() <= (paneShapes.getLayoutX() + paneShapes.getWidth())
                    && event.getSceneY() >= paneShapes.getLayoutY() && event.getSceneY() <= (paneShapes.getLayoutY() + paneShapes.getHeight())) {
                if (r != null && r.getText().equals("Select / Move")) {
                    Rectangle rg = (Rectangle) event.getSource();
                    rg.setX(event.getX() - (rg.getWidth() / 2));
                    rg.setY(event.getY() - (rg.getHeight() / 2));
                }
            }
        }
    };
    EventHandler<MouseEvent> ev_drag_ellip = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
            if (event.getSceneX() >= paneShapes.getLayoutX() && event.getSceneX() <= (paneShapes.getLayoutX() + paneShapes.getWidth())
                    && event.getSceneY() >= paneShapes.getLayoutY() && event.getSceneY() <= (paneShapes.getLayoutY() + paneShapes.getHeight())) {
                if (r != null && r.getText().equals("Select / Move")) {
                    Ellipse e = (Ellipse) event.getSource();
                    e.setCenterX(event.getX());
                    e.setCenterY(event.getY());
                }
            }
        }
    };
    EventHandler<MouseEvent> ev_drag_line = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            RadioButton r = (RadioButton) groupRb.getSelectedToggle();
            if (event.getSceneX() >= paneShapes.getLayoutX() && event.getSceneX() <= (paneShapes.getLayoutX() + paneShapes.getWidth())
                    && event.getSceneY() >= paneShapes.getLayoutY() && event.getSceneY() <= (paneShapes.getLayoutY() + paneShapes.getHeight())) {
                if (r != null && r.getText().equals("Select / Move")) {
                    Line ln = (Line) event.getSource();
                    ln.setTranslateX(event.getSceneX() - (paneShapes.getLayoutX() + (ln.getStartX() + ln.getEndX()) / 2));
                    ln.setTranslateY(event.getSceneY() - (paneShapes.getLayoutY() + (ln.getStartY() + ln.getEndY()) / 2));
                }
            }
        }
    };


    /**
     * Initialize the scene of the application
     */
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

        // Event on Color changing in colorpicker
        colorpicker.setOnAction(event -> {
            if(saved_shape != null) {
                if(saved_shape instanceof Line)
                    saved_shape.setStroke(colorpicker.getValue());
                else
                    saved_shape.setFill(colorpicker.getValue());
            }
        });

        // Event on Radiobutton
        groupRb.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(oldValue != null)
                    oldValue.setSelected(false);
                btnClone.setDisable(true);
                btnDelete.setDisable(true);
                saved_shape = null;
                paint_save = null;
            }
        });

        // Event on Delete button
        btnDelete.setOnAction(event -> {
            if(saved_shape != null) {
                paneShapes.getChildren().remove(saved_shape);
                saved_shape = null;
                paint_save = null;
                btnDelete.setDisable(true);
                btnClone.setDisable(true);
            }
        });

        // Event on Clone button
        btnClone.setOnAction(event -> {
            Ellipse ep = null;
            Rectangle rect = null;
            Line line = null;
            if(saved_shape != null) {
                if(saved_shape instanceof Ellipse) {
                    // Creation of a new Ellipse
                    ep = new Ellipse(((Ellipse) saved_shape).getCenterX(), ((Ellipse) saved_shape).getCenterY(),
                            ((Ellipse) saved_shape).getRadiusX(), ((Ellipse) saved_shape).getRadiusY());
                    ep.setFill(saved_shape.getFill());

                    // Event
                    ep.setOnMousePressed(ev_select);
                    ep.setOnMouseReleased(ev_end_select);
                    ep.setOnMouseDragged(ev_drag_ellip);

                    paneShapes.getChildren().add(ep);
                }
                else if(saved_shape instanceof Rectangle) {
                    // Creation of a new Rectangle
                    rect = new Rectangle(((Rectangle) saved_shape).getX(), ((Rectangle) saved_shape).getY(),
                            ((Rectangle) saved_shape).getWidth(), ((Rectangle) saved_shape).getHeight());
                    rect.setFill(saved_shape.getFill());

                    // Event
                    rect.setOnMousePressed(ev_select);
                    rect.setOnMouseReleased(ev_end_select);
                    rect.setOnMouseDragged(ev_drag_rect);

                    paneShapes.getChildren().add(rect);
                }
                else {
                    // Creation of a new Line
                    line = new Line(((Line) saved_shape).getStartX(), ((Line) saved_shape).getStartY(),
                            ((Line) saved_shape).getEndX(), ((Line) saved_shape).getEndY());
                    line.setStroke(saved_shape.getStroke());

                    // Event
                    line.setOnMousePressed(ev_select);
                    line.setOnMouseReleased(ev_end_select);
                    line.setOnMouseDragged(ev_drag_line);

                    paneShapes.getChildren().add(line);
                }
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
                        rect.setOnMousePressed(ev_select);
                        rect.setOnMouseReleased(ev_end_select);
                        rect.setOnMouseDragged(ev_drag_rect);
                        paneShapes.getChildren().add(rect);
                        break;
                    case "Ellipse":
                        line_draw = false;
                        // Create the ellipse
                        ellipse = new Ellipse(x,y,50,30);
                        ellipse.setFill(colorpicker.getValue());
                        // Event to move it
                        ellipse.setOnMousePressed(ev_select);
                        ellipse.setOnMouseReleased(ev_end_select);
                        ellipse.setOnMouseDragged(ev_drag_ellip);
                        paneShapes.getChildren().add(ellipse);
                        break;
                    case "Line":
                        if(line_draw) {
                            // Create the line
                            line = new Line(line_startX, line_startY, event.getX(), event.getY());
                            line.setStroke(colorpicker.getValue());
                            // Event to move it
                            line.setOnMousePressed(ev_select);
                            line.setOnMouseReleased(ev_end_select);
                            line.setOnMouseDragged(ev_drag_line);
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