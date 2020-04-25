import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WordGuessServer extends Application {

	// set up initial scene for entering portNumber
	Button serverChoice;
	Text portInstructions = new Text();
	TextField portNumber = new TextField("5555");

	// set up background images
	Image background = new Image("initialServerBackground.jpg");
	BackgroundImage backgroundImage1 = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
	Background serverBackgroundImage = new Background(backgroundImage1);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(server) Playing word guess!!!");

		// adjust serverChoice button dimensions
		serverChoice = new Button("Start Server!");
		serverChoice.setStyle("-fx-pref-width: 100px");

		// create new text field
		portNumber.setMaxWidth(150);
		portNumber.setAlignment(Pos.CENTER);

		// create text input instructions
		portInstructions.setText("Please enter port number");
		portInstructions.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		portInstructions.setFill(Color.DARKGOLDENROD);

		// set up VBox for port Entry
		VBox portEntry = new VBox(0, portInstructions, portNumber, serverChoice);
		portEntry.setAlignment(Pos.CENTER);

		// create startPane with serverChoiceButton and background
		BorderPane startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setTop(portEntry);
		startPane.setBackground(serverBackgroundImage);

		// create new scene with port # input, and textfield
		Scene scene = new Scene(startPane,600,600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
