package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller0 {
    @FXML
    private void handleEncriptarButtonClick(ActionEvent event) {
        System.out.println("Encriptar arxiu clicado");
        UtilsViews.setViewAnimating("encriptar");

    }

    @FXML
    private void handleDesencriptarButtonClick(ActionEvent event) {
        System.out.println("Desencriptar arxiu clicado");
        UtilsViews.setViewAnimating("desencriptar");
    }
}