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
    private CardController cardSeleccionada;

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
        cardSeleccionada = null;

        for (Comic c : service.getUltimos(3)) {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("cardsView.fxml"));
                Parent card = loader.load();
                CardController cc = loader.getController();

                cc.setData(
                        c,
                        () -> seleccionarCard(cc),   // click izquierdo
                        null,
                        null// home NO elimina
                );

                contenedorUltimos.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarCard(CardController nueva) {
        if (cardSeleccionada != null) {
            cardSeleccionada.deseleccionar();
        }
        cardSeleccionada = nueva;
        cardSeleccionada.seleccionar();
    }
}
