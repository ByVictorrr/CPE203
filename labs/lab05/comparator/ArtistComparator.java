
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
	
	public int compare(Song s1, Song s2)
	{
		//if s1.getartist has less lexicogrpahicallly letter like a than s2 b return less than 0
		return s1.getArtist().compareTo(s2.getArtist());
	}
	

	
//the value 0 if the argument string is equal to this string; 
//a value less than 0 if this string is lexicographically less than the string argument; 
//and a value greater than 0 if this string is lexicographically greater than the string argument.

}
