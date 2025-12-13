package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class CardController {
    @FXML private Label lblTitulo;
    @FXML private Label lblInfo;


    public void setData(Comic comic) {
        lblTitulo.setText(comic.getTitulo());

        String estado = comic.getLoTengo() ? " ✔" : " ❌";

        lblInfo.setText(" #" + comic.getNumero() + " • " + comic.getEditorial() + estado);
    }

}
