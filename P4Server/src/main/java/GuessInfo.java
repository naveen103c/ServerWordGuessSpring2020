import java.io.Serializable;
import java.util.ArrayList;

/* This class will be used to hold data for the client and help update the game accordingly. */
public class GuessInfo implements Serializable {
    static final long serialVersionUID = 1L;
    private int numWrongGuesses; // The number of wrong guesses that the player made.
    private ArrayList<Character> guesses; // An ArrayList of all the guesses.
    private String word; // The word the player has to guess, it won't include the actual word, only underscores and it'll keep getting added into it with correct guesses.
    private ArrayList<String> categories; // The categories that the player has picked.
    private int numWordsGuessed; // The number of words the player guessed.

    // Initialize the variables when GuessInfo gets created
    GuessInfo() {
        numWrongGuesses = 0;
        guesses = new ArrayList<>();
        word = "";
        categories = new ArrayList<>();
        numWordsGuessed = 0;
    }

    // Clears the guesses
    public void clearGuesses(){
        this.guesses.clear();
    }

    public int getNumWrongGuesses() {
        return numWrongGuesses;
    }

    public void setNumWrongGuesses(int numWrongGuesses) {
        this.numWrongGuesses = numWrongGuesses;
    }

    public ArrayList<Character> getGuesses() {
        return guesses;
    }

    public void setGuesses(char c) {
        this.guesses.add(c);
    }

    public String getWord() {
        return word;
    }

    // If the given parameter is int then its the length of the word.
    public void setWord(int length) {
        // Concatenate underscores depending on the length of the word.
        word = "";
        for(int i = 0; i < length; i++)
            word = word.concat("_");
    }

    // If the parameter is just a string then make the word data member equal to name.
    public void setWord(String name) {
        word = name;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(String category) {
        categories.add(category);
    }

    // Remove specifically removes from the last index of the ArrayList categories.
    public void removeCategory(){
        categories.remove(categories.size() - 1);
    }

    public int getNumWordsGuessed() {
        return numWordsGuessed;
    }

    public void setNumWordsGuessed(int numWordsGuessed) {
        this.numWordsGuessed = numWordsGuessed;
    }
}
