import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the frontend interface of Songify.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Frontend implements FrontendInterface {
  private final Scanner userIn;
  private final BackendInterface backend;
  private String min = "MIN";
  private String max = "MAX";
  private String speed = "none";

  /**
   * Constructor for Frontend
   * @param in System.in scanner
   * @param backend backend interface implementation
   */
  public Frontend(Scanner in, BackendInterface backend) {
    userIn = in;
    this.backend = backend;
  }

  /**
   * This method runs the user interface that prompts the user for input.
   */
  @Override
  public void runCommandLoop() {
    String choice;
    String exitMessage = """
    -------------------------------------
    Thank you for using Songify! Goodbye!""";
    do {
      displayMainMenu();
      choice = userIn.next();
      userIn.nextLine();

      if (!choice.equalsIgnoreCase("R") &&
          !choice.equalsIgnoreCase("G") &&
          !choice.equalsIgnoreCase("F") &&
          !choice.equalsIgnoreCase("D") &&
          !choice.equalsIgnoreCase("Q")) {
        System.out.println("\" " + choice + " \" is not a valid choice. Please try again.\n");
        continue;
      }
      switch (choice.toUpperCase()) {
        case "R" -> readFile();
        case "G" -> getValues();
        case "F" -> setFilter();
        case "D" -> topFive();
      }
    } while (!choice.equalsIgnoreCase("Q"));
    System.out.print(exitMessage);
    userIn.close();
  }

  /**
   * This method prints the main menu prompt.
   */
  @Override
  public void displayMainMenu() {
    String menu = """
      ~~~ Command Menu ~~~
          [R]ead Data
          [G]et Songs by Danceability [MIN - MAX]
          [F]ilter Fast Songs (by minimum speed: none)
          [D]isplay Five Most Energetic
          [Q]uit
        Choose command:""";
    menu=menu.replace("MIN",min).replace("MAX",max).replace("none",speed);
    System.out.println(menu);
  }

  /**
   * This method takes a user inputted file and sends it to the backend method to read it.
   */
  @Override
  public void readFile() {
    System.out.println("Enter path to csv file to load:");
    String filePath = userIn.next();
    userIn.nextLine();
    try {
      backend.readData(filePath);
      System.out.println("Done reading file.\n");
    } catch (IOException e) {
      System.out.println("\" " + filePath + " \" is not a valid file path. Please input [R] and try again.\n");
    }
  }

  /**
   * This method prompts the user for a range of values to send to the backend method for sorting by danceability.
   */
  @Override
  public void getValues() {
    System.out.println("Enter a MIN value:");
    int min;
    do {
      while (!userIn.hasNextInt()) {
        System.out.println("Input must be an integer.\n");
        System.out.println("Enter a MIN value:");
        userIn.nextLine();
      }
      min = userIn.nextInt();
      if (min < 0) {
        System.out.println("MIN must be non-negative. --> (" + min + " is negative)\n");
        System.out.println("Enter a MIN value:");
        userIn.nextLine();
      }
    } while (min < 0);

    userIn.nextLine();
    System.out.println("Enter a MAX value:");
    int max;
    do {
      while (!userIn.hasNextInt()) {
        System.out.println("Input must be an integer.\n");
        System.out.println("Enter a MAX value:");
        userIn.nextLine();
      }
      max = userIn.nextInt();
      if (max < min) {
        System.out.println("MAX must be greater than or equal to MIN. --> (" + max + " is not greater than " + min + ")\n");
        System.out.println("Enter a MAX value:");
        userIn.nextLine();
      }
    } while (max < min);

    this.min = String.valueOf(min);
    this.max = String.valueOf(max);
    System.out.println("Range chosen --> [" + min + " - " + max + "]");
    if (speed.equals("none")) {
      System.out.println("Songs retrieved between [" + min + " - " + max + "]:");
    } else {
      System.out.println("Songs retrieved between [" + min + " - " + max + "] with MIN BPM [" + speed + "]:");
    }
    List<String> getRange = backend.getRange(min, max);
    int count = 1;
    for (String song : getRange) {
      System.out.println(count + ". " + song);
      count++;
    }
  }

  /**
   * This user prompts the user for a minimum bpm to send to the backend method for filtering speed.
   */
  @Override
  public void setFilter() {
    System.out.println("Enter a MIN BPM:");
    int bpm;
    do {
      while (!userIn.hasNextInt()) {
        System.out.println("Input must be an integer.\n");
        System.out.println("Enter a MIN BPM:");
        userIn.nextLine();
      }
      bpm = userIn.nextInt();
      if (bpm < 0) {
        System.out.println("BPM must be non-negative. --> (" + bpm + " is negative)\n");
        System.out.println("Enter a MIN BPM:");
        userIn.nextLine();
      }
    } while (bpm < 0);

    speed = String.valueOf(bpm);
    System.out.println("MIN BPM chosen --> [" + bpm + "]");
    if (!min.equals("MIN")) {
      System.out.println("Updated songs between [" + min + " - " + max + "] now with MIN BPM [" + speed + "]:");
      List<String> filterFastSongs = backend.filterFastSongs(bpm);
      int count = 1;
      for (String song : filterFastSongs) {
        System.out.println(count + ". " + song);
        count++;
      }
    } else {
      backend.filterFastSongs(bpm);
      System.out.println();
    }
  }

  /**
   * This method prints out the top five energetic songs between the range specified earlier by the user.
   */
  @Override
  public void topFive() {
    try {
      if (speed.equals("none")) {
        System.out.println("Top five energetic songs between [" + min + " - " + max + "]:");
        List<String> fiveMostEnergetic = backend.fiveMostEnergetic();
        int count = 1;
        for (String song : fiveMostEnergetic) {
          System.out.println(count + ". " + song);
          count++;
        }
      } else {
        System.out.println("Top five energetic songs between [" + min + " - " + max + "] with MIN BPM [" + speed + "]:");
	List<String> fiveMostEnergetic = backend.fiveMostEnergetic();
        int count = 1;
        for (String song : fiveMostEnergetic) {
          System.out.println(count + ". " + song);
          count++;
        }
      }
    } catch (IllegalStateException e) {
      System.out.println("Please input [G] to specify a range and try again.\n");
    }
  }
}
