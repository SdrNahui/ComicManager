package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    private Comic comicEditado;
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
    public void editarComic(Comic comic) {
        this.comicEditado = comic;

        txtTitulo.setText(comic.getTitulo());
        txtEditorial.setText(comic.getEditorial());
        txtPrecio.setText(String.valueOf(comic.getPrecio()));
        loTengo.setSelected(comic.getLoTengo());
        txtNotas.setText(comic.getNotas());
        cmbTipo.setDisable(true);

        // detectar tipo real
        if (comic instanceof Libro) {
            cmbTipo.setValue("Libro / One-shot");
        }
        else if (comic instanceof TomoRecopilatorio t) {
            cmbTipo.setValue("Tomo recopilatorio");
            txtDesde.setText(String.valueOf(t.getDesde()));
            txtHasta.setText(String.valueOf(t.getHasta()));
        }
        else if (comic instanceof Evento e) {
            cmbTipo.setValue("Evento");
            txtIncluye.setText(e.getIncluye());
        }

        lblMsj.setText("Editando comic");
    }


    @FXML
    public void guardar() {
        try {
            String titulo = txtTitulo.getText();
            String editorial = txtEditorial.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            boolean tengo = loTengo.isSelected();
            String notas = txtNotas.getText();

            if (comicEditado == null) {
                // ====== AGREGAR ======
                Comic c;
                String tipo = cmbTipo.getValue();

                switch (tipo) {
                    case "Libro / One-shot" ->
                            c = new Libro(titulo, editorial, precio, tengo, notas);

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

            } else {
                // ====== EDITAR ======
                comicEditado.setTitulo(titulo);
                comicEditado.setEditorial(editorial);
                comicEditado.setPrecio(precio);
                comicEditado.setLoTengo(tengo);
                comicEditado.setNotas(notas);

                if (comicEditado instanceof TomoRecopilatorio t) {
                    t.setDesde(Integer.parseInt(txtDesde.getText()));
                    t.setHasta(Integer.parseInt(txtHasta.getText()));
                }

                if (comicEditado instanceof Evento e) {
                    e.setIncluye(txtIncluye.getText());
                }

                service.guardar(); // persistir cambios
                lblMsj.setText("Editado correctamente");
                Stage stage = (Stage) txtTitulo.getScene().getWindow();
                stage.close();
            }

        } catch (Exception e) {
            lblMsj.setText("Error: " + e.getMessage());
        }
    }



}