package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // CustomerView.fxml dosyamızı yüklüyoruz
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
        // Ekran boyutunu FXML'de belirlediğimiz gibi 400x300 yapıyoruz
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Online Shopping");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}