import domain.*;
import domain.animation.*;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;


@Nested
public class LoadStatsTest extends Game {
    /**
     * Requires: A BufferedReader object that is not null and contains at least four lines of input.
     *           The first three lines should be ints representing chances, score, and time elapsed.
     *           The fourth line should be a long representing the version number of the game.
     * Modifies: This method will modify the player's chances, score, and the animator's start time.
     * Effects:  If the version number in the BufferedReader is greater than the current game version,
     *           this method will print a message to the console and return 1.
     *           If the version number is less than or equal to the current game version,
     *           this method will set the player's chances and score and the animator's start time based on the input,
     *           and return 0.
     *           If the BufferedReader does not contain the expected input, this method will throw a NumberFormatException.
     */

    private static final int gameVersion = 1;
    private static Player player;
    private static Animator animator;

    public LoadStatsTest() {
        player = new Player();
        animator = new Animator(this);
    }

    @Test
    public void testNonexistentFile() { // test 1
        assertThrows(IOException.class, () -> this.loadStats("nonexistent"));
    }

    // overriding to test with string inputs from right here instead of using files
    public int loadStats(BufferedReader reader) throws IOException {
        int chances = Integer.parseInt(reader.readLine());
        int score = Integer.parseInt(reader.readLine());
        int timeElapsed = Integer.parseInt(reader.readLine());
        long versionNumber = Long.parseLong(reader.readLine());
        if (versionNumber > gameVersion) {
            System.out.println("Save version is higher than current version.");
            return 1;
        }
        reader.close();
        player.setChances(chances);
        player.setScore(score);
        animator.setStartTimeMilli(System.currentTimeMillis() - timeElapsed);
        return 0;
    }

    public BufferedReader createReaderWithString(String input) {
        return new BufferedReader(new StringReader(input));
    }

    @Test
    public void testVersions() throws IOException { // test 2
        String wrongVersion = "3\n0\n1000\n2\n"; // should fail
        BufferedReader reader = createReaderWithString(wrongVersion);
        assertEquals(1, loadStats(reader));
        String correctVersion = "3\n0\n1000\n1\n"; // should pass (version up to date)
        BufferedReader reader2 = createReaderWithString(correctVersion);
        assertEquals(0, loadStats(reader2));
        String compatibleVersion = "3\n0\n1000\n0\n"; // should pass (save from older version, compatible)
        BufferedReader reader3 = createReaderWithString(compatibleVersion);
        assertEquals(0, loadStats(reader3));
    }

    @Test
    public void testValidInput() throws IOException { // test 3
        String input = "3\n0\n1000\n1\n";
        BufferedReader reader = createReaderWithString(input);
        assertEquals(0, loadStats(reader));
        assertEquals(3, player.getChances());
        assertEquals(0, player.getScore());
        assertEquals(1000, System.currentTimeMillis() - animator.getStartTimeMilli());
    }

    @Test
    public void testLoadStatsInvalidInput() { // test 4
        String invalidInput = "invalid\ninput\n";
        BufferedReader reader = new BufferedReader(new StringReader(invalidInput));
        LoadStatsTest game = new LoadStatsTest();
        assertThrows(NumberFormatException.class, () -> game.loadStats(reader));
    }

    public static void main(String[] args) throws IOException {
        LoadStatsTest test = new LoadStatsTest();
        test.testNonexistentFile();
        test.testVersions();
        test.testValidInput();
        test.testLoadStatsInvalidInput();
    }
}
