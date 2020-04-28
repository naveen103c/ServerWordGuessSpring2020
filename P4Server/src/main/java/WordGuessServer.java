import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class WordGuessServer extends Application {

	HashMap<String, Scene> sceneMap;
	ListView<String> listItems;
	ServerLogic serverConnection;
	TextField portNumber;

	// set up initial scene for entering portNumber
	Button serverChoice;
	Text portInstructions = new Text();

	// set up background images
	Image background = new Image("initialServerBackground.jpg");
	BackgroundImage backgroundImage1 = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
	Background serverBackgroundImage = new Background(backgroundImage1);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// ---------------------------------------------------
		// --------------- INITIALIZE SCENEMAP ---------------
		// ---------------------------------------------------
		// initialize the hashmap to store scenes
		sceneMap = new HashMap<String, Scene>();
		portNumber = new TextField();

		// prevent user from being able to enter more than 4 characters for port
		portNumber.setTextFormatter(new TextFormatter<String>(change ->
				change.getControlNewText().length() <= 4 ? change : null));

		// initialize the ListView
		listItems = new ListView<String>();

		// load hashmap with two server GUI's
		sceneMap.put("start", startScreenGui());
		sceneMap.put("server",  createServerGui());

		// display initial server GUI
		primaryStage.setScene(sceneMap.get("start"));
		primaryStage.setTitle("(server) Playing word guess!!!");
		primaryStage.show();

		// ----------------------------------------------
		// --------------- EVENT HANDLERS ---------------
		// ----------------------------------------------
		// event handler for start server button
		serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("Server");

			serverConnection = new ServerLogic(data -> {
				Platform.runLater(()->{

					// make listItems autoscroll
					int index = listItems.getItems().size();
					listItems.scrollTo(index);

					listItems.getItems().add(data.toString());
				});
			}, Integer.parseInt(portNumber.getText())); // get port number
		});

		//if window is closed the program exits out
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

	}


	// ----------------------------------------------
	// --------------- SCENE BUILDERS ---------------
	// ----------------------------------------------
	// create initial GUI
	private Scene startScreenGui() {

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

		// create new scene with port # input, textfield, and background image
		return new Scene(startPane, 500, 600);
	}

	// create the server GUI
	public Scene createServerGui() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		// pane.setStyle("-fx-background-color: gray");
		pane.setCenter(listItems);

		return new Scene(pane, 500, 400);
	}

}
