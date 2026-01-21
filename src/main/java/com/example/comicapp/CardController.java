package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

public class CardController {

    @FXML private Label lblTitulo;
    @FXML private Label lblInfo;
    @FXML private AnchorPane root;

    private Runnable onSelect;
    private Runnable onEliminar;
    private Runnable onEditar;

    public void setData(Comic comic, Runnable onSelect, Runnable onEliminar, Runnable onEditar) {
        this.onSelect = onSelect;
        this.onEliminar = onEliminar;
        this.onEditar = onEditar;
        lblTitulo.setText(comic.getTitulo());
        String estado = comic.getLoTengo() ? " ✔" : " ❌";
        lblInfo.setText(comic.getDescripcion() + estado);

        configurarEventos();
    }

    private void configurarEventos() {
        root.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && onSelect != null) {
                onSelect.run();
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem eliminar = new MenuItem("Eliminar");
        eliminar.setOnAction(e -> {
            if (onEliminar != null) onEliminar.run();
        });
        MenuItem editar = new MenuItem("Editar");
        editar.setOnAction(e -> {
            if(onEditar != null) onEditar.run();
        });

        menu.getItems().addAll(eliminar,editar);
        root.setOnContextMenuRequested(e ->
                menu.show(root, e.getScreenX(), e.getScreenY())
        );
    }

    //estilos
    public void seleccionar() {
        root.setStyle("""
            -fx-background-color: #1E90FF;
            -fx-background-radius: 10;
        """);
    }

    public void deseleccionar() {
        root.setStyle("""
            -fx-background-color: #0D2A63;
            -fx-background-radius: 10;
        """);
    }
}
