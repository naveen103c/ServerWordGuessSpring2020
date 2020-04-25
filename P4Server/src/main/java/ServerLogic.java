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
    ArrayList<String> gamesCategoryWords = new ArrayList<>();
    ArrayList<String> sportsCategoryWords = new ArrayList<>();
    ArrayList<String> foodCategoryWords = new ArrayList<>();


    ServerLogic(Consumer<Serializable> call, int port) {
        callback = call;
        portNum = port;
        server.start();

        // populate gamesCategoryWords with words
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

        // populate sportsCategoryWords with words
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

        // populate foodCategoryWords with words
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
        @Override
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(portNum);) {
                System.out.println("Server is waiting for a client!");
                while (true) {
                    ClientThread client = new ClientThread(mySocket.accept());
                }
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
            connection = s;
        }

        // update client with relevant info
        public void updateClient() {

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
