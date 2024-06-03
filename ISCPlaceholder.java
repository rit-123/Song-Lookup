import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ISCPlaceholder<T extends Comparable<T>>
    implements IterableSortedCollection<T> {

    private T value;
    private List<T> songs = new ArrayList<T>();
    
    public boolean insert(T data)
	throws NullPointerException, IllegalArgumentException {
	value = data;
    songs.add(data);
	return true;
    }

    public boolean contains(Comparable<T> data) {
	return true;
    }

    public boolean isEmpty() {
	return false;
    }
    
    public int size() {
	return songs.size();
    }

    public void clear() {
    }

    public void setIterationStartPoint(Comparable<T> startPoint) {	
    }

    public Iterator<T> iterator() {
	
	return songs.iterator();
    }
}
