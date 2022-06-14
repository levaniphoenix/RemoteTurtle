package com.levaniphoenix.remoteturtle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import spark.Spark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("window.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Remote Turtle");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Properties properties = new Properties();
        URL url = AppController.class.getResource("turtle.properties");

        properties.setProperty("turtle.direction", AppController.direction.toString());
        properties.setProperty("turtle.x", String.valueOf(AppController.turtle.getTranslateX()));
        properties.setProperty("turtle.y", String.valueOf(AppController.turtle.getTranslateY()));
        properties.setProperty("turtle.z", String.valueOf(AppController.turtle.getTranslateZ()));

        File file = new File(url.toURI().getPath());
        FileOutputStream out = new FileOutputStream(file);
        properties.store(out,null);
        out.close();

        super.stop();
        Spark.stop();
    }

    public static void main(String[] args) {
        Spark.webSocket("/", WebSocketServer.class);
        Spark.init();
        launch();
    }

}