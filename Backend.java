import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Backend implements BackendInterface{
    private IterableSortedCollection<SongInterface> tree;
    private List<SongInterface> range = null;
    private int minBPM = -1;

    public Backend(IterableSortedCollection<SongInterface> tree) {
        this.tree = tree;
        this.minBPM = -1;
    }

    /**
     * Loads data from the .csv file referenced by filename.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException {
        File fileObj = new File(filename);
        Scanner scanner = new Scanner(fileObj);
        scanner.nextLine();
        while (scanner.hasNext()) {
            ArrayList<String> stringFields = new ArrayList<String>();
            ArrayList<Integer> intFields = new ArrayList<Integer>();
            String line = scanner.nextLine();
            String currentField = "";
            int fieldIndex = 0;
            int[] stringIndexes = {0, 1, 2};
            int quotesCounter = 0;


            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == ',' && quotesCounter % 2 == 0) {

                    // Add to string field arraylist
                    if (fieldIndex <= 2) {
                        stringFields.add(currentField);
                    }
                    else { // Add to integer field arraylist
                        intFields.add(Integer.parseInt(currentField));
                    }
                    currentField = "";
                    fieldIndex = fieldIndex + 1;

                    // making sure only first 8 fields are read
                    if (fieldIndex == 9) {
                        break;
                    }
                }
                else if (c == '\"') {
                        quotesCounter = quotesCounter + 1;
                        if (line.charAt(i + 1) == '\"') {
                            currentField = currentField + "\"";
                        }
                }
                else { // continue appending to the current word
                    currentField = currentField + c;
                    
                }
            }
            Song newSong = new Song(stringFields.get(0), stringFields.get(1), stringFields.get(2), intFields.get(0),
            intFields.get(1), intFields.get(2), intFields.get(3), intFields.get(4), intFields.get(5));
            this.tree.insert(newSong);
        }

    }

    /**
     * Retrieves a list of song titles for songs that have a Danceability
     * within the specified range (sorted by Danceability in ascending order).
     * If a minSpeed filter has been set using filterFastSongs(), then only 
     * songs with speed greater than or equal to that minSpeed should be 
     * included in the list returned by this method.
     *
     * Note that either this danceability range, or the resulting unfiltered 
     * list of songs should be saved for later use by the other methods 
     * defined in this class.
     *
     * @param low is the minimum danceability of songs in the returned list
     * @param hight is the maximum danceability of songs in the returned list
     * @return List of titles for all songs in specified range 
     */
    public List<String> getRange(int low, int high) {
        this.range = new ArrayList<SongInterface>();
        if (this.minBPM == -1){
            for  (SongInterface song: this.tree) {
                if (song.getDanceability() >= low && song.getDanceability() <= high) {
                    this.range.add(song);
                }
            }
        } 
        else if (this.minBPM != -1){ // if filterFastSongs HAS been called before
            for  (SongInterface song: this.tree) {
                if (song.getDanceability() >= low && song.getDanceability() <= high) {
                    this.range.add(song);
                }
            }
            return this.filterFastSongs(this.minBPM);
        }

        //Sorting them based on their danceability
        for (int i = 0; i < this.range.size(); i++) {
            int minDanceability = 999999999;
            int minDanceSongIndex = -1;

            for (int j = i; j < this.range.size(); j++) {
                if (minDanceability > this.range.get(j).getDanceability()) {
                    minDanceability = this.range.get(j).getDanceability();
                    minDanceSongIndex = j;
                }
            }
            SongInterface temp = this.range.get(minDanceSongIndex);
            this.range.set(minDanceSongIndex, this.range.get(i));
            this.range.set(i, temp);
        }

        List<String> stringRange = new ArrayList<String>();
        // add the NAMES of all the songs in this.range to stringRange HERE
        for (SongInterface currSong: this.range) {
            stringRange.add(currSong.getTitle());
        }

        return stringRange;
    }   

    /**
     * Filters the list of songs returned by future calls of getRange() and 
     * fiveMostEnergetic() to only include Fast songs.  If getRange() was 
     * previously called, then this method will return a list of song titles
     * (sorted in ascending order by Danceability) that only includes songs 
     * that as fast or faster than minSpeed.  If getRange() was not previusly
     * called, then this method should return an empty list.
     *
     * Note that this minSpeed threshold should be saved for later use by the 
     * other methods defined in this class.
     *
     * @param minSpeed is the minimum speed in bpm of returned songs
     * @return List of song titles, empty if getRange was not previously called
     */
    public List<String> filterFastSongs(int minSpeed) {
        this.minBPM = minSpeed;
        List<SongInterface> filtered = new ArrayList<SongInterface>();
        if (this.range == null) { // range has NOT been called yet!
            return new ArrayList<String>();
        } else { // range HAS been called
            for (SongInterface song: this.range) {
                if (song.getBPM() >= this.minBPM) {
                    filtered.add(song);
                }
            }
        }

        //Sorting them based on their danceability
        for (int i = 0; i < filtered.size(); i++) {
            int minDanceability = 999999999;
            int minDanceSongIndex = -1;

            for (int j = i; j < filtered.size(); j++) {
                if (minDanceability > filtered.get(j).getDanceability()) {
                    minDanceability = filtered.get(j).getDanceability();
                    minDanceSongIndex = j;
                }
            }
            SongInterface temp = filtered.get(minDanceSongIndex);
            filtered.set(minDanceSongIndex, filtered.get(i));
            filtered.set(i, temp);
        }
        List<String> stringFiltered = new ArrayList<String>();
        // add the NAMES of all the songs in this.range to stringRange HERE
        for (SongInterface currSong: filtered) {
            stringFiltered.add(currSong.getTitle());
        }
        return stringFiltered;
    }

    /**
     * This method makes use of the attribute range specified by the most
     * recent call to getRange().  If a minSpeed threshold has been set by
     * filterFastSongs() then that will also be utilized by this method.
     * Of those songs that match these criteria, the five most energetic will
     * be  returned by this method as a List of Strings in increasing order of 
     * danceability.  Each string contains the energy rating followed by a 
     * colon, a space, and then the song's title.
     * If fewer than five such songs exist, return all of them.
     *
     * @return List of five most energetic song titles and their energies
     * @throws IllegalStateException when getRange() was not previously called.
     */
    public List<String> fiveMostEnergetic() {
        if (this.range == null) { 
            throw new IllegalStateException("getRange() method not called previously!");
        }
        List<SongInterface> rangeCopy = new ArrayList<SongInterface>();
        for (SongInterface song: this.range) {
            if (song.getBPM() > this.minBPM) {
                rangeCopy.add(song);
            }
        }
        List<SongInterface> sortedRangeCopy = new ArrayList<SongInterface>();
        int numberOfElements = rangeCopy.size();
        if (numberOfElements >= 5) {
            numberOfElements = 5;
        }

        // Getting the top 5 Engergetic songs
        for (int i = 0; i < numberOfElements; i++) {
            int maxEnergy = -1000000;
            SongInterface maxSong = null;
            for (int j = 0; j < rangeCopy.size(); j++) {
                if (rangeCopy.get(j).getEnergy() > maxEnergy) {
                    maxSong = rangeCopy.get(j);
                    maxEnergy = maxSong.getEnergy();
                }
            }
            sortedRangeCopy.add(maxSong);
            rangeCopy.remove(maxSong);
        }
         
        // Sorting them based on their danceability
        for (int i = 0; i < sortedRangeCopy.size(); i++) {
            int minDanceability = 999999999;
            int minDanceSongIndex = -1;

            for (int j = i; j < sortedRangeCopy.size(); j++) {
                if (minDanceability > sortedRangeCopy.get(j).getDanceability()) {
                    minDanceability = sortedRangeCopy.get(j).getDanceability();
                    minDanceSongIndex = j;
                }
            }
            SongInterface temp = sortedRangeCopy.get(minDanceSongIndex);
            sortedRangeCopy.set(minDanceSongIndex, sortedRangeCopy.get(i));
            sortedRangeCopy.set(i, temp);
        }


        List<String> stringFiltered = new ArrayList<String>();
        // add the NAMES of all the songs in this.range to stringRange HERE
        for (SongInterface currSong: sortedRangeCopy) {
            stringFiltered.add(currSong.getTitle());
        }
        return stringFiltered;
    }

    // public static void main(String arg[]) {
    //      IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>();
    //     Backend backend = new Backend(tree);
    //     try {
    //         backend.readData("songs.csv");
    //     }
    //     catch (IOException e) {
            
    //     }
    //     //backend.filterFastSongs(110);
    //     List<String> rangeReturned = backend.getRange(69, 69);
    //     //rangeReturned = backend.fiveMostEnergetic();
    //     for (int i = 0; i < rangeReturned.size(); i++) {
    //         System.out.print(i);
    //         System.out.print(": ");
    //         System.out.println(rangeReturned.get(i));
    //     }
    // }
}