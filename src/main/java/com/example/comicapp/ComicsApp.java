package com.example.comicapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ComicsApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ComicService service = new ComicService();
        service.cargar();
        FXMLLoader fxmlLoader = new FXMLLoader(ComicsApp.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainController controller =fxmlLoader.getController();
        controller.setService(service);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("nightwing-logo.png")));
        stage.setTitle("Comics App");
        stage.setWidth(1100);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.show();
    }
public static void main(String[] args){
        launch();
}
}
