import com.sun.security.ntlm.Client;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Random;

public class ServerLogic {
    int portNum;
    int clientNum = 1; // The client number
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>(); // ArrayList of all client objects
    Consumer<Serializable> callback;
    TheServer server = new TheServer();
    ArrayList<String> gamesCategoryWords; // The ArrayList for all the Video Game words
    ArrayList<String> sportsCategoryWords; // The ArrayList for all the Sport words
    ArrayList<String> foodCategoryWords; // The ArrayList for all the Food words

    ServerLogic(Consumer<Serializable> call, int port) {
        callback = call;
        portNum = port;
        server.start();

        // initialize gamesCategoryWords and populate with words
        gamesCategoryWords = new ArrayList<>();
        gamesCategoryWords.add("DOOM");
        gamesCategoryWords.add("MINECRAFT");
        gamesCategoryWords.add("STARCRAFT");
        gamesCategoryWords.add("HALO");
        gamesCategoryWords.add("PORTAL");
        gamesCategoryWords.add("DESTINY");
        gamesCategoryWords.add("DIABLO");
        gamesCategoryWords.add("CUPHEAD");
        gamesCategoryWords.add("DIVINITY");
        gamesCategoryWords.add("WARFRAME");

        // initialize sportsCategoryWords and populate with words
        sportsCategoryWords = new ArrayList<>();
        sportsCategoryWords.add("VOLLEYBALL");
        sportsCategoryWords.add("HOCKEY");
        sportsCategoryWords.add("BOWLING");
        sportsCategoryWords.add("BASKETBALL");
        sportsCategoryWords.add("FOOTBALL");
        sportsCategoryWords.add("SOCCER");
        sportsCategoryWords.add("BASEBALL");
        sportsCategoryWords.add("TENNIS");
        sportsCategoryWords.add("GOLF");
        sportsCategoryWords.add("CRICKET");

        // initialize foodCategoryWords and populate with words
        foodCategoryWords = new ArrayList<>();
        foodCategoryWords.add("SOPES");
        foodCategoryWords.add("SAMOSAS");
        foodCategoryWords.add("DOLMA");
        foodCategoryWords.add("HALEEM");
        foodCategoryWords.add("PAVLOV");
        foodCategoryWords.add("SUSHI");
        foodCategoryWords.add("SHAKSHUKA");
        foodCategoryWords.add("RAMEN");
        foodCategoryWords.add("LAMINGTON");
        foodCategoryWords.add("QUESADILLA");
    }

