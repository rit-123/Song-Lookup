import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * This class contains methods that test each of the 5 menu options specified in FrontendInterface
 */
@SuppressWarnings("SpellCheckingInspection")
public class FrontendDeveloperTests {
  /**
   * Tester method for the readFile() method in the frontend interface
   */
  @Test
  public void rTest() {
    TextUITester tester = new TextUITester("r\nmySongData.csv\nq\n");
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
   * Tester method for the getValues() method in the frontend interface
   */
  @Test
  public void gTest() {
    TextUITester tester = new TextUITester("g\no\n-10\n10\no\n5\n50\nq\n");
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
      Input must be an integer.
      
      Enter a MIN value:
      MIN must be non-negative. --> (-10 is negative)
      
      Enter a MIN value:
      Enter a MAX value:
      Input must be an integer.
      
      Enter a MAX value:
      MAX must be greater than or equal to MIN. --> (5 is not greater than 10)
      
      Enter a MAX value:
      Range chosen --> [10 - 50]
      Songs retrieved between [10 - 50]:
      1. Hey, Soul Sister
      2. Love The Way You Lie
      3. TiK ToK
      4. Bad Romance
      5. Just the Way You Are
      
      ~~~ Command Menu ~~~
          [R]ead Data
          [G]et Songs by Danceability [10 - 50]
          [F]ilter Fast Songs (by minimum speed: none)
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
   * Tester method for the setFilter() method in the frontend interface
   */
  @Test
  public void fTest() {
    TextUITester tester = new TextUITester("f\no\n-10\n30\nq\n");
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
      Enter a MIN BPM:
      Input must be an integer.
      
      Enter a MIN BPM:
      BPM must be non-negative. --> (-10 is negative)
      
      Enter a MIN BPM:
      MIN BPM chosen --> [30]
      
      ~~~ Command Menu ~~~
          [R]ead Data
          [G]et Songs by Danceability [MIN - MAX]
          [F]ilter Fast Songs (by minimum speed: 30)
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
   * Tester method for the topFive() method in the frontend interface
   */
  @Test
  public void dTest() {
    TextUITester tester = new TextUITester("d\nq\n");
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
      Top five energetic songs between [MIN - MAX]:
      1. 89: Hey, Soul Sister
      2. 93: Love The Way You Lie
      
      ~~~ Command Menu ~~~
          [R]ead Data
          [G]et Songs by Danceability [MIN - MAX]
          [F]ilter Fast Songs (by minimum speed: none)
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
   * Tester method for the quit input in the frontend interface
   */
  @Test
  public void qTest() {
    TextUITester tester = new TextUITester("m\nq\n");
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
      " m " is not a valid choice. Please try again.
      
      ~~~ Command Menu ~~~
          [R]ead Data
          [G]et Songs by Danceability [MIN - MAX]
          [F]ilter Fast Songs (by minimum speed: none)
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
