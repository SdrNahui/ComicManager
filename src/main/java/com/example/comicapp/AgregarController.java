package com.example.comicapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AgregarController implements ServiceAware{
    @FXML private TextField txtTitulo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtEditorial;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox  loTengo;
    @FXML private TextArea txtNotas;
    @FXML private Label lblMsj;
    @FXML private Button btnGuardar;
    private ComicService service;
    public void setService(ComicService service){
        this.service = service;
    }
    @FXML public void guardar(){
        try{
            Comic c =new Comic(txtTitulo.getText(),
                    Integer.parseInt(txtNumero.getText()),
                    txtEditorial.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    loTengo.isSelected(), txtNotas.getText()
            );
            service.agregarComic(c);
            lblMsj.setText("Comic agregado correctamente");
            txtNumero.clear();
            txtPrecio.clear();
            txtNotas.clear();
            loTengo.setSelected(false);

        }catch (Exception e){
            lblMsj.setText("Error " + e.getMessage());
        }
    }

}