module com.example.comicapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires javafx.graphics;

    opens com.example.comicapp to javafx.fxml, com.google.gson;
    exports com.example.comicapp;
}