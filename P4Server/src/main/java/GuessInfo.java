import java.io.Serializable;
import java.util.ArrayList;

public class GuessInfo implements Serializable {
    static final long serialVersionUID = 1L;
    private int numWrongGuesses = 0;
    private ArrayList<Character> guesses;
    private String word = "";
    private ArrayList<String> categories;

    GuessInfo(int length, String category){
        for(int i = 0; i < length; i++)
            word.concat("_");

        categories.add(category);
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

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
