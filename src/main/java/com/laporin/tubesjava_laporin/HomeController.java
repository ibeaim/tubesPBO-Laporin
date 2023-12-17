package com.laporin.tubesjava_laporin;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Object Based Programming Class");
    }

    public void home(Event event) {
        welcomeText.setText("");
    }

    public void exit(ActionEvent actionEvent) {
        welcomeText.setText("Ada tombol x di atas kanan");
    }
}