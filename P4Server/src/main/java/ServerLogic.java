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
    int clientNum = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    Consumer<Serializable> callback;
    TheServer server = new TheServer();
    ArrayList<String> gamesCategoryWords;
    ArrayList<String> sportsCategoryWords;
    ArrayList<String> foodCategoryWords;
    GuessInfo guessInformation;

    ServerLogic(Consumer<Serializable> call, int port) {
        callback = call;
        portNum = port;
        guessInformation = new GuessInfo();
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

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.number = count;
        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }// end of try catch

            GuessInfo serverInfo = new GuessInfo();
            GuessInfo playerInfo;

            boolean guess = true;
            boolean category = false;
            boolean gameOver = false;

            Random rand = new Random();

            int randomNum =  rand.nextInt(10);
            ArrayList<String> words = new ArrayList<String>();
            String wordCreation = "";
            char letter;

            try {
                playerInfo = (GuessInfo) in.readObject();
                serverInfo.setCategories(playerInfo.getCategories().get(0)); // disgosten line.
            }
            catch (Exception e){
                System.out.println("Ain't workin' properly");
            }


            if(serverInfo.getCategories().get(0).equals("Video Games"))
                words.add(gamesCategoryWords.get(randomNum));
            else if(serverInfo.getCategories().get(0).equals("Sports"))
                words.add(sportsCategoryWords.get(randomNum));
            else // If the wrong word given is from category Foods then this might be the issue.
                words.add(foodCategoryWords.get(randomNum));

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
                            callback.accept("Player " + number + " guessed a letter twice");
                            continue; // If not then try again.
                        }
                        else {
                            serverInfo.setGuesses(letter);
                            // If the letter doesn't exist in the word
                            if(words.get(words.size() - 1).indexOf(letter) == -1){ // Still needs so much work
                                serverInfo.setNumWrongGuesses(serverInfo.getNumWrongGuesses() + 1);
                                if(serverInfo.getNumWrongGuesses() == 6){

                                    serverInfo.setNumWordsGuessed(serverInfo.getNumWordsGuessed() + 1);
                                    serverInfo.clearGuesses();
                                    serverInfo.removeCategory();

                                    callback.accept("Player " + number + " lost the guess round!");
                                    guess = false;
                                    category = true;
                                    gameOver = false;

                                    int gameCount = 0;
                                    int foodCount = 0;
                                    int sportCount = 0;
                                    int size = words.size(); // just to make it look better

                                    // this for loops just counts up the words based on category (hopefully none are >= 4)
                                    for(int i = 0; i < size; i++){
                                        if(gamesCategoryWords.contains(words.get(i)))
                                            gameCount++;
                                        else if (foodCategoryWords.contains(words.get(i)))
                                            foodCount++;
                                        else if(sportsCategoryWords.contains(words.get(i)))
                                            sportCount++;
                                    }
                                    if((gameCount == 3 && gamesCategoryWords.contains(words.get(size - 1)))
                                    || (foodCount == 3 && foodCategoryWords.contains(words.get(size - 1)))
                                    || (sportCount == 3 && sportsCategoryWords.contains(words.get(size - 1)))){
                                        callback.accept("Player " + number + " lost the game!");
                                        guess = false;
                                        category = false;
                                        gameOver = true;
                                    }

                                }
                                out.reset();
                                out.writeObject(serverInfo);
                            }
                            // If the letter exists in the word being guessed
                            else{
                                wordCreation = serverInfo.getWord();
                                for(int i = 0; i < words.get(words.size() - 1).length(); i++){
                                    if(words.get(words.size() - 1).charAt(i) == letter){
                                        wordCreation = wordCreation.substring(0, i) + letter + wordCreation.substring(i+1);
                                    }
                                }
                                serverInfo.setWord(wordCreation);
                                // If the player guesses the entire word correctly.
                                if(wordCreation.equals(words.get(words.size() - 1))){
                                    callback.accept("Player " + number + " guessed the word correctly!");
                                    serverInfo.setNumWordsGuessed(serverInfo.getNumWordsGuessed() + 1);
                                    serverInfo.clearGuesses();
                                    guess = false;
                                    category = true;
                                    gameOver = false;
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
                    else if(category){
                        playerInfo = (GuessInfo) in.readObject();
                        serverInfo.setCategories(playerInfo.getCategories().get(playerInfo.getCategories().size() - 1));

                        while(true) {
                            randomNum = rand.nextInt(10);

                            int lastIndex = serverInfo.getCategories().size() - 1;

                            String randWord;
                            if (serverInfo.getCategories().get(lastIndex).equals("Video Games"))
                                //words.add(gamesCategoryWords.get(randomNum));
                                randWord = gamesCategoryWords.get(randomNum);
                            else if (serverInfo.getCategories().get(lastIndex).equals("Sports"))
                                //words.add(sportsCategoryWords.get(randomNum));
                                randWord = sportsCategoryWords.get(randomNum);
                            else // If the wrong word given is from category Foods then this might be the issue.
                                //words.add(foodCategoryWords.get(randomNum));
                                randWord = foodCategoryWords.get(randomNum);

                            if(!words.contains(randWord)){
                                words.add(randWord);
                                break;
                            }
                        }

                        serverInfo.clearGuesses();
                        serverInfo.setNumWrongGuesses(0);
                        serverInfo.setWord(words.get(words.size() - 1).length());
                        out.reset();
                        out.writeObject(serverInfo);

                        guess = true;
                        category = false;
                        gameOver = false;

                    }
                    else if(gameOver){
                        playerInfo = (GuessInfo) in.readObject();

                        if(playerInfo == null) {
                            callback.accept("Player " + number + " left the game.");
                            clients.remove(this);
                            break;
                        }
                        else{
                            serverInfo = playerInfo;
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
