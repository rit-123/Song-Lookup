public class Song implements SongInterface {
    private String title;
    private String artist;
    private String topGenre;
    private int year;
    private int bpm;
    private int energy;
    private int danceability;
    private int loudness;
    private int liveliness;

    public Song(String title, String artist, String topGenre, int year, int bpm, int energy
        ,int danceability, int loudness, int liveliness) {
            this.title = title;
            this.artist = artist;
            this.topGenre = topGenre;
            this.year = year;
            this.bpm = bpm;
            this.energy = energy;
            this.danceability = danceability;
            this.loudness = loudness;
            this.liveliness = liveliness;
    }

    public String getTitle() {
        return this.title;
    } // returns this song's title

    public String getArtist() {
        return this.artist;
    } // returns this song's artist

    public String getGenres() {
        return this.topGenre;
    } // returns string containing each of this song's genres

    public int getYear() {
        return this.year;
    } // returns this song's year in the Billboard
    
    public int getBPM() {
        return this.bpm;
    } // returns this song's speed/tempo in beats per minute
    
    public int getEnergy() {
        return this.energy;
    } // returns this song's energy rating 
    
    public int getDanceability() {
        return this.danceability;
    }// returns this song's danceability rating
    
    public int getLoudness() {
        return this.loudness;
    } // returns this song's loudness in dB
    
    public int getLiveness() {
        return this.liveliness;
    } // returns this song's liveness rating
    
    public int compareTo(SongInterface otherSong) {
        if (this.danceability == otherSong.getDanceability()) {
            return 0;
        }
        else if (this.danceability > otherSong.getDanceability()){
            return 1;
        }
        else {
            return -1;
        }
    }

    public String toString() {
        return this.getTitle();
    }
}