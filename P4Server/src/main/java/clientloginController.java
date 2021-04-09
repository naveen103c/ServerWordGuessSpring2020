

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class clientloginController {
    
    @FXML
    TextField name,portnum;
    @FXML
    Button startGame;
    public Stage stage2;
    
    @FXML
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage2 = new Stage();
            stage2.initStyle(StageStyle.DECORATED);
            stage2.setScene(new Scene(root1));
            stage2.setTitle("Client Side - Word Guess Game");
            stage2.show();

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (IOException ex) {
            Logger.getLogger(clientloginController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Some Error Occured");
        }
    }
    
}
