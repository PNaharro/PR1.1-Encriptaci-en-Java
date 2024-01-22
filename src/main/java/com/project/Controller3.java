package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller3 {
     @FXML
    private Button ok;
     @FXML
    private void ok(ActionEvent event) {
        UtilsViews.setViewAnimating("layout1");
    }
}
