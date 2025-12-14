package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AgregarController implements ServiceAware{
    @FXML private TextField txtTitulo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtEditorial;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox  loTengo;
    @FXML private TextArea txtNotas;
    @FXML private Label lblMsj;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtDesde;
    @FXML private TextField txtHasta;
    @FXML private TextArea txtIncluye;
    @FXML private HBox boxTomo;
    private ComicService service;
    public void setService(ComicService service){
        this.service = service;
    }
    @FXML
    private void initialize() {
        // arrancan ocultos
        boxTomo.setVisible(false);
        boxTomo.setManaged(false);

        txtIncluye.setVisible(false);
        txtIncluye.setManaged(false);

        cmbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            switch (newVal) {
                case "Libro / One-shot" -> {
                    boxTomo.setVisible(false);
                    boxTomo.setManaged(false);

                    txtIncluye.setVisible(false);
                    txtIncluye.setManaged(false);
                }
                case "Tomo recopilatorio" -> {
                    boxTomo.setVisible(true);
                    boxTomo.setManaged(true);

                    txtIncluye.setVisible(false);
                    txtIncluye.setManaged(false);
                }
                case "Evento" -> {
                    boxTomo.setVisible(false);
                    boxTomo.setManaged(false);

                    txtIncluye.setVisible(true);
                    txtIncluye.setManaged(true);
                }
            }
        });
    }

    @FXML
    public void guardar() {
        try {
            String titulo = txtTitulo.getText();
            String editorial = txtEditorial.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            boolean tengo = loTengo.isSelected();
            String notas = txtNotas.getText();

            Comic c;

            String tipo = cmbTipo.getValue();
            if (tipo == null) {
                throw new RuntimeException("Seleccioná un tipo");
            }

            switch (tipo) {
                case "Libro / One-shot" -> {
                    c = new Libro(titulo, editorial, precio, tengo, notas);
                }
                case "Tomo recopilatorio" -> {
                    int desde = Integer.parseInt(txtDesde.getText());
                    int hasta = Integer.parseInt(txtHasta.getText());
                    c = new TomoRecopilatorio(
                            titulo, editorial, precio, tengo, notas, desde, hasta
                    );
                }
                case "Evento" -> {
                    String incluye = txtIncluye.getText();
                    c = new Evento(titulo, editorial, precio, tengo, notas, incluye);
                }
                default -> throw new RuntimeException("Tipo inválido");
            }

            service.agregarComic(c);
            lblMsj.setText("Agregado correctamente");

        } catch (Exception e) {
            lblMsj.setText("Error: " + e.getMessage());
        }
    }


}