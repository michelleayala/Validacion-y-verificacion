package Inicio;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static  Stage MainStage;
    public static Stage appStage = new Stage();


    @Override
    public void start(Stage primaryStage) throws Exception{
        MainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Scene escena = new Scene(root);
        primaryStage.setScene(escena);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
