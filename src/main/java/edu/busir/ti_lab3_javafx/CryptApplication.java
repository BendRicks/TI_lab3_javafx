package edu.busir.ti_lab3_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CryptApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CryptApplication.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 490, 280);
        stage.setTitle("Шифрование Эль-Гамаля");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}