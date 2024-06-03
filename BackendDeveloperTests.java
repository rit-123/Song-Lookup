import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.Scanner;

public class BackendDeveloperTests {

    /**
     * Makes sure the read data function reads all the data and only throws an exception if
     * an invalid file name is passed as input
     */
    @Test
    public void readDataTest()
    {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
        BackendInterface backend = new Backend(tree);

        try {
            backend.readData("TesterSongs.csv");
        }
        catch (Exception e) {
            Assertions.fail();
        }

        try {
            backend.readData("....");
            Assertions.fail();
        }
        catch (Exception e) {
        }
    }

    /**
     * Tests the functionality of the getRange() function of the backend class
     * 
     */
    @Test
    public void rangeTester() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
        BackendInterface backend = new Backend(tree);
        try {
            backend.readData("TesterSongs.csv");
        }
        catch (Exception e) {
            Assertions.fail();
        }
        List<String> actual = backend.getRange(82, 83);
        List<String> expected = java.util.Arrays.asList(new String[]{    
        "The Time (Dirty Bit)",
        "S&M Remix",
        "Sing",
        "My Way",
        "24K Magic",
        "I Like It",
        "Telephone",
        "Your Love Is My Drug",
        "Lose Yourself to Dance",
        "Waves - Robin Schulz Radio Edit",
        "Downtown (feat. Melle Mel, Grandmaster Caz, Kool Moe Dee & Eric Nally)",
        "Pep Rally",
        "BURNITUP!",
        "Shape of You",
	    });
        for (int i = 0; i < actual.size(); i++) {
            boolean comparison = actual.get(i).equals(expected.get(i));
            Assertions.assertEquals(true, comparison);
        }
    }

    /**
     * Tests the functionality of the filerFastSong() function BEFORE the getRange() function is called
     */
    @Test
    public void filterFastSongsTester1() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
        BackendInterface backend = new Backend(tree);
        try {
            backend.readData("TesterSongs.csv");
        }
        catch (Exception e) {
            Assertions.fail();
        }

        List<String> returnValue = backend.filterFastSongs(120);
        Assertions.assertEquals(java.util.Arrays.asList(new String[]{}), returnValue);

         List<String> rangeReturn = backend.getRange(82, 83);

         List<String> expected = java.util.Arrays.asList(new String[]{
            "The Time (Dirty Bit)",
            "S&M Remix",
            "Sing",
            "My Way",
            "I Like It",
            "Telephone",
            "Your Love Is My Drug",
            "Waves - Robin Schulz Radio Edit",
            "BURNITUP!"
	    });
        
        for (int i = 0; i < rangeReturn.size(); i++) {
            boolean comparison = rangeReturn.get(i).equals(expected.get(i));
            Assertions.assertEquals(true, comparison);
        }
    }

     /**
     * Tests the functionality of the filterFastSong() function AFTER the getRange() function is called
     */
    @Test
    public void filterFastSongsTester2() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
        BackendInterface backend = new Backend(tree);
        try {
            backend.readData("TesterSongs.csv");
        }
        catch (Exception e) {
            Assertions.fail();
        }

        backend.getRange(82,83);

        List<String> returnValue = backend.filterFastSongs(130);
        List<String> expected = java.util.Arrays.asList(new String[]{
            "I Like It"
	    });

        // Comparing the output of the program with the actual output
        for (int i = 0; i < returnValue.size(); i++) {
            boolean comparison = returnValue.get(i).equals(expected.get(i));
            Assertions.assertEquals(true, comparison);
        }
    }

    /**
     * Tests the functionality of the fiveMostEnergetic() function
     */
    @Test
    public void fiveMostEnergeticTester() {
        IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
        BackendInterface backend = new Backend(tree);
        try {
            backend.readData("TesterSongs.csv");
        }
        catch (Exception e) {
            Assertions.fail();
        }

        backend.getRange(82,83);
        
        List<String> returnValue = backend.fiveMostEnergetic();
        List<String> expected = java.util.Arrays.asList(new String[]{
            "My Way",
            "The Time (Dirty Bit)",
            "S&M Remix",
            "24K Magic",
            "Telephone"
	    });
        

        // Comparing the output of the program with the actual output
        for (int i = 0; i < returnValue.size(); i++) {
            boolean comparison = returnValue.get(i).equals(expected.get(i));
            Assertions.assertEquals(true, comparison);
        }
    }

    /**
     * This method tests the integration of the frontend with the backend for the readData() method
     */
    @Test
    public void integrationTest1() {
        TextUITester tester = new TextUITester("r\nhi\nr\nsongs.csv\nq\n");
        FrontendInterface frontend = new Frontend(new Scanner(System.in), new Backend(new IterableRedBlackTree<>()));
        frontend.runCommandLoop();
        String expected = """
        ~~~ Command Menu ~~~
            [R]ead Data
            [G]et Songs by Danceability [MIN - MAX]
            [F]ilter Fast Songs (by minimum speed: none)
            [D]isplay Five Most Energetic
            [Q]uit
        Choose command:
        Enter path to csv file to load:
        " hi " is not a valid file path. Please input [R] and try again.

        ~~~ Command Menu ~~~
            [R]ead Data
            [G]et Songs by Danceability [MIN - MAX]
            [F]ilter Fast Songs (by minimum speed: none)
            [D]isplay Five Most Energetic
            [Q]uit
        Choose command:
        Enter path to csv file to load:
        Done reading file.

        ~~~ Command Menu ~~~
            [R]ead Data
            [G]et Songs by Danceability [MIN - MAX]
            [F]ilter Fast Songs (by minimum speed: none)
            [D]isplay Five Most Energetic
            [Q]uit
        Choose command:
        -------------------------------------
        Thank you for using Songify! Goodbye!
        ===================
        Thanks, and Goodbye""";
        String output = tester.checkOutput();
        Scanner expectedString = new Scanner(expected);
        Scanner outputString = new Scanner(output);
        while (expectedString.hasNext() && outputString.hasNext()) {
            Assertions.assertEquals(expectedString.next(), outputString.next());
        }
        expectedString.close();
        outputString.close();
  }

  /**
   *  This method tests the integration of the frontend with the backend for the getRange() method 
   */
  @Test
  public void integrationTestFilteredAndTopEngergetic() {
    TextUITester tester = new TextUITester("r\nsongs.csv\ng\n82\n83\nf\n130\nq\n");
    FrontendInterface frontend = new Frontend(new Scanner(System.in), new Backend(new IterableRedBlackTree<>()));
    frontend.runCommandLoop();

    String output = tester.checkOutput();

    List<String> expected = java.util.Arrays.asList(new String[]{    
        "The Time (Dirty Bit)",
        "S&M Remix",
        "Sing",
        "My Way",
        "24K Magic",
        "I Like It",
        "Telephone",
        "Your Love Is My Drug",
        "Lose Yourself to Dance",
        "Waves - Robin Schulz Radio Edit",
        "Downtown (feat. Melle Mel, Grandmaster Caz, Kool Moe Dee & Eric Nally)",
        "Pep Rally",
        "BURNITUP!",
        "Shape of You",
        });

    // Checks to see if all songs fetched when range is specified
    for (String song: expected) {
        Assertions.assertTrue(output.contains(song), "failed getRange()");
    }

    // Makes sure correct songs are listed after filter is called 
    Assertions.assertTrue(output.contains("1. I Like It"), "Fails filter tester");
  } 

  /**
   * Partner test to test the output of the frontend when the getValue() and setFilter() methods
   * are called sequentially
   */
  @Test
  public void PartnerTest1() {
    TextUITester tester = new TextUITester("g\n82\n83\nf\n130\nq\n");
    Frontend testFrontend = new Frontend(new Scanner(System.in), new BackendPlaceholder(
        new ISCPlaceholder<>()));
    testFrontend.runCommandLoop();

    String expected = """ 
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [MIN - MAX]
    [F]ilter Fast Songs (by minimum speed: none)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
Enter a MIN value:
Enter a MAX value:
Range chosen --> [82 - 83]
Songs retrieved between [82 - 83]:
1. Hey, Soul Sister
2. Love The Way You Lie
3. TiK ToK
4. Bad Romance
5. Just the Way You Are
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [82 - 83]
    [F]ilter Fast Songs (by minimum speed: none)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
Enter a MIN BPM:
MIN BPM chosen --> [130]
Updated songs between [82 - 83] now with MIN BPM [130]:
1. Hey, Soul Sister
2. Love The Way You Lie
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [82 - 83]
    [F]ilter Fast Songs (by minimum speed: 130)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
-------------------------------------
Thank you for using Songify! Goodbye!""";

    String output = tester.checkOutput();
    Scanner expectedString = new Scanner(expected);
    Scanner outputString = new Scanner(output);
    while (expectedString.hasNext() && outputString.hasNext()) {
      Assertions.assertEquals(expectedString.next(), outputString.next());
    }
    expectedString.close();
    outputString.close();
  }


  /**
   * Partner test to test the output of the frontend when the getValue(), setFilter() and topFive() methods
   * are called sequentially
   */
  @Test
  public void PartnerTest2() {
    TextUITester tester = new TextUITester("g\n82\n83\nf\n130\nd\nq\n");
    Frontend testFrontend = new Frontend(new Scanner(System.in), new BackendPlaceholder(
        new ISCPlaceholder<>()));
    testFrontend.runCommandLoop();

    String expected = """ 
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [MIN - MAX]
    [F]ilter Fast Songs (by minimum speed: none)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
Enter a MIN value:
Enter a MAX value:
Range chosen --> [82 - 83]
Songs retrieved between [82 - 83]:
1. Hey, Soul Sister
2. Love The Way You Lie
3. TiK ToK
4. Bad Romance
5. Just the Way You Are
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [82 - 83]
    [F]ilter Fast Songs (by minimum speed: none)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
Enter a MIN BPM:
MIN BPM chosen --> [130]
Updated songs between [82 - 83] now with MIN BPM [130]:
1. Hey, Soul Sister
2. Love The Way You Lie
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [82 - 83]
    [F]ilter Fast Songs (by minimum speed: 130)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
Top five energetic songs between [82 - 83] with MIN BPM [130]:
1. 89: Hey, Soul Sister
2. 93: Love The Way You Lie
~~~ Command Menu ~~~
    [R]ead Data
    [G]et Songs by Danceability [82 - 83]
    [F]ilter Fast Songs (by minimum speed: 130)
    [D]isplay Five Most Energetic
    [Q]uit
  Choose command:
-------------------------------------
Thank you for using Songify! Goodbye!""";

    String output = tester.checkOutput();
    Scanner expectedString = new Scanner(expected);
    Scanner outputString = new Scanner(output);
    while (expectedString.hasNext() && outputString.hasNext()) {
      Assertions.assertEquals(expectedString.next(), outputString.next());
    }
    expectedString.close();
    outputString.close();
  }
}