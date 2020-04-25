import java.io.Serializable;
import java.util.ArrayList;

public class GuessInfo implements Serializable {
    static final long serialVersionUID = 1L;
    private int numWrongGuesses;
    private ArrayList<Character> guesses;
    private String word;
    private ArrayList<String> categories;
    private int numWordsGuessed;

    GuessInfo() {
        numWrongGuesses = 0;
        guesses = new ArrayList<>();
        word = "";
        categories = new ArrayList<>();
        numWordsGuessed = 0;
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

    public void setGuesses(ArrayList<Character> guesses) {
        this.guesses = guesses;
    }

    public String getWord() {
        return word;
    }

    public void setWord(int length) {
        for(int i = 0; i < length; i++)
            word.concat("_");
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(String category) {
        categories.add(category);
    }
}
