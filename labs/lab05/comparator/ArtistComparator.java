
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;


public class ArtistComparator implements Comparator <Song>{
	
	public int compare(Song s1, Song s2){return (int)(s1.getArtist().charAt(0)) - (int)(s2.getArtist().charAt(0));}
	

	


}
