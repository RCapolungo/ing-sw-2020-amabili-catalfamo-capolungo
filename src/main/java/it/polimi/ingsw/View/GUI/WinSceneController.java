package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Network.Client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WinSceneController extends BoardController implements Initializable {

    String Player;

    @FXML
    Text WinPlayer = new Text();
    @FXML
    ImageView WinBanner = new ImageView();

    public WinSceneController(Client client, String state, String Player) {
        super(client, state);
        this.Player = Player;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadNameAndCards();
        LoadBoard();
        setUpButtons();
    }

    /**
     * Load the name of the player who has won.
     */
    public void setUpButtons() {
        WinPlayer.setText(Player + " is the winner");
        if(Player.equals(client.getNickname())) {
            WinBanner.setImage(new Image("/Images/gameGodFrameName1.png"));
        } else {
            WinBanner.setImage(new Image("/Images/gameGodFrameName2.png"));
        }
    }
}