    public class TheServer extends Thread {
        // start the server and wait until a client has connected
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(portNum)) {
                System.out.println("Server is waiting for a client!");
                while(true) {
                    ClientThread c = new ClientThread(mySocket.accept(), clientNum);
                    callback.accept("A client has connected!");
                    clients.add(c);
                    c.start();
                    clientNum++;
                }
            }
            catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }
    }

    public class ClientThread extends Thread {
        Socket connection;
        int number;
        ObjectInputStream in;
        ObjectOutputStream out;

        // Get the connection and client number.
        ClientThread(Socket s, int count) {
            this.connection = s;
            this.number = count;
        }

        public void run() {

            /* Start the connection between the server and client */
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }// end of try catch

            GuessInfo serverInfo = new GuessInfo(); // Initialize the GuessInfo that the server will use.
            GuessInfo playerInfo; // This will be the GuessInfo that the Client will send to the server.

            /* These will be the states the game will be at */
            boolean guess = true;
            boolean category = false;
            boolean gameOver = false;

            Random rand = new Random(); // Randomizor object

            int randomNum =  rand.nextInt(10); // Get a random number between 0-9 inclusive.
            ArrayList<String> words = new ArrayList<String>(); // ArrayList of words that the user has guessed and/or is guessing currently.
            String wordCreation = ""; // Creating the client's string using this variable.
            char letter; // The letter the player will be guessing.

            try {
                // Read the GuessInfo from the client, it'll always be a category.
                playerInfo = (GuessInfo) in.readObject();
                serverInfo.setCategories(playerInfo.getCategories().get(0)); // Add that category to the server.
            }
            catch (Exception e){
                System.out.println("Ain't workin' properly");
            }


            /* The next if-else branches check what the category is, depending on the category it'll retrieve get a random word and
            * add it to the ArrayList of words for the user to guess. */
            if(serverInfo.getCategories().get(0).equals("Video Games"))
                words.add(gamesCategoryWords.get(randomNum));
            else if(serverInfo.getCategories().get(0).equals("Sports"))
                words.add(sportsCategoryWords.get(randomNum));
            else
                words.add(foodCategoryWords.get(randomNum));

            // Create the hidden word into server's GuessInfo then send it to the client.
            serverInfo.setWord(words.get(0).length());
            try {
                out.writeObject(serverInfo);
            }
            catch(Exception e){
                System.out.println("Ain't workin' properly #2");
            }

            while(true) {
                try {
                    // In the guessing phase
                    if(guess) {

                        playerInfo = (GuessInfo) in.readObject(); // Waits for the player's response here

                        letter = playerInfo.getGuesses().get(playerInfo.getGuesses().size() - 1); // Store the response to the variable letter

                        // Check if the letter is unique from all the guesses the player made
                        if(playerInfo.getGuesses().indexOf(letter) != playerInfo.getGuesses().size() - 1){
                            callback.accept("Player " + number + " guessed this letter before...");
                            continue; // If not then try again.
                        }
                        // Else it's a new guess.
                        else {

                            serverInfo.setGuesses(letter); // Add the guess to the server GuessInfo's guesses ArrayList

                            // If the letter doesn't exist in the word
                            if(words.get(words.size() - 1).indexOf(letter) == -1){

                                serverInfo.setNumWrongGuesses(serverInfo.getNumWrongGuesses() + 1); // Increment the number of wrong guesses by one.
                                // If the number of wrong guesses goes over 6 then the player lost the round.
                                if(serverInfo.getNumWrongGuesses() == 6){

                                    serverInfo.setNumWordsGuessed(serverInfo.getNumWordsGuessed() + 1); // Increment the number of guessed words.
                                    serverInfo.clearGuesses(); // Cleared the guesses ArrayList.
                                    serverInfo.removeCategory(); // Removed the category that was chosen previously.

                                    callback.accept("Player " + number + " lost the guess round!");

                                    /* Update the current state of the game. */
                                    guess = false;
                                    category = true;
                                    gameOver = false;

                                    /* These integers will count the number of words the player have guessed based on the respective category */
                                    int gameCount = 0;
                                    int foodCount = 0;
                                    int sportCount = 0;

                                    int size = words.size(); // just to make it look better
                                    // This for loops just counts up the words based on the category (hopefully none are >= 4)
                                    for(int i = 0; i < size; i++){
                                        if(gamesCategoryWords.contains(words.get(i)))
                                            gameCount++;
                                        else if (foodCategoryWords.contains(words.get(i)))
                                            foodCount++;
                                        else if(sportsCategoryWords.contains(words.get(i)))
                                            sportCount++;
                                    }

                                    // If any of the categories have 3 words guessed on and they were all incorrect guesses then the player looses.
                                    if((gameCount == 3 && gamesCategoryWords.contains(words.get(size - 1)))
                                    || (foodCount == 3 && foodCategoryWords.contains(words.get(size - 1)))
                                    || (sportCount == 3 && sportsCategoryWords.contains(words.get(size - 1)))){
                                        callback.accept("Player " + number + " lost the game!");

                                        /* Update the state of the game. */
                                        guess = false;
                                        category = false;
                                        gameOver = true;
                                        serverInfo.setWord("game over");
                                    }

                                }

                                // Send the updated GuessInfo to the client.
                                out.reset();
                                out.writeObject(serverInfo);
                            }
                            // Else If the letter exists in the word being guessed
                            else{

                                // Get the current word from the server's GuessInfo.
                                wordCreation = serverInfo.getWord();

                                // Remake the word being guessed on by the player with the new guessed letter.
                                for(int i = 0; i < words.get(words.size() - 1).length(); i++){
                                    if(words.get(words.size() - 1).charAt(i) == letter){
                                        wordCreation = wordCreation.substring(0, i) + letter + wordCreation.substring(i+1);
                                    }
                                }
                                serverInfo.setWord(wordCreation); // Add the new word to the server's GuessInfo

                                // If the player guesses the entire word correctly.
                                if(wordCreation.equals(words.get(words.size() - 1))){
                                    callback.accept("Player " + number + " guessed the word correctly!");

                                    serverInfo.setNumWordsGuessed(serverInfo.getNumWordsGuessed() + 1); // Increment the number of guessed words.
                                    serverInfo.clearGuesses(); // Cleared the ArrayList of guesses.

                                    /* Update the state of the game */
                                    guess = false;
                                    category = true;
                                    gameOver = false;

                                    // If the player guessed correctly on all categories then the player wins.
                                    if(serverInfo.getCategories().size() == 3){
                                        guess = false;
                                        category = false;
                                        gameOver = true;
                                        callback.accept("Player " + number + " won the game!");
                                    }
                                }

                                out.reset();
                                out.writeObject(serverInfo); // Send the GuessInfo after the guess.
                            }
                        }
                    }
                    // If the current state is choosing a category.
                    else if(category){

                        playerInfo = (GuessInfo) in.readObject(); // Receive the player's GuessInfo.
                        // Add the new category chosen to the server's GuessInfo.
                        serverInfo.setCategories(playerInfo.getCategories().get(playerInfo.getCategories().size() - 1));

                        // Keep looping through the next set of instructions until the player gets a new unique word.
                        while(true) {
                            randomNum = rand.nextInt(10); // Get a new number between 0-9 inclusive.

                            int lastIndex = serverInfo.getCategories().size() - 1; // Get the index number of the server's category ArrayList.

                            String randWord;
                            /* Depending on the current chosen category, retrieve a new word from that category and store it into randWord. */
                            if (serverInfo.getCategories().get(lastIndex).equals("Video Games"))
                                randWord = gamesCategoryWords.get(randomNum);
                            else if (serverInfo.getCategories().get(lastIndex).equals("Sports"))
                                randWord = sportsCategoryWords.get(randomNum);
                            else
                                randWord = foodCategoryWords.get(randomNum);

                            // Check if it's a unique word from the list of words that the player has guessed, if it is then break from the while loop.
                            if(!words.contains(randWord)){
                                words.add(randWord); // Add it to the list of words the user has guessed on/being guessed on right now.
                                break;
                            }
                        }

                        serverInfo.clearGuesses(); // Reset the ArrayList of guesses.
                        serverInfo.setNumWrongGuesses(0); // Reset the number of wrong guesses.
                        serverInfo.setWord(words.get(words.size() - 1).length()); // This will make the word hidden with underscores equaling to the length of the word.

                        out.reset();
                        out.writeObject(serverInfo); // Send it to the client.

                        /* Update the state of the game. */
                        guess = true;
                        category = false;
                        gameOver = false;

                    }
                    // After the player won or lost, they come to this branch.
                    else if(gameOver){
                        playerInfo = (GuessInfo) in.readObject(); // Receive the client's GuessInfo

                        // If the received GuessInfo is null then that means the player chose to quit.
                        if(playerInfo == null) {
                            callback.accept("Player " + number + " left the game.");
                            clients.remove(this); // Remove the client from the ArrayList of clients.
                            break; // Break off the while loop to end the game.
                        }
                        // Else the player chose to play again.
                        else{
                            // Reset the server's GuessInfo with the player's GuessInfo
                            serverInfo = playerInfo;

                            /* Update the game's state */
                            guess = false;
                            category = true;
                            gameOver = false;
                        }
                    }
                }
                catch (Exception e) {
                    callback.accept("Something is wrong with the socket connection for player " + number);
                    clients.remove(this);
                    break;
                }
//                run game functionality
            } // end of game functionality while loop
        }// end of run method
    }
}
