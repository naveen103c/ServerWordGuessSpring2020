import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

class GuessTest {
	GuessInfo guess;

	@BeforeEach
	void before() {
		guess = new GuessInfo();
	}

	@Test
	void testOne(){
		guess.setWord(5);

		assertEquals("_____", guess.getWord(), "Wrong amount of underscores");
	}

	@Test
	void testTwo(){
		guess.setWord(2);
		guess.setWord("Hey");

		assertEquals("Hey",guess.getWord(),"Wrong word");
	}

	@Test
	void testThree(){
		guess.setGuesses('q');
		guess.setGuesses('t');
		guess.setGuesses('p');
		guess.setGuesses('i');

		assertEquals(4,guess.getGuesses().size(), "Wrong size of guesses ArrayList");
	}

	@Test
	void testFour(){
		guess.setGuesses('q');
		guess.setGuesses('t');
		guess.setGuesses('p');
		guess.setGuesses('i');

		guess.clearGuesses();
		assertEquals(0,guess.getGuesses().size(), "Guesses ArrayList should be empty");
	}

	@Test
	void testFive(){
		guess.setCategories("Video Games");
		guess.setCategories("Sports");
		guess.setCategories("Food");

		assertEquals(3,guess.getCategories().size(), "Wrong size of categories ArrayList");
	}

	@Test
	void testSix(){
		guess.setCategories("Video Games");
		guess.setCategories("Sports");
		guess.setCategories("Food");

		guess.removeCategory();
		assertEquals(2,guess.getCategories().size(),"Wrong size of categories ArrayList #2");
	}

	@Test
	void testSeven(){
		guess.setCategories("Video Games");
		guess.setCategories("Sports");
		guess.setCategories("Food");

		guess.removeCategory();
		assertFalse(guess.getCategories().contains("Food"), "Categories ArrayList should have removed the last index");
	}

	@Test
	void testEight(){
		guess.setNumWrongGuesses(guess.getNumWrongGuesses() + 1);

		assertEquals(1, guess.getNumWrongGuesses(), "numWrongGuesses didn't get incremented correctly");
	}

	@Test
	void testNine(){
		guess.setNumWordsGuessed(guess.getNumWordsGuessed() + 1);

		assertEquals(1,guess.getNumWordsGuessed(), "numWordsGuessed wasn't incremented correctly");
	}

	@Test
	void testTen(){
		guess.setCategories("Video Games");
		guess.setCategories("Sports");
		guess.setCategories("Food");

		ArrayList list = guess.getCategories();

		assertEquals(3,list.size(), "Wrong size for the list ArrayList");
		assertTrue(list.contains("Video Games"), "List doesn't have the correct String");
		assertTrue(list.contains("Sports"), "List doesn't have the correct String");
		assertTrue(list.contains("Food"), "List doesn't have the correct String");
	}

}
