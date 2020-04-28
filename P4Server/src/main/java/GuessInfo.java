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

    public void setWord(int length) {
        word = "";
        for(int i = 0; i < length; i++)
            word = word.concat("_");
    }

    public void setWord(String name) {
        word = name;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(String category) {
        categories.add(category);
    }

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
