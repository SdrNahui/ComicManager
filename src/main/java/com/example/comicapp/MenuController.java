package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML private VBox menuTextos;
    @FXML private Label labelHome;
    @FXML private Label labelAgregar;
    @FXML private Label labelBiblo;
    @FXML private Label labelTotal;
    @FXML private ImageView iconHome;
    @FXML private ImageView iconAgregar;
    @FXML private ImageView iconBiblo;
    @FXML private ImageView iconTotal;

    private MainController main;

    public void setMainController(MainController main){
        this.main = main;
    }

    @FXML
    public void initialize() {
        // Por defecto el menú está cerrado → ocultamos textos
        setTextosVisibles(false);

        iconHome.setOnMouseClicked(e -> main.cargarVista("home.fxml"));
        iconAgregar.setOnMouseClicked(e ->main.cargarVista("agregarView.fxml"));
        iconBiblo.setOnMouseClicked(e -> main.cargarVista("listaView.fxml"));
    }

    public void setTextosVisibles(boolean visible){
        labelHome.setVisible(visible);
        labelAgregar.setVisible(visible);
        labelBiblo.setVisible(visible);
        labelTotal.setVisible(visible);
    }
}
