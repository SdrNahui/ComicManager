package com.example.comicapp;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController implements ServiceAware {
    @FXML private ImageView iconMenu;
    @FXML private VBox menuContainer;
    @FXML private StackPane contentContainer;
    private MenuController menuController;
    private boolean menuVisible = false;
    private ComicService service;

    public void setService(ComicService service) {
        this.service = service;
        cargarVista("home.fxml");
    }
    @FXML public void initialize() {
        cargarMenu();
        menuContainer.setTranslateY(-1000);
        configurarAnimacionesMenu();
    }
    public void cargarVista(String nombre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));
            Parent vista = loader.load();
            Object controller = loader.getController();
            if(controller instanceof ServiceAware){
                ((ServiceAware) controller).setService(service);
            }
            contentContainer.getChildren().setAll(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void cargarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuView.fxml"));
            Parent menu = loader.load();
            menuController = loader.getController();  // <-- guardamos referencia
            menuController.setMainController(this);
            menuContainer.getChildren().setAll(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMenu() {
        if (menuVisible) return;
        // mostrar textos
        if (menuController != null) {
            menuController.setTextosVisibles(true);
        }
        TranslateTransition t = new TranslateTransition(Duration.millis(200), menuContainer);
        t.setToY(0);
        t.play();
        menuVisible = true;
    }

    private void ocultarMenu() {
        if (!menuVisible) return;
        //ocultar textos
        if (menuController != null) {
            menuController.setTextosVisibles(false);
        }
        TranslateTransition t = new TranslateTransition(Duration.millis(200), menuContainer);
        t.setToY(-1000);
        t.play();
        menuVisible = false;
    }

    private void configurarAnimacionesMenu() {
        iconMenu.setOnMouseClicked(e -> {
            if (menuVisible) ocultarMenu();
            else mostrarMenu();
        });
        //cerrar
        contentContainer.setOnMouseClicked(e -> {
            if (menuVisible) ocultarMenu();
        });
    }
}