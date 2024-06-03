import java.util.List;
import java.io.IOException;

/**
 * BackendInterface - CS400 Project 1: iSongify
 */
public interface BackendInterface {
 
    //public BackendPlaceholder(IterableSortedCollection<SongInterface> tree);

    /**
     * Loads data from the .csv file referenced by filename.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException;

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
    public List<String> getRange(int low, int high);

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
    public List<String> filterFastSongs(int minSpeed);

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
    public List<String> fiveMostEnergetic();
}
