package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private ToggleGroup groupRb = new ToggleGroup();

    public void initialize(URL location, ResourceBundle resources) {
        // Init RadioButtons
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

            RadioButton rb = (RadioButton) groupRb.getSelectedToggle();
            if(rb != null) {
                switch(rb.getText()) {
                    case "Rectangle":
                        rect = new Rectangle(x,y,50,50);
                        rect.setFill(colorpicker.getValue());
                        paneShapes.getChildren().add(rect);
                        break;
                    case "Ellipse":
                        ellipse = new Ellipse(x,y,50,50);
                        ellipse.setFill(colorpicker.getValue());
                        paneShapes.getChildren().add(ellipse);
                        break;
                    case "Select / Move":
                        break;
                }
            }
        });
    }
}
