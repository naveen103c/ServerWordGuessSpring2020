import com.sun.security.ntlm.Client;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ServerLogic {
    int portNum;
    Consumer<Serializable> callback;
    TheServer server = new TheServer();
    ArrayList<String> gamesCategoryWords;
    ArrayList<String> sportsCategoryWords;
    ArrayList<String> foodCategoryWords;
    ClientThread client;
    GuessInfo guessInformation;

    ServerLogic(Consumer<Serializable> call, int port) {
        callback = call;
        portNum = port;
        guessInformation = new GuessInfo();
        server.start();

        // initialize gamesCategoryWords and populate with words
        gamesCategoryWords = new ArrayList<>();
        gamesCategoryWords.add("Doom");
        gamesCategoryWords.add("Minecraft");
        gamesCategoryWords.add("Starcraft");
        gamesCategoryWords.add("Halo");
        gamesCategoryWords.add("Portal");
        gamesCategoryWords.add("Destiny");
        gamesCategoryWords.add("Diablo");
        gamesCategoryWords.add("Cuphead");
        gamesCategoryWords.add("Divinity");
        gamesCategoryWords.add("Warframe");

        // initialize sportsCategoryWords and populate with words
        sportsCategoryWords = new ArrayList<>();
        sportsCategoryWords.add("Volleyball");
        sportsCategoryWords.add("Hockey");
        sportsCategoryWords.add("Bowling");
        sportsCategoryWords.add("Basketball");
        sportsCategoryWords.add("Football");
        sportsCategoryWords.add("Soccer");
        sportsCategoryWords.add("Baseball");
        sportsCategoryWords.add("Tennis");
        sportsCategoryWords.add("Golf");
        sportsCategoryWords.add("Cricket");

        // initialize foodCategoryWords and populate with words
        foodCategoryWords = new ArrayList<>();
        foodCategoryWords.add("Sopes");
        foodCategoryWords.add("Samosas");
        foodCategoryWords.add("Dolma");
        foodCategoryWords.add("Haleem");
        foodCategoryWords.add("Pavlova");
        foodCategoryWords.add("Sushi");
        foodCategoryWords.add("Shakshuka");
        foodCategoryWords.add("Ramen");
        foodCategoryWords.add("Lamington");
        foodCategoryWords.add("Quesadilla");
    }

    public class TheServer extends Thread {
        // start the server and wait until a client has connected
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(portNum);) {
                System.out.println("Server is waiting for a client!");
                    client = new ClientThread(mySocket.accept());
                    callback.accept("A client has connected!");
                    client.start();
            }
            catch (Exception e) {
                callback.accept("Serer socket did not launch");
            }
        }
    }

    public class ClientThread extends Thread {
        Socket connection;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s) {
            this.connection = s;
        }

        // update client with relevant info
        public void updateClient() {
//            try {
//                client.out.writeObject();
//                client.out.reset();
//                client.out.reset();
//            }
//            catch (Exception e) {
//
//            }
        }

        // update server with relevant info
        public void updateServer() {

        }

        // logic for the game
        public void guessingGameLogic() {

        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }// end of try catch

//            while(true) {
//                try {
//
//                }
//                catch (Exception e) {
//                    callback.accept("Something is wrong with the socket connection");
//                    break;
//                }
//                run game functionality
//            } // end of game functionality while loop
        }// end of run method
    }
}
