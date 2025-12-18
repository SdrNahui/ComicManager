package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ListaController implements ServiceAware{
    @FXML private FlowPane contenedorCards;
    @FXML private TextField txtBuscar;
    @FXML private Label lblVacio;
    private ComicService service;
    private CardController cardSeleccionada;

     public void setService(ComicService service) {
         this.service = service;
         if (contenedorCards != null) {
             cargarCards(this.service.getListaComics());
         }
     }
     @FXML
     public  void initialize(){
        txtBuscar.textProperty().addListener((observableValue, old, nuevo) -> buscar());
     //   buscar();
     }


    private void cargarCards(List<Comic> lista) {
        contenedorCards.getChildren().clear();
        cardSeleccionada = null;

        for (Comic c : lista) {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("cardsView.fxml"));
                Parent card = loader.load();
                CardController cc = loader.getController();

                cc.setData(
                        c,
                        () -> seleccionarCard(cc),
                        () -> confirmarEliminar(c),
                        () -> abrirEditor(c)
                );

                contenedorCards.getChildren().add(card);
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

    private void confirmarEliminar(Comic comic) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar comic");
        alert.setHeaderText("¿Eliminar este comic?");
        alert.setContentText(comic.getTitulo());

        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                service.eliminarComic(comic);
                cargarCards(service.getListaComics());
            }
        });
    }
    private void abrirEditor(Comic comic) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("agregarView.fxml"));
            Parent root = loader.load();
            AgregarController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.editarComic(comic);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("nightwing-logo.png")));

            stage.setTitle("Editar comic");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            // refrescar lista al cerrar
            cargarCards(service.getListaComics());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buscar() {
        List<Comic> datos;
        if (service == null) return;
        String filtro = txtBuscar.getText().toLowerCase();
        if (filtro == null || filtro.isEmpty()) {
            datos = service.getListaComics();
        } else {
            datos = service.buscarInteligente(filtro);
        }
        cargarCards(datos);
        lblVacio.setVisible(datos.isEmpty());
    }
}
