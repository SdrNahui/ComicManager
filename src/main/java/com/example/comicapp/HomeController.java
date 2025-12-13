package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class HomeController implements ServiceAware {

    @FXML private Label lblTotalComics;
    @FXML private Label lblEditoriales;
    @FXML private Label lblNuevos;
    @FXML private FlowPane contenedorUltimos;

    private ComicService service;

    @Override
    public void setService(ComicService service) {
        this.service = service;

        lblTotalComics.setText(String.valueOf(service.getListaComics().size()));
        lblEditoriales.setText(String.valueOf(service.contarEditoriales()));
        lblNuevos.setText(String.valueOf(service.getNuevosDelMes()));

        cargarUltimos();
    }
    @FXML public void initialize(){
        System.out.println("HOME CARGADO");
    }

    private void cargarUltimos() {
        contenedorUltimos.getChildren().clear();

        for (Comic c : service.getUltimos(3)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cardsView.fxml"));
                Parent card = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(c);

                contenedorUltimos.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
