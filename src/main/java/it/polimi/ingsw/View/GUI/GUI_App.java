package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.Client;
import it.polimi.ingsw.Network.Client.GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GUI_App extends Application {

    private Client[] client = new Client[1];
    private GUI[] UI = new GUI[1];

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void init() throws IOException {
        UI[0] = new GUI();
        client[0] = new Client(UI[0]);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        UI[0].setPrimaryStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(GUI_App.class.getResource("/welcomeScene.fxml"));
        loader.setController(new WelcomeSceneController(client[0]));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                client[0].setActive(false);
                try {
                    client[0].killClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
